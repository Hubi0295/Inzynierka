package com.example.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserInfoDTO {
    String Uuid;
    String name;
    String surname;
    String username;
    String email;
    UserType userType;
    boolean isEnabled;
    boolean isLock;
}
