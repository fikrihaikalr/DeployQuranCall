package com.example.quran.response;

import com.example.quran.data.MateriData;
import lombok.Data;

import java.util.List;

@Data
public class MateriResponse {
    private MessageResponse messageResponse;
    private List<MateriData> data;
}
