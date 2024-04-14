package com.example.quran.response;

import com.example.quran.data.RoleData;
import lombok.Data;

import java.util.List;

@Data
public class RoleResponse {
    private MessageResponse messageResponse;
    private List<RoleData> data;
}
