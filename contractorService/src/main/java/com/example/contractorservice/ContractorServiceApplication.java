package com.example.contractorservice;

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
        "com.example.auth.entity",
})
@EnableJpaRepositories(basePackages = {
        "com.example.contractorservice.repository",
        "com.example.auth.repository"
})
public class ContractorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractorServiceApplication.class, args);
    }

    @Value("${jwt.niewzykle_wazny_secret_jwt}")
    private String jwtSecret;
    @Bean
    public JwtService jwtService() {
        return new JwtService(jwtSecret);
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}
