package com.example.gateway.filter;

import org.example.entity.Endpoint;
import org.example.entity.HttpMethod;
import org.example.entity.Response;
import org.example.entity.UserType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final Set<Endpoint> openApiEndpoints = new HashSet<>(List.of(
            new Endpoint("/v3/api-docs/**", HttpMethod.GET, UserType.USER),
            new Endpoint("/swagger-ui/**", HttpMethod.GET, UserType.USER),
            new Endpoint("/swagger-ui.html", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/auth/users", HttpMethod.POST, UserType.USER),
            new Endpoint("/api/auth/login", HttpMethod.POST, UserType.USER),
            new Endpoint("/api/auth/validate", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/auth/logout", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/auth/auto-login", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/auth/authorizeAdmin", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/auth/authorizeSupervisor", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/gateway", HttpMethod.POST, UserType.USER)
    ));

    private final Set<Endpoint> adminEndpoints = new HashSet<>(List.of(
            new Endpoint("/api/auth/users/{uuid}", HttpMethod.PUT, UserType.ADMIN),
            new Endpoint("/api/auth/users/{uuid}", HttpMethod.DELETE, UserType.ADMIN),
            new Endpoint("/api/auth/users/{uuid}/role", HttpMethod.PATCH, UserType.ADMIN),
            new Endpoint("/api/auth/users/{uuid}", HttpMethod.GET, UserType.ADMIN),
            new Endpoint("/api/auth/users", HttpMethod.GET, UserType.ADMIN)
    ));

    private final Set<Endpoint> supervisorEndpoints = new HashSet<>();

    public void addEndpoints(List<Endpoint> endpointList) {
        for (Endpoint endpoint : endpointList) {
            if (endpoint.getRole() == UserType.ADMIN) {
                adminEndpoints.add(endpoint);
            } else if (endpoint.getRole() == UserType.USER) {
                openApiEndpoints.add(endpoint);
            } else if (endpoint.getRole() == UserType.SUPERVISOR) {
                supervisorEndpoints.add(endpoint);
            }
        }
    }

    private boolean matches(ServerHttpRequest request, Endpoint endpoint) {
        return pathMatcher.match(endpoint.getUrl(), request.getURI().getPath()) &&
                request.getMethod() != null &&
                request.getMethod().name().equals(endpoint.getHttpMethod().name());
    }

    public final Predicate<ServerHttpRequest> isAdmin =
            request -> adminEndpoints.stream().anyMatch(endpoint -> matches(request, endpoint));

    public final Predicate<ServerHttpRequest> isSupervisor =
            request -> supervisorEndpoints.stream().anyMatch(endpoint -> matches(request, endpoint));

    public final Predicate<ServerHttpRequest> isOpen =
            request -> openApiEndpoints.stream().anyMatch(endpoint -> matches(request, endpoint));

    public final Predicate<ServerHttpRequest> isSecure =
            request -> !isOpen.test(request);
}
