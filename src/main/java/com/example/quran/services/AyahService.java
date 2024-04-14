package com.example.quran.services;

import com.example.quran.model.Ayah;
import com.example.quran.repository.AyahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AyahService {
    @Autowired
    AyahRepository ayahRepository;

    public List<Ayah> getSurah(Integer surahNumber){
        return ayahRepository.findBySurahOrderByAyahAsc(surahNumber);
    }
}
