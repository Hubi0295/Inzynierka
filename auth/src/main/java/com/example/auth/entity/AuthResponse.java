package com.example.auth.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;
@Getter
public class AuthResponse {
    private String timestamp;
    private String message;
    private String username;
    private String userRole;
    private String email;

    public AuthResponse(String timestamp, String message, String username, String userRole, String email) {
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        this.message = message;
        this.username = username;
        this.userRole = userRole;
        this.email = email;
    }
}
