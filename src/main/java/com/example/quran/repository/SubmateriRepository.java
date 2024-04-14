package com.example.quran.repository;

import com.example.quran.model.Submateri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmateriRepository extends JpaRepository<Submateri, Long> {
    List<Submateri> findByMateriId(Long id);


}
