package com.example.product;

import com.example.auth.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.example.contractorservice.entity",
        "com.example.product.entity",
        "com.example.auth.entity",
        "com.example.warehouse.entity"
})
@EnableJpaRepositories(basePackages = {
        "com.example.product.repository",
        "com.example.contractorservice.repository",
        "com.example.warehouse.repository",
        "com.example.auth.repository"
})
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
    @Value("${jwt.niewzykle_wazny_secret_jwt}")
    private String jwtSecret;
    @Bean
    public JwtService jwtService() {
        return new JwtService(jwtSecret);
    }
}
