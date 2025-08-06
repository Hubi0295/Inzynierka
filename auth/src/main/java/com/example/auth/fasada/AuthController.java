package com.example.auth.fasada;

import com.example.auth.entity.*;
import com.example.auth.exceptions.UserExistsWithEmail;
import com.example.auth.exceptions.UserExistsWithUsername;
import com.example.auth.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRegisterDTO user){
        userService.register(user);
        return ResponseEntity.ok().body(new AuthResponse("Dodano użytkownika",user.getUsername(),user.getUserType().toString(),user.getEmail()));
    }
    @RequestMapping(path="/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response){
        return userService.login(userLoginDTO,response);
    }
    @RequestMapping(path="/validate", method = RequestMethod.GET)
    public ResponseEntity<Response> validateToken(HttpServletRequest request){
        try{
            String userName = userService.validate_JWT_Token(request);
            return ResponseEntity.ok(new Response(userName));
        }
        catch (ExpiredJwtException | IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Brak tokena"));
        }

    }
    @RequestMapping(path="/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        return userService.logout(request,response);
    }
    @RequestMapping(path="/auto-login", method = RequestMethod.GET)
    public ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response){
        return userService.autoLogin(request,response);
    }
    @RequestMapping(path="/authorizeAdmin", method = RequestMethod.GET)
    public ResponseEntity<?> authorizeAdmin(HttpServletRequest request){
        return userService.authorize(request,UserType.ADMIN);
    }
    @RequestMapping(path="/authorizeSupervisor", method = RequestMethod.GET)
    public ResponseEntity<?> authorizeSupervisor(HttpServletRequest request){
        return userService.authorize(request,UserType.SUPERVISOR);
    }
}
