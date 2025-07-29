package com.example.auth.exceptions;

public class UserExistsWithUsername extends RuntimeException{
    public UserExistsWithUsername(String message){
        super(message);
    }
    public UserExistsWithUsername(String message, Throwable cause){
        super(message, cause);
    }
    public UserExistsWithUsername(Throwable cause){
        super(cause);
    }
}
