package com.example.auth.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class UserRegisterDTO {
    @NotBlank(message = "Email jest wymagany")
    @Email
    private String email;
    @NotBlank(message = "name jest wymagany")
    private String name;
    @NotBlank(message = "surname jest wymagany")
    private String surname;
    @NotBlank(message = "Username jest wymagany")
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank(message = "Hasło jest wymagane")
    @Length(min = 10 ,max = 50, message = "Haslo powinno mieć pomiędzy 10 a 50 znaków")
    private String password;
    @NotNull(message = "Rola użytkownika jest wymagana")
    private UserType userType;

}
