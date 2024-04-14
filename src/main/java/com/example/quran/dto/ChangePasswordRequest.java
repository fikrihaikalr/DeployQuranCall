package com.example.quran.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
