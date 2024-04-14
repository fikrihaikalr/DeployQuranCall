package com.example.quran.services;

import com.example.quran.data.DoaData;
import com.example.quran.dto.DetailDoa;
import com.example.quran.model.Doa;
import com.example.quran.repository.DoaRepository;
import com.example.quran.response.DetailDoaResponse;
import com.example.quran.response.DoaResponse;
import com.example.quran.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoaService {
    @Autowired
    DoaRepository doaRepository;

    public DoaResponse getListDoa(){
        DoaResponse doaResponse = new DoaResponse();

        MessageResponse messageResponse = new MessageResponse(false, "Success");
        doaResponse.setMessageResponse(messageResponse);

        List<Doa> doaList = doaRepository.findAll();

        List<DoaData> dataList = new ArrayList<>();
        for (Doa doa : doaList) {
            DoaData doaData = new DoaData();
            doaData.setId(doa.getId().toString());
            doaData.setDoaName(doa.getTypeDoa());
            dataList.add(doaData);
        }
        doaResponse.setData(dataList);

        return doaResponse;
    }

    public DetailDoaResponse getDetailDoa(Long id){
        DetailDoaResponse detailDoaResponse = new DetailDoaResponse();
        List<DetailDoa> dataList = new ArrayList<>();

        // Menginisialisasi pesan respons
        MessageResponse messageResponse;

        // Mencari objek Doa berdasarkan ID
        Optional<Doa> optionalDoa = doaRepository.findById(id);

        // Memeriksa apakah objek Doa ditemukan
        if (optionalDoa.isPresent()) {
            Doa doa = optionalDoa.get();
            // Mengatur data doa ke dalam DoaData
            DetailDoa data = new DetailDoa();
            data.setId(Long.toString(doa.getId()));
            data.setDoaName(doa.getTypeDoa());
            data.setArabDoa(doa.getArabDoa());
            data.setTranslateDoa(doa.getTranslateDoa());
            // Menambahkan DoaData ke dalam dataList
            dataList.add(data);

            // Mengatur pesan respons ke "Success" jika objek Doa ditemukan
            messageResponse = new MessageResponse(false, "Success");
        } else {
            // Mengatur pesan respons ke "Doa not found" jika objek Doa tidak ditemukan
            messageResponse = new MessageResponse(true, "Doa not found");
        }

        // Mengatur pesan respons ke dalam DoaResponse
        detailDoaResponse.setMessageResponse(messageResponse);
        detailDoaResponse.setDoa(dataList);

        return detailDoaResponse;
    }

    public DoaResponse getDoaByTitle(String title){
        DoaResponse doaResponse = new DoaResponse();
        MessageResponse messageResponse = new MessageResponse(false, "Success");
        doaResponse.setMessageResponse(messageResponse);
        List<Doa> doaList = doaRepository.findByTypeDoaContainingIgnoreCase(title);
        List<DoaData> dataList = new ArrayList<>();
        for(Doa doa : doaList){
            DoaData doaData = new DoaData();
            doaData.setId(doa.getId().toString());
            doaData.setDoaName(doa.getTypeDoa());
            dataList.add(doaData);
        }
        doaResponse.setData(dataList);
        return doaResponse;
    }

//    public DoaResponse getDoaDetail(Long title){
//        DoaResponse doaResponses = new DoaResponse();
//        MessageResponse messageResponse = new MessageResponse(false, "Success");
//        doaResponses.setMessageResponse(messageResponse);
//        List<DoaData> dataList = new ArrayList<>();
//        for(Doa doa : doaList){
//            DoaData doaData = new DoaData();
//            doaData.setId(doa.getId());
//            doaData.setDoaName(doa.getTypeDoa());
//            doaData.setArabDoa(doa.getArabDoa());
//            doaData.setTranslateDoa(doa.getTranslateDoa());
//            dataList.add(doaData);
//
//        doaResponses.setData(dataList);
//        return doaResponses;
//    }
}
