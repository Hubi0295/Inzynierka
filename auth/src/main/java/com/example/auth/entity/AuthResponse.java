package com.example.auth.entity;

import lombok.Getter;
import java.util.Date;

@Getter
public class AuthResponse extends Response {
    private String username;
    private String userRole;
    private String email;

    public AuthResponse(String message, String username, String userRole, String email) {
        super(message);
        this.username = username;
        this.userRole = userRole;
        this.email = email;
    }
}
