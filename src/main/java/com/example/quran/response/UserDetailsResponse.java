package com.example.quran.response;

import com.example.quran.model.Role;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDetailsResponse {
    private String id;
    private String username;
    private String email;
    private String password;
    private String photoPath;
    private List<String> roles;

//    public UserDetailsResponse(String id, String username, String email, String password, String photoPath, List<String> roles){
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.photoPath = photoPath;
//        this.roles = roles;
//    }

}
