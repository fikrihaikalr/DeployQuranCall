package com.example.quran.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AuthenticationResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String jwt;

    public AuthenticationResponse(Long id, String username, String email, List<String> roles, String jwt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.jwt = jwt;
    }
}
