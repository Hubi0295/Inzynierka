package com.example.product.conf;

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
        endpoints.add(new Endpoint("/api/products",HttpMethod.POST,UserType.USER));
        endpoints.add(new Endpoint("/api/products",HttpMethod.GET,UserType.USER));
        endpoints.add(new Endpoint("/api/products/{uuid}",HttpMethod.POST,UserType.USER));
        endpoints.add(new Endpoint("/api/products/{uuid}",HttpMethod.DELETE,UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/products/{uuid}",HttpMethod.GET,UserType.USER));
        endpoints.add(new Endpoint("/api/products/{rfid}/location",HttpMethod.GET,UserType.USER));
        endpoints.add(new Endpoint("/api/products/categories",HttpMethod.POST,UserType.USER));
        endpoints.add(new Endpoint("/api/products/categories",HttpMethod.GET,UserType.USER));
        endpoints.add(new Endpoint("/api/products/categories/{id}",HttpMethod.PUT,UserType.USER));
        endpoints.add(new Endpoint("/api/products/categories/{id}",HttpMethod.DELETE,UserType.USER));
        endpoints.add(new Endpoint("/api/products/transfer",HttpMethod.GET,UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/products/inventories/{uuid}",HttpMethod.GET,UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/products/inventories/{uuid}",HttpMethod.POST,UserType.SUPERVISOR));
        endpoints.add(new Endpoint("/api/products/history/{uuid}",HttpMethod.POST,UserType.SUPERVISOR));
    }

    @Override
    public void register() {
        restTemplate.postForEntity(ApiGatewayUrl, endpoints, Response.class);
    }

    @Override
    public void run(String... args) throws Exception {
        initMap();
        register();
    }
}
