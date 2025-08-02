package com.example.auth.services;

import com.example.auth.entity.*;
import com.example.auth.exceptions.UserExistsWithEmail;
import com.example.auth.exceptions.UserExistsWithUsername;
import com.example.auth.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    @Value("${jwt.duration}")
    private int duration;
    @Value("${jwt.refreshDuration}")
    private int refreshDuration;
    private User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }
    public String generate_JWT_Token(String username, int duration){

        return jwtService.generate_JWT_Token(username,duration);
    }
    public String validate_JWT_Token(HttpServletRequest request, HttpServletResponse response) throws ExpiredJwtException, IllegalArgumentException {
        String token = null;
        String refresh = null;
        if(request.getCookies() != null){
            for(Cookie value: Arrays.stream(request.getCookies()).toList()){
                if(value.getName().equals("Authorization")){
                    token=value.getValue();
                }
                else if(value.getName().equals("refresh")){
                    refresh=value.getValue();
                    System.out.println("Tutaj 1");
                }
            }
        }
        else{
            System.out.println("Tutaj 2");
            throw new IllegalArgumentException("Brak tokena");
        }
        try{
            System.out.println("Tutaj 3");
            jwtService.validateToken(token);
            System.out.println("Tutaj 4");
            return jwtService.getSubject(token);
        }
        catch (IllegalArgumentException | ExpiredJwtException e){
            System.out.println("Tutaj 5");
            jwtService.validateToken(refresh);
            Cookie cookie = cookieService.generateCookie("Authorization", jwtService.refreshToken(refresh, duration), duration);
            Cookie refreshCookie = cookieService.generateCookie("refresh", jwtService.refreshToken(refresh,refreshDuration), refreshDuration);
            response.addCookie(cookie);
            response.addCookie(refreshCookie);
            System.out.println("Tutaj 6");
            System.out.println(jwtService.getSubject(cookie.getValue()));
            return jwtService.getSubject(cookie.getValue());
        }
    }

    public void register(UserRegisterDTO userRegisterDTO) {
        userRepository.findUserByUsername(userRegisterDTO.getUsername()).ifPresent(value->{
            throw new UserExistsWithUsername("Użytkownik z tą nazwą już istnieje w bazie danych");
        });
        userRepository.findUserByEmail(userRegisterDTO.getEmail()).ifPresent(value->{
            throw new UserExistsWithEmail("Użytkownik o takim mailu już istnieje w bazie danych");
        });
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(userRegisterDTO.getPassword());
        user.setEmail(userRegisterDTO.getEmail());
        user.setRole(userRegisterDTO.getUserRole());
        user.setEnable(true);
        user.setLock(false);
        user.generateUuid();
        saveUser(user);
    }
    public ResponseEntity<?> login(UserLoginDTO userProvided, HttpServletResponse response){
        User user = userRepository.findUserByUsername(userProvided.getUsername()).orElse(null);
        if(user!=null){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userProvided.getUsername(), userProvided.getPassword()));
            if(authentication.isAuthenticated()) {
                Cookie cookie = cookieService.generateCookie("Authorization", generate_JWT_Token(userProvided.getUsername(),duration), duration);
                Cookie refreshCookie = cookieService.generateCookie("refresh", generate_JWT_Token(userProvided.getUsername(), refreshDuration), refreshDuration);
                response.addCookie(cookie);
                response.addCookie(refreshCookie);
                return ResponseEntity.ok(
                        UserRegisterDTO
                                .builder()
                                .username(user.getUsername())
                                .userRole(user.getRole())
                                .email(user.getEmail())
                                .build());
            }
            else{
                return ResponseEntity.ok(new Response("Nie udało się zalogować, niepoprawne dane"));
            }
        }
        else{
            return ResponseEntity.ok(new Response("Brak takiego użytkownika w bazie danych"));
        }
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie1 = cookieService.removeCookie(request.getCookies(),"Authorization");
        Cookie cookie2 = cookieService.removeCookie(request.getCookies(),"refresh");
        if(cookie1 != null){
            response.addCookie(cookie1);
        }
        if(cookie2 != null){
            response.addCookie(cookie2);
        }
        return ResponseEntity.ok(new Response("Pomyślnie wylogowano"));
    }

    public ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response) {
    try{
        validate_JWT_Token(request,response);
        String refresh= null;
        for(Cookie cookie: Arrays.stream(request.getCookies()).toList()){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }
        String login = jwtService.getSubject(refresh);
        User user = userRepository.findUserByUsername(login).orElse(null);
        if(user != null){
            return ResponseEntity.ok(new AuthResponse(
                    "Zalogowano tokenem refresh",
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().toString()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Nie ma takiego uzytkownika w bazie"));

    }
    catch (ExpiredJwtException | IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Token refresh przestał być ważny"));
    }
    }

    public ResponseEntity<?> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        try{
            validate_JWT_Token(request,response);
            return ResponseEntity.ok(new Response("true"));
        }
        catch (ExpiredJwtException | IllegalArgumentException exception){
            return ResponseEntity.ok(new Response("false"));
        }
    }

    public ResponseEntity<?> authorize(HttpServletRequest request, HttpServletResponse response) {
        try {
            validate_JWT_Token(request, response);
            String token = null;
            String refresh = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : Arrays.stream(request.getCookies()).toList()) {
                    if (cookie.getName().equals("Authorization")) {
                        token = cookie.getValue();
                    } else if (cookie.getName().equals("refresh")) {
                        refresh = cookie.getValue();
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Token nie istnieje"));
            }
            if (token != null && !token.isEmpty()) {
                String username = jwtService.getSubject(token);
                User user = userRepository.findUserByUsernameAndIsAdmin(username).orElse(null);
                if (user != null) {
                    return ResponseEntity.ok(new Response("Witamy adminie"));
                }
            }
            else if (refresh != null && !refresh.isEmpty()) {
                String username = jwtService.getSubject(refresh);
                User user = userRepository.findUserByUsernameAndIsAdmin(username).orElse(null);
                if (user != null) {
                    return ResponseEntity.ok(new Response("Witamy adminie"));
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Brak tokena admina"));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Uzytkownik nie istenieje w bazie"));
        }
        catch (ExpiredJwtException | IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Brak tokena lub token nie wazny"));
        }
    }
}
