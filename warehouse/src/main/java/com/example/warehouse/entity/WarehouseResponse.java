package com.example.warehouse.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class WarehouseResponse {
    private String timestamp;
    private String message;
    private UUID uuid;
    public WarehouseResponse(String message, UUID uuid){
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        this.message = message;
        this.uuid=uuid;
    }
}