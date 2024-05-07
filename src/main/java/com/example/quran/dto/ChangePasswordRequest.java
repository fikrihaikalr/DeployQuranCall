package com.example.quran.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
        private String confirmNewPassword;

}
