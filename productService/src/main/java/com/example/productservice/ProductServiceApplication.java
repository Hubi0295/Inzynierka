package com.example.productservice;

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
        "com.example.productservice.entity",
        "com.example.auth.entity",
        "com.example.product.entity",
        "com.example.contractorservice.entity",
        "com.example.warehouse.entity"
})
@EnableJpaRepositories(basePackages = {
        "com.example.productservice.repository",
        "com.example.auth.repository",
        "com.example.product.repository",
        "com.example.contractorservice.repository"
})
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }
    @Value("${jwt.niewzykle_wazny_secret_jwt}")
    private String jwtSecret;
    @Bean
    public JwtService jwtService() {
        return new JwtService(jwtSecret);
    }
}
