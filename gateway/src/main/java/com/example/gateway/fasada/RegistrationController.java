package com.example.gateway.fasada;

import com.example.gateway.entity.Response;
import com.example.gateway.entity.Endpoint;
import com.example.gateway.filter.RouteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/gateway")
@RequiredArgsConstructor
public class RegistrationController {
    private final RouteValidator routeValidator;
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody List<Endpoint> endpoints){
        routeValidator.addEndpoints(endpoints);
        return ResponseEntity.ok(new Response("Pomyslnie zarejestrowano endpoint"));
    }
}
