package com.example.auth.services;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public Cookie generateCookie(String name, String value, int duration){
        Cookie cookie = new Cookie(name,value);
        cookie.setMaxAge(duration);
        cookie.setHttpOnly(true);
        return cookie;
    }
    public Cookie removeCookie(Cookie[] cookies, String name){
        for(Cookie cookie: cookies){
            if (cookie.getName().equals(name)){
                cookie.setPath("/");
                cookie.setMaxAge(0);
                cookie.setHttpOnly(true);
                return cookie;
            }
        }
        return null;
    }
}
