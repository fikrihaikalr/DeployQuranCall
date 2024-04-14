package com.example.quran.repository;

import com.example.quran.model.Ayah;
import com.example.quran.model.Surah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AyahRepository extends JpaRepository<Ayah, Long> {
    List<Ayah> findBySurahOrderByAyahAsc(Integer surahNumber);

    @Query("SELECT a FROM Ayah a WHERE a.surahId.id = :surahId ORDER BY a.id asc")
    List<Ayah> findBySurahId(Long surahId);
}
