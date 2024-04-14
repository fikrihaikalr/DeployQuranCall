package com.example.quran.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ayah")
public class Ayah {
    private String arab;
    private Integer asbab;
    private String audio;
    private Integer ayah;
    private Integer hizb;
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "surah_id") // Kunci asing
    private Surah surahId; // Relasi dengan entitas Surah
    private Integer juz;
    private String latin;
    private String notes;
    private Integer page;
    private Integer surah;
    private String text;
    private Integer theme;
}
