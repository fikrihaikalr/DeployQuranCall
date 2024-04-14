package com.example.quran.response;

import lombok.Data;

@Data
public class MessageResponse {
    private Boolean error;
    private String message;

    public MessageResponse(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }
}
