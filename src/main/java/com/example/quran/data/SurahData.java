package com.example.quran.data;

import lombok.Data;

@Data
public class SurahData {
    private String id;
    private String audioUrl;
    private String number;
    private String surahName;
    private String translateId;
}
