package com.example.productservice.conf;

import org.example.SendEndpointConfToGateway;
import org.example.entity.Endpoint;
import org.example.entity.HttpMethod;
import org.example.entity.Response;
import org.example.entity.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
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
        endpoints.add(new Endpoint("/api/productService/receipts", HttpMethod.POST, UserType.USER));
        endpoints.add(new Endpoint("/api/productService/receipts", HttpMethod.GET, UserType.USER));
        endpoints.add(new Endpoint("/api/productService/issues", HttpMethod.POST, UserType.USER));
        endpoints.add(new Endpoint("/api/productService/issues", HttpMethod.GET, UserType.USER));
        endpoints.add(new Endpoint("/api/productService/receipts/{uuid}", HttpMethod.PUT, UserType.USER));
        endpoints.add(new Endpoint("/api/productService/receipts/{uuid}", HttpMethod.GET, UserType.USER));
        endpoints.add(new Endpoint("/api/productService/issues/{uuid}", HttpMethod.PUT, UserType.USER));
        endpoints.add(new Endpoint("/api/productService/issues/{uuid}", HttpMethod.GET, UserType.USER));
        endpoints.add(new Endpoint("/api/productService/receipts/{uuid}", HttpMethod.DELETE, UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/productService/issues/{uuid}", HttpMethod.DELETE, UserType.SUPERVISOR));
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
