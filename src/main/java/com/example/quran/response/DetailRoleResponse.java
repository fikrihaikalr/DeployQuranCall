package com.example.quran.response;

import com.example.quran.data.RoleData;
import lombok.Data;

@Data
public class DetailRoleResponse {
    private MessageResponse messageResponse;
    private RoleData data;
}
