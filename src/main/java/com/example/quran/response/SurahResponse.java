package com.example.quran.response;

import com.example.quran.data.SurahData;
import lombok.Data;

import java.util.List;

@Data
public class SurahResponse {
    private MessageResponse messageResponse;
    private List<SurahData> data;
}
