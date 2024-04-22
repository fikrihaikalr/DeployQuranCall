package com.example.quran.response;

import com.example.quran.data.SubMateriData;
import lombok.Builder;
import lombok.Data;

@Data
public class DetailSubMateriResponse {
    private MessageResponse messageResponse;
    private SubMateriData data;
}
