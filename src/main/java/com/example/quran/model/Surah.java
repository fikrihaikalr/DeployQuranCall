package com.example.quran.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "surah")
public class Surah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "audio_url")
    private String audioUrl;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "name_id")
    private String nameId;
    @Column(name = "name_long")
    private String nameLong;
    @Column(name = "name_short")
    private String nameShort;
    @Column(name = "number")
    private int number;
    @Column(name = "number_of_verses")
    private int numberOfVerses;
    @Column(name = "revelation")
    private String revelation;
    @Column(name = "revelation_en")
    private String revelationEn;
    @Column(name = "revelation_id")
    private String revelationId;
    @Column(name = "sequence")
    private int sequence;
    @Column(name = "tafsir", columnDefinition = "text")
    private String tafsir;
    @Column(name = "translation_en", columnDefinition = "text")
    private String translationEn;
    @Column(name = "translation_id")
    private String translationId;

    @OneToMany(mappedBy = "surah", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ayah> ayahList; // Relasi dengan entitas Ayah


    // Getters and setters
}
