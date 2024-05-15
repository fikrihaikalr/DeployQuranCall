package com.example.quran.controller;


import com.example.quran.model.Surah;
import com.example.quran.response.DetailSurahResponse;
import com.example.quran.response.SurahResponse;
import com.example.quran.services.SurahServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SurahController {
    @Autowired
    private SurahServices surahServices;

    @GetMapping("/surah")
    public ResponseEntity<SurahResponse> getAllSurah(){
        return ResponseEntity.ok(surahServices.getSurah());
    }

    @GetMapping("/surahName")
    public ResponseEntity<List<Surah>> getSurahLike(@RequestParam String surahName){
        return ResponseEntity.ok(surahServices.getSurahLike(surahName));
    }

//    @GetMapping("/all")
//    public ResponseEntity<?> surahGet(){
//        return ResponseEntity.ok(surahServices.getAllSurah());
//    }

    @GetMapping("/surah/{surahId}")
    public ResponseEntity<DetailSurahResponse> getSurahById(@PathVariable Long surahId) {
        return ResponseEntity.ok(surahServices.getSurahById(surahId));
    }
}
