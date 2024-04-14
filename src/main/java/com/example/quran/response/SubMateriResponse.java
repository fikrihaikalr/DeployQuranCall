package com.example.quran.response;

import com.example.quran.data.SubMateriData;
import lombok.Data;

import java.util.List;

@Data
public class SubMateriResponse {
    private MessageResponse messageResponse;
    private List<SubMateriData> data;
}
