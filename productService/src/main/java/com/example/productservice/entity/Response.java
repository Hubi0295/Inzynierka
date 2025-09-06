package com.example.productservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Response {
    private String timestamp;
    private String message;
    public Response(String message){
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        this.message = message;
    }
}
