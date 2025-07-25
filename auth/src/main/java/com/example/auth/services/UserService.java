package com.example.auth.services;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }
    public String generate_JWT_Token(User user){
        return jwtService.generate_JWT_Token(user.getUsername());
    }
    public void validate_JWT_Token(String jwt_token){
        jwtService.validateToken(jwt_token);
    }
}
