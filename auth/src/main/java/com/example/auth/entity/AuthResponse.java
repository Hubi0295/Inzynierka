package com.example.auth.entity;

import lombok.Getter;

@Getter
public class AuthResponse extends Response {
    private String name;
    private String surname;
    private String username;
    private String userType;
    private String email;

    public AuthResponse(String message, String name, String surname,String username, String userType, String email) {
        super(message);
        this.name=name;
        this.surname=surname;
        this.username = username;
        this.userType = userType;
        this.email = email;
    }
}
