package com.example.quran.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Email(message = "Invalid your email")
    private String email;

    @NotBlank
    private String password;
}
