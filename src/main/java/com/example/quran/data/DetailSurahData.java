package com.example.quran.data;

import com.example.quran.model.Ayah;
import lombok.Data;

import java.util.List;

@Data
public class DetailSurahData {
    private String id;
    private String audioUrl;
    private String number;
    private String surahName;
    private String translateId;
    private List<AyahData> ayahData;
}
