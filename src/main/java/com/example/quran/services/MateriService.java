package com.example.quran.services;

import com.example.quran.data.MateriData;
import com.example.quran.data.SubMateriData;
import com.example.quran.dto.MateriDTO;
import com.example.quran.model.Materi;
import com.example.quran.model.Submateri;
import com.example.quran.repository.MateriRepository;
import com.example.quran.response.MateriResponse;
import com.example.quran.response.MessageResponse;
import com.example.quran.response.SubMateriResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MateriService {
    @Autowired
    private MateriRepository materiRepository;

    public MateriResponse getAllMateri(){
        MateriResponse materiResponse = new MateriResponse();
        List<MateriData> materiDataList = new ArrayList<>();
        List<Materi> materiList = materiRepository.findAll();

        for (Materi materi : materiList) {
            MateriData materiData = new MateriData();
            materiData.setId(Long.toString(materi.getId()));
            materiData.setTitle(materi.getTitle());
            materiDataList.add(materiData);
        }

        materiResponse.setMessageResponse(new MessageResponse(false, "Get All Materi Success"));
        materiResponse.setData(materiDataList);

        return materiResponse;

    }

    public Materi getMateriById(Long id) {
        return materiRepository.findById(id).orElse(null);
    }

    public SubMateriResponse getSubMateriByMateriId(Long materiId) {
        SubMateriResponse subMateriResponse = new SubMateriResponse();
        List<SubMateriData> subMateriDataList = new ArrayList<>();

        // Mendapatkan materi berdasarkan ID
        Materi materi = getMateriById(materiId);

        if (materi != null) {
            // Jika materi ditemukan, ambil daftar submateri
            List<Submateri> subMateriList = materi.getSubmateriList();

            // Iterasi melalui daftar submateri dan konversi menjadi SubMateriData
            for (Submateri subMateri : subMateriList) {
                SubMateriData subMateriData = new SubMateriData();
                subMateriData.setId(Long.toString(subMateri.getId()));
                subMateriData.setAuthor(subMateri.getAuthor());
                subMateriData.setTitle(subMateri.getTitle());
                subMateriDataList.add(subMateriData);
            }

            subMateriResponse.setMessageResponse(new MessageResponse(false, "Get Sub Materi by Materi ID Success"));
            subMateriResponse.setData(subMateriDataList);
        } else {
            // Jika materi tidak ditemukan
            subMateriResponse.setMessageResponse(new MessageResponse(true, "Materi not found"));
            subMateriResponse.setData(Collections.emptyList());
        }

        return subMateriResponse;
    }


    public Materi createMateri(MateriDTO materiDTO){
        Materi materi = new Materi();
        materi.setTitle(materiDTO.getTitle());
//        MessageResponse messageResponse = new MessageResponse(false, "Materi Successfully Created!");
//        materiRepository.save(materi)
        return materiRepository.save(materi);
    }
}
