package com.example.warehouse.configuration;

import org.example.entity.Endpoint;
import org.example.entity.HttpMethod;
import org.example.entity.Response;
import org.example.entity.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.example.SendEndpointConfToGateway;
import org.springframework.web.client.RestTemplate;

@Component
public class SendEndpointConfToGatewayImplementation implements SendEndpointConfToGateway, CommandLineRunner {
    @Value("${apigateway.url}")
    private String ApiGatewayUrl;
    private final RestTemplate restTemplate;

    public SendEndpointConfToGatewayImplementation(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void initMap() {
        endpoints.add(new Endpoint("/api/warehouseManagement/warehouses", HttpMethod.POST, UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/warehouseManagement/warehouses/{uuid}", HttpMethod.PUT, UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/warehouseManagement/warehouses/{uuid}", HttpMethod.DELETE, UserType.SUPERVISOR));

        endpoints.add(new Endpoint("/api/warehouseManagement/warehouses/{uuid}/halls", HttpMethod.POST, UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/warehouseManagement/halls/{uuid}", HttpMethod.PUT, UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/warehouseManagement/halls/{uuid}", HttpMethod.DELETE, UserType.SUPERVISOR));

        endpoints.add(new Endpoint("/api/warehouseManagement/halls/{uuid}/shelves", HttpMethod.POST, UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/warehouseManagement/shelves/{uuid}", HttpMethod.PUT, UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/warehouseManagement/shelves/{uuid}", HttpMethod.DELETE, UserType.SUPERVISOR));

        endpoints.add(new Endpoint("/api/warehouseManagement/shelves/{uuid}/spots", HttpMethod.POST, UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/warehouseManagement/spots/{uuid}", HttpMethod.PUT, UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/warehouseManagement/spots/{uuid}", HttpMethod.DELETE, UserType.SUPERVISOR));
    }

    @Override
    public void register() {
        restTemplate.postForEntity(ApiGatewayUrl, endpoints, Response.class);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("TUTTTAAAAJ!!!!");
        initMap();
        register();
    }
}
