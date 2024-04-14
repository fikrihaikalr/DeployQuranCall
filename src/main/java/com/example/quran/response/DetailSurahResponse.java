package com.example.quran.response;

import com.example.quran.data.AyahData;
import com.example.quran.data.SurahData;
import lombok.Data;

import java.util.List;

@Data
public class DetailSurahResponse {
    private MessageResponse messageResponse;
    private SurahData data;
    private List<AyahData> ayah;
}
