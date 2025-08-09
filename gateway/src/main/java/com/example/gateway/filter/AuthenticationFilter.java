package com.example.gateway.filter;

import com.example.gateway.conf.Carousel;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final String AUTH_COOKIE_NAME = "Authorization";
    private static final String REFRESH_COOKIE_NAME = "refresh";

    private final RouteValidator validator;
    private final RestTemplate template;
    private final Carousel carousel;

    public AuthenticationFilter(RestTemplate restTemplate, RouteValidator validator, Carousel carousel) {
        super(Config.class);
        this.carousel = carousel;
        this.template = restTemplate;
        this.validator = validator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (validator.isSecure.test(exchange.getRequest())) {

                List<HttpCookie> authCookies = exchange.getRequest().getCookies().get(AUTH_COOKIE_NAME);
                List<HttpCookie> refreshCookies = exchange.getRequest().getCookies().get(REFRESH_COOKIE_NAME);

                if (authCookies == null || authCookies.isEmpty() || refreshCookies == null || refreshCookies.isEmpty()) {
                    return unauthorizedRawJson(exchange.getResponse(), "");
                }

                HttpCookie authCookie = authCookies.get(0);
                HttpCookie refreshCookie = refreshCookies.get(0);

                try {
                    String cookiesHeader = authCookie.getName() + "=" + authCookie.getValue() + ";" +
                            refreshCookie.getName() + "=" + refreshCookie.getValue();

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add("Cookie", cookiesHeader);
                    HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);

                    ResponseEntity<String> response;

                    if (validator.isAdmin.test(exchange.getRequest())) {
                        response = template.exchange(
                                "http://" + carousel.getUriAuth() + "/api/auth/authorizeAdmin",
                                HttpMethod.GET, entity, String.class
                        );
                    } else if (validator.isSupervisor.test(exchange.getRequest())) {
                        response = template.exchange(
                                "http://" + carousel.getUriAuth() + "/api/auth/authorizeSupervisor",
                                HttpMethod.GET, entity, String.class
                        );
                    } else {
                        response = template.exchange(
                                "http://" + carousel.getUriAuth() + "/api/auth/validate",
                                HttpMethod.GET, entity, String.class
                        );
                    }

                    if (response.getStatusCode() == HttpStatus.OK) {
                        List<String> cookiesList = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                        if (cookiesList != null && !cookiesList.isEmpty()) {
                            List<java.net.HttpCookie> parsedCookies = java.net.HttpCookie.parse(cookiesList.get(0));
                            for (java.net.HttpCookie cookie : parsedCookies) {
                                exchange.getResponse().addCookie(
                                        ResponseCookie.from(cookie.getName(), cookie.getValue())
                                                .domain(cookie.getDomain())
                                                .path(cookie.getPath())
                                                .maxAge(cookie.getMaxAge())
                                                .secure(cookie.getSecure())
                                                .httpOnly(cookie.isHttpOnly())
                                                .build()
                                );
                            }
                        }
                    }

                } catch (HttpClientErrorException e) {
                    String body = e.getResponseBodyAsString(); // to będzie czysty JSON z Auth
                    return unauthorizedRawJson(exchange.getResponse(), body);
                }
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> unauthorizedRawJson(ServerHttpResponse response, String rawJson) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(
                new DefaultDataBufferFactory().wrap(rawJson.getBytes())
        ));
    }

    public static class Config {
    }
}
