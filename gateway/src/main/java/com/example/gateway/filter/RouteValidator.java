package com.example.gateway.filter;
import com.example.gateway.entity.Endpoint;
import com.example.gateway.entity.HttpMethod;
import com.example.gateway.entity.UserType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public Set<Endpoint> openApiEndpoints = new HashSet<>(List.of(
            new Endpoint("/api/auth/users", HttpMethod.POST, UserType.USER),
            new Endpoint("/api/auth/login", HttpMethod.POST, UserType.USER),
            new Endpoint("/api/auth/validate", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/auth/logout", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/auth/auto-login", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/auth/authorizeAdmin", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/auth/authorizeSupervisor", HttpMethod.GET, UserType.USER),
            new Endpoint("/api/gateway", HttpMethod.POST, UserType.USER)
            )
    );
    private Set<Endpoint> adminEndpoints = new HashSet<>();

    public void addEndpoints(List<Endpoint> endpointList){
        for (Endpoint endpoint: endpointList){
            if (endpoint.getRole().name().equals(UserType.ADMIN.name())) {
                adminEndpoints.add(endpoint);
            }
            if (endpoint.getRole().name().equals(UserType.USER.name())) {
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
