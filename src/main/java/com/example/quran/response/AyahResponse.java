package com.example.quran.response;

import com.example.quran.model.Ayah;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class AyahResponse {
    private MessageResponse messageResponse;
    private List<Map<String, Object>> data;

    public AyahResponse(MessageResponse messageResponse, List<Map<String, Object>> data) {
        this.messageResponse= messageResponse;
        this.data = data;
    }

}

