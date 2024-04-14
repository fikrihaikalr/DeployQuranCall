package com.example.quran.response;

import com.example.quran.data.DoaData;
import com.example.quran.model.Doa;
import lombok.Data;

import java.util.List;

@Data
public class DoaResponse {
    private MessageResponse messageResponse;
    private List<DoaData> data;
}
