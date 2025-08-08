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
import java.util.List;

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
    public String validate_JWT_Token(HttpServletRequest request) throws IllegalArgumentException, ExpiredJwtException {
        String token = null;
        if(request.getCookies() != null){
            for(Cookie value: Arrays.stream(request.getCookies()).toList()){
                if(value.getName().equals("Authorization")){
                    token=value.getValue();
                }
            }
        }
        else{
            throw new IllegalArgumentException("Brak ważnego tokena authorization");
        }
        jwtService.validateToken(token);
        return jwtService.getSubject(token);
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
        user.setRole(userRegisterDTO.getUserType());
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
                return ResponseEntity.ok(new AuthResponse("Success",user.getUsername(),user.getRole().toString(),user.getEmail()));
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
            String refresh = null;
            if(request.getCookies()==null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Brak ciasteczek"));
            }
            for (Cookie cookie : Arrays.stream(request.getCookies()).toList()) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
            if (refresh == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Brak refresh tokena"));
            }
            jwtService.validateToken(refresh);
            String login = jwtService.getSubject(refresh);
            User user = userRepository.findUserByUsername(login).orElse(null);
            if (user != null) {
                Cookie cookie = cookieService.generateCookie("Authorization", generate_JWT_Token(login,duration), duration);
                response.addCookie(cookie);
                return ResponseEntity.ok(new AuthResponse(
                        "Zalogowano tokenem refresh",
                        user.getUsername(),
                        user.getRole().toString(),
                        user.getEmail()
                ));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Nie ma takiego uzytkownika w bazie"));
    }


    public ResponseEntity<?> authorize(HttpServletRequest request, UserType userType) {
        try {
            String login = validate_JWT_Token(request);
            User user = userRepository.findUserByUsernameAndHasPremission(login, userType).orElse(null);
            if (user != null) {
                return validateAuthority(user,"Uzyskano dostęp","Nie ma takiego uzytkownika w bazie");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Brak odpowiedniego tokena"));
            }
        }
        catch (IllegalArgumentException | ExpiredJwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Brak odpowiedniego tokena lub token wygasł"));
        }
    }
    public ResponseEntity<?> validateAuthority(User user, String successMessage, String errorMessage){
        if (user != null) {
            return ResponseEntity.ok().body(new AuthResponse(successMessage,user.getUsername(),user.getRole().toString(),user.getEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(errorMessage));
        }
    }

    public ResponseEntity<?> updateUser(HttpServletRequest request, UserRegisterDTO userRegisterDTO, String uuid) {
        try{
            validate_JWT_Token(request);
        }
        catch(IllegalArgumentException | ExpiredJwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Token niewłaściwy lub wygasł"));
        }
        ResponseEntity<?> r = authorize(request,UserType.ADMIN);
        if(r.getStatusCode()!=HttpStatus.OK){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Nie uzyskano dostepu"));
        }
        int result = userRepository.updateUserByUuid(
                userRegisterDTO.getEmail(),
                userRegisterDTO.getUsername(),
                passwordEncoder.encode(userRegisterDTO.getPassword()),
                userRegisterDTO.getUserType(),
                uuid);
        System.out.println(result);
        if(result==1){
            return ResponseEntity.ok(new AuthResponse("Pomyślnie zaaktualizowano użytkownika",userRegisterDTO.getUsername(),userRegisterDTO.getUserType().toString(),userRegisterDTO.getEmail()));

        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie wykonano akutalizacji uzytkownika"));
        }
    }

    public ResponseEntity<?> deleteUser(HttpServletRequest request,String uuid) {
        try{
            validate_JWT_Token(request);
        }
        catch(IllegalArgumentException | ExpiredJwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Token niewłaściwy lub wygasł"));
        }
        ResponseEntity<?> r = authorize(request,UserType.ADMIN);
        if(r.getStatusCode()!=HttpStatus.OK){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Nie uzyskano dostepu"));
        }
        int result = userRepository.deleteUserByUuid(uuid);
        if(result==1){
            return ResponseEntity.ok(new Response("Pomyślnie usunieto użytkownika"));

        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie wykonano usuniecia uzytkownika"));
        }
    }

    public ResponseEntity<?> getUserInfo(HttpServletRequest request,String uuid) {
        try{
            validate_JWT_Token(request);
        }
        catch(IllegalArgumentException | ExpiredJwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Token niewłaściwy lub wygasł"));
        }
        ResponseEntity<?> r = authorize(request,UserType.ADMIN);
        if(r.getStatusCode()!=HttpStatus.OK){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Nie uzyskano dostepu"));
        }
        User user = userRepository.findUserByUuid(uuid).orElse(null);
        if(user!=null){
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany uuid"));
        }
    }

    public ResponseEntity<?> getUsersInfo(HttpServletRequest request) {
        try{
            System.out.println("POCZATEK");
            String u = validate_JWT_Token(request);
            System.out.println(u);
        }
        catch(IllegalArgumentException | ExpiredJwtException e){
            System.out.println("HERE1");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Token niewłaściwy lub wygasł"));
        }
        ResponseEntity<?> r = authorize(request,UserType.ADMIN);
        if(r.getStatusCode()!=HttpStatus.OK){
            System.out.println("HERE2"+r.getStatusCode());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Nie uzyskano dostepu"));
        }
        System.out.println("HERE3");
        List<User> listOfUsers = userRepository.findAll();
        if(listOfUsers!=null){
            return ResponseEntity.ok(listOfUsers);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Brak uzytkownikow w bazie"));
        }
    }

    public ResponseEntity<?> changeRole(HttpServletRequest request, String uuid, UserType userType) {
        try{
            validate_JWT_Token(request);
        }
        catch(IllegalArgumentException | ExpiredJwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Token niewłaściwy lub wygasł"));
        }
        ResponseEntity<?> r = authorize(request,UserType.ADMIN);
        if(r.getStatusCode()!=HttpStatus.OK){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Nie uzyskano dostepu"));
        }
        int result = userRepository.changeUserRole(uuid,userType);
        if(result==1){
            return ResponseEntity.ok(new Response("Pomyślnie zmieniono role użytkownika"));

        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie wykonano zmiany roli uzytkownika"));
        }
    }

}
