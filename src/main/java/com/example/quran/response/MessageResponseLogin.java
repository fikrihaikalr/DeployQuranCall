package com.example.quran.response;

import lombok.Data;
import org.springframework.security.core.userdetails.User;

@Data
public class MessageResponseLogin {
    private Boolean error;
    private String message;
    private UserInfoResponse loginResult;

    public MessageResponseLogin(Boolean error, String message, UserInfoResponse loginResult) {
        this.error = error;
        this.message = message;
        this.loginResult = loginResult;
    }
}
