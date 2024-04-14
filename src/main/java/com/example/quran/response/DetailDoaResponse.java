package com.example.quran.response;

import com.example.quran.dto.DetailDoa;
import lombok.Data;

import java.util.List;

@Data
public class DetailDoaResponse {
    private MessageResponse messageResponse;
    private List<DetailDoa> doa;
}
