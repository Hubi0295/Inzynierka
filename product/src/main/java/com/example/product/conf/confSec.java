package com.example.product.conf;



import com.example.auth.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class confSec {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/products",
                        "/api/products/{uuid}",
                        "/api/products/{rfid}/location",
                        "/api/products/categories",
                        "/api/products/categories/{id}",
                        "/api/products/transfer",
                        "/api/products/inventories/{uuid}",
                        "/api/products/inventories/{uuid}",
                        "/api/products/history/{uuid}",
                        "/actuator/health"
                )
                .permitAll()
                .and()
                .build();
    }
}
