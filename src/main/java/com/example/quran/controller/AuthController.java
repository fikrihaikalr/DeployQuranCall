package com.example.quran.controller;

import com.example.quran.dto.LoginRequest;
import com.example.quran.dto.SignupRequest;
import com.example.quran.response.AuthenticationResponse;
import com.example.quran.jwt.JwtUtils;
import com.example.quran.repository.RoleRepository;
import com.example.quran.repository.UsersRepository;
import com.example.quran.response.MessageResponse;
import com.example.quran.response.MessageResponseLogin;
import com.example.quran.response.UserInfoResponse;
import com.example.quran.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest loginRequest){
        try {
            AuthenticationResponse authenticationResponse = authService.loginUser(loginRequest);
            UserInfoResponse userInfoResponse = new UserInfoResponse(
                    authenticationResponse.getId(),
                    authenticationResponse.getEmail(),
                    authenticationResponse.getRoles(),
                    authenticationResponse.getJwt()
            );
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, authenticationResponse.getJwt())
                    .body(new MessageResponseLogin(false, "success", userInfoResponse));
        }catch (AuthenticationException e){
            return ResponseEntity.badRequest().body(new MessageResponseLogin(true, "Invalid email or password", null));
        }

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequest signUpRequest) {
        if(signUpRequest.getEmail() == null || signUpRequest.getUsername() == null || signUpRequest.getPassword() == null){
            return ResponseEntity.badRequest().body(new MessageResponse(true, "Harus Diisi!"));
        }
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse(false,"User Registered Successful"));
    }
}
