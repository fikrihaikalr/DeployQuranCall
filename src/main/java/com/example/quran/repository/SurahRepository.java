package com.example.quran.repository;

import com.example.quran.model.Surah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurahRepository extends JpaRepository<Surah, Long> {
    List<Surah> findAllByOrderByNumberAsc();
    List<Surah> findByNameIdContainingIgnoreCase(String namaSurah);

    @Query(value = "SELECT * FROM Surah WHERE to_tsvector('simple', REPLACE(name_id, '-', '')) @@ to_tsquery('simple', REPLACE(?1, '-', ''))", nativeQuery = true)
    List<Surah> findByFullTextSearch(String keyword);
}

