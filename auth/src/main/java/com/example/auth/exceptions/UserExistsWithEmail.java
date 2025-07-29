package com.example.auth.exceptions;

public class UserExistsWithEmail extends RuntimeException{
    public UserExistsWithEmail(String message){
        super(message);
    }
    public UserExistsWithEmail(String message, Throwable cause){
        super(message, cause);
    }
    public UserExistsWithEmail(Throwable cause){
        super(cause);
    }
}
