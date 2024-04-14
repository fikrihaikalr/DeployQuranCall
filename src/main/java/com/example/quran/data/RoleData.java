package com.example.quran.data;

import lombok.Data;

import java.util.List;

@Data
public class RoleData {
    private String id;
    private String username;
    private String email;
    private String photoPath;
    private String numberPhone;
    private String status;
    private List<String> roles;
}
