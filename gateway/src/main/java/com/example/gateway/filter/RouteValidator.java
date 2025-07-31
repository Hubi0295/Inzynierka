package com.example.gateway.filter;

import com.example.auth.entity.UserRole;
import com.example.gateway.entity.Endpoint;
import com.example.gateway.entity.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public Set<Endpoint> openApiEndpoints = new HashSet<>(List.of(
            new Endpoint("/auth/users", HttpMethod.POST,UserRole.USER),
            new Endpoint("/auth/login", HttpMethod.POST,UserRole.USER),
            new Endpoint("/auth/validate", HttpMethod.GET,UserRole.USER)
            )
    );
    private Set<Endpoint> adminEndpoints = new HashSet<>();

    public void addEndpoints(List<Endpoint> endpointList){
        for (Endpoint endpoint: endpointList){
            if (endpoint.getRole().name().equals(UserRole.ADMIN.name())) {
                adminEndpoints.add(endpoint);
            }
            if (endpoint.getRole().name().equals(UserRole.USER.name())) {
                openApiEndpoints.add(endpoint);
            }

        }
    }

    public Predicate<ServerHttpRequest> isAdmin =
            request -> adminEndpoints
                    .stream()
                    .anyMatch(value -> request.getURI()
                            .getPath()
                            .contains(value.getUrl())
                            && request.getMethod().name().equals(value.getHttpMethod().name()));
    public Predicate<ServerHttpRequest> isSecure = request->openApiEndpoints.stream()
            .noneMatch(value->request.getURI()
                    .getPath()
                    .contains(value.getUrl())
                    && request.getMethod().name().equals(value.getHttpMethod().name()));
}
