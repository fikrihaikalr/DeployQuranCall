package com.example.quran.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ChangePasswordRequest {
    private Long id;  // Include user ID for identification
    private String oldPassword;
    private String newPassword;

}
