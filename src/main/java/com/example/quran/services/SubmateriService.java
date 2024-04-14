package com.example.quran.services;

import com.example.quran.dto.SubMateriDTO;
import com.example.quran.model.Materi;
import com.example.quran.model.Submateri;
import com.example.quran.repository.MateriRepository;
import com.example.quran.repository.SubmateriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmateriService {
    @Autowired
    private SubmateriRepository submateriRepository;

    @Autowired
    private MateriRepository materiRepository;

    public List<Submateri> getAllSubmateri(){
        return submateriRepository.findAll();
    }

    public Submateri getSubmateriByMateriId(Long subMateriId){
        return submateriRepository.findById(subMateriId).orElse(null);
    }

    public Optional<Submateri> getSubmateriById(Long id){
        Optional<Submateri> submateriOptional = submateriRepository.findById(id);
        return submateriOptional;
    }
    public Submateri createSubMateri(SubMateriDTO subMateriDTO){
        Materi materi = materiRepository.findById(subMateriDTO.getMateriId()).orElse(null);
        if(materi == null){
            throw new IllegalArgumentException("Materi With ID " +  subMateriDTO.getMateriId() + " not found");
        }

        Submateri submateri = new Submateri();
        submateri.setTitle(subMateriDTO.getTitle()); // Menggunakan nilai dari subMateriDTO
        submateri.setAuthor(subMateriDTO.getAuthor()); // Menggunakan nilai dari subMateriDTO
        submateri.setMateri(materi); // Mengatur materi untuk submateri
        submateri.setContent(subMateriDTO.getContent()); // Menggunakan nilai dari subMateriDTO

        return submateriRepository.save(submateri);

    }
}
