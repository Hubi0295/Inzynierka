package com.example.warehouse.configuration;



import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                        "/actuator/health",
                        "/api/warehouseManagement/warehouses",
                        "/api/warehouseManagement/warehouses/{uuid}",
                        "/api/warehouseManagement/warehouses/{uuid}",
                        "/api/warehouseManagement/warehouses/{uuid}/halls",
                        "/api/warehouseManagement/halls/{uuid}",
                        "/api/warehouseManagement/halls/{uuid}",
                        "/api/warehouseManagement/halls/{uuid}/shelves",
                        "/api/warehouseManagement/shelves/{uuid}",
                        "/api/warehouseManagement/shelves/{uuid}",
                        "/api/warehouseManagement/shelves/{uuid}/spots",
                        "/api/warehouseManagement/spots/{uuid}",
                        "/api/warehouseManagement/spots/{uuid}",
                        "/api/warehouseManagement/warehouses/report"
                )
                .permitAll()
                .and()
                .build();
    }

}
