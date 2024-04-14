package com.example.quran.services;


import com.example.quran.data.AyahData;
import com.example.quran.data.SurahData;
import com.example.quran.model.Ayah;
import com.example.quran.model.Surah;
import com.example.quran.repository.AyahRepository;
import com.example.quran.repository.SurahRepository;
import com.example.quran.response.DetailSurahResponse;
import com.example.quran.response.MessageResponse;
import com.example.quran.response.SurahResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SurahServices {
    @Autowired
    SurahRepository surahRepository;

    @Autowired
    AyahRepository ayahRepository;

    public SurahResponse getSurah(){
        SurahResponse surahResponse = new SurahResponse();
        List<SurahData> surahDataList = new ArrayList<>();

        try {
            List<Surah> surahs = surahRepository.findAllByOrderByNumberAsc();
            for(Surah surahList : surahs){
                SurahData surahData = new SurahData();
                surahData.setId(Long.toString(surahList.getId()));
                surahData.setAudioUrl(surahList.getAudioUrl());
                surahData.setNumber(Integer.toString(surahList.getNumber()));
                surahData.setSurahName(surahList.getNameId());
                surahData.setTranslateId(surahList.getTranslationId());
                surahDataList.add(surahData);
            }

            surahResponse.setMessageResponse(new MessageResponse(false, "Surahs retrieved successfully"));
            surahResponse.setData(surahDataList);
        } catch (Exception e) {
            surahResponse.setMessageResponse(new MessageResponse(true, "Failed to retrieve Surahs: " + e.getMessage()));
            surahResponse.setData(new ArrayList<>());
        }

        return surahResponse;
    }


    public List<Surah> getSurahLike(String surahName){
        return surahRepository.findByFullTextSearch(surahName);
    }


    public DetailSurahResponse getSurahById(Long surahId) {
        DetailSurahResponse surahResponse = new DetailSurahResponse();
        List<AyahData> ayahDataList = new ArrayList<>();

        try {
            // Ambil detail Surah berdasarkan ID
            Surah surah = surahRepository.findById(surahId).orElse(null);
            if (surah != null) {
                SurahData surahData = new SurahData();
                surahData.setId(Long.toString(surah.getId()));
                surahData.setAudioUrl(surah.getAudioUrl());
                surahData.setNumber(Integer.toString(surah.getNumber()));
                surahData.setSurahName(surah.getNameId());
                surahData.setTranslateId(surah.getTranslationId());

                // Ambil daftar Ayah berdasarkan Surah
                List<Ayah> ayahList = ayahRepository.findBySurahId(surahId);
                for (Ayah ayah : ayahList) {
                    AyahData ayahData = new AyahData();
                    ayahData.setAyah(Long.toString(ayah.getAyah()));
                    ayahData.setLatin(ayah.getLatin());
                    ayahData.setText(ayah.getText());
                    ayahData.setArab(ayah.getArab());
                    ayahDataList.add(ayahData);
                }

                surahResponse.setMessageResponse(new MessageResponse(false, "Surah detail retrieved successfully"));
                surahResponse.setData(surahData);
            } else {
                surahResponse.setMessageResponse(new MessageResponse(true, "Surah with ID " + surahId + " not found"));
            }
        } catch (Exception e) {
            surahResponse.setMessageResponse(new MessageResponse(true, "Failed to retrieve Surah detail: " + e.getMessage()));
        }

        surahResponse.setAyah(ayahDataList);
        return surahResponse;
    }

}

