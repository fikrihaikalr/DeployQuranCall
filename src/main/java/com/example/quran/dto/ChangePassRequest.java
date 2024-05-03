package com.example.quran.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePassRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
