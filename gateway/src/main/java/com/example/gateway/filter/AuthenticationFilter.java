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

    private final RouteValidator validator;
    private final RestTemplate template;
    private final Carousel carousel;

    public AuthenticationFilter(RestTemplate restTemplate, RouteValidator validator,Carousel carousel) {
        super(Config.class);
        this.carousel = carousel;
        this.template = restTemplate;
        this.validator = validator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            if (validator.isSecure.test(exchange.getRequest())) {
                if (!exchange.getRequest().getCookies().containsKey(HttpHeaders.AUTHORIZATION) && !exchange.getRequest().getCookies().containsKey("refresh")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    StringBuilder stringBuilder = new StringBuilder("{\n")
                            .append("\"timestamp\": \"")
                            .append(new Timestamp(System.currentTimeMillis()))
                            .append("\",\n")
                            .append("\"message\": \"Wskazany token jest pusty lub nie ważny\"\n")
                            .append("}");

                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap((stringBuilder.toString()).getBytes())));
                }

                HttpCookie authCookie = exchange.getRequest().getCookies().get(HttpHeaders.AUTHORIZATION).get(0);
                HttpCookie refreshCookie = exchange.getRequest().getCookies().get("refresh").get(0);
                try {
                        String cookies = new StringBuilder()
                                .append(authCookie.getName())
                                .append("=")
                                .append(authCookie.getValue())
                                .append(";")
                                .append(refreshCookie.getName())
                                .append("=")
                                .append(refreshCookie.getValue()).toString();

                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.add("Cookie",cookies);
                        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
                        ResponseEntity<String> response;
                        if (validator.isAdmin.test(exchange.getRequest())){
                            response = template.exchange("http://"+carousel.getUriAuth()+"/api/v1/auth/authorize", HttpMethod.GET,entity, String.class);
                        }else {
                            response = template.exchange("http://" + carousel.getUriAuth() + "/api/v1/auth/validate", HttpMethod.GET, entity, String.class);
                        }
                        if (response.getStatusCode() == HttpStatus.OK){
                            List<String> cookiesList = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                            if (cookiesList != null) {
                                List<java.net.HttpCookie> httpCookie = java.net.HttpCookie.parse(cookiesList.get(0));
                                for (java.net.HttpCookie cookie: httpCookie){
                                    exchange.getResponse().getCookies().add(cookie.getName(),
                                            ResponseCookie.from(cookie.getName(),cookie.getValue())
                                                    .domain(cookie.getDomain())
                                                    .path(cookie.getPath())
                                                    .maxAge(cookie.getMaxAge())
                                                    .secure(cookie.getSecure())
                                                    .httpOnly(cookie.isHttpOnly())
                                                    .build());
                                }
                            }
                        }

                } catch (HttpClientErrorException e) {
                    String message  = e.getMessage().substring(7);
                    message = message.substring(0,message.length()-1);
                    ServerHttpResponse response = exchange.getResponse();
                    HttpHeaders headers = response.getHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().writeWith(Flux.just(new DefaultDataBufferFactory().wrap(message.getBytes())));
                }
            }
            return chain.filter(exchange);
        });
    }


    public static class Config {

    }
}
