package com.example.auth.entity;

import lombok.Getter;

@Getter
public class AuthResponse extends Response {
    private String username;
    private String userType;
    private String email;

    public AuthResponse(String message, String username, String userType, String email) {
        super(message);
        this.username = username;
        this.userType = userType;
        this.email = email;
    }
}
