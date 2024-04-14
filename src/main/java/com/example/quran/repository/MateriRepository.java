package com.example.quran.repository;

import com.example.quran.model.Materi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriRepository extends JpaRepository<Materi, Long> {
}
