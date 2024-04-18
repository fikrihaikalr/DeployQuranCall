package com.example.quran.data;

import com.example.quran.model.Ayah;
import lombok.Data;

import java.util.List;

@Data
public class DetailSurahData {
    private SurahData surahData;
    private List<AyahData> ayahData;
}
