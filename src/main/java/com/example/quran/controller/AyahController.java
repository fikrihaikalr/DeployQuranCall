package com.example.quran.controller;

import com.example.quran.model.Ayah;
import com.example.quran.response.AyahResponse;
import com.example.quran.response.MessageResponse;
import com.example.quran.services.AyahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AyahController {
    @Autowired
    AyahService ayahService;

    @GetMapping("/ayah/{surahNumber}")
    public ResponseEntity<AyahResponse> getBySurah(@PathVariable Integer surahNumber) {
//        AyahResponse ayahResponse = ayahService.getSurah(surahNumber);
        List<Ayah> ayahList = ayahService.getSurah(surahNumber);

        // Transform Ayah objects into Maps containing desired fields
        List<Map<String, Object>> transformedAyahs = ayahList.stream()
                .map(ayah -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("arab", ayah.getArab());
                    resultMap.put("ayah", ayah.getAyah());
                    resultMap.put("latin", ayah.getLatin());
                    resultMap.put("text", ayah.getText());
                    return resultMap;
                })

                .collect(Collectors.toList());

        return ResponseEntity.ok(new AyahResponse(new MessageResponse(false, "Sucsess"), transformedAyahs));
    }


}
