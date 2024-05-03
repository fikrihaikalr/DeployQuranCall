package com.example.quran.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ChangePasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
