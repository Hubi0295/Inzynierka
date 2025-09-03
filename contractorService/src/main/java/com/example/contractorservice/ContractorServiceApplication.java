package com.example.contractorservice;

import com.example.auth.services.JwtService;
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
    @Bean
    public JwtService jwtService(){
        return new JwtService("f1d8b9f42649a9a758ead13908ddce7f59280d4047a707af06176b4441764f90");
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}
