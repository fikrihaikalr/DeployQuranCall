package com.example.quran.repository;

import com.example.quran.model.Doa;
import com.example.quran.model.Surah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoaRepository extends JpaRepository<Doa, Long> {
    @Query(value = "SELECT * FROM Doa WHERE to_tsvector('simple', REPLACE(judul, '-', '')) @@ to_tsquery('simple', REPLACE(?1, '-', ''))", nativeQuery = true)
    List<Doa> findByFullTextSearch(String keyword);

    List<Doa> findByTypeDoaContainingIgnoreCase(String namaSurah);

    Doa findDoaByTypeDoa(String doa);

}
