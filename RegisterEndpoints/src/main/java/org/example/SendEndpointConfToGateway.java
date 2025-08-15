package org.example;

import org.example.entity.Endpoint;

import java.util.ArrayList;
import java.util.List;

public interface SendEndpointConfToGateway {
    List<Endpoint> endpoints = new ArrayList<>();
    void initMap();
    void register();
}
