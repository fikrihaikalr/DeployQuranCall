package com.example.quran.dto;


import com.example.quran.model.Role;
import lombok.Data;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank(message = "Username kosong")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "Email Kosong")
    @Size(max = 50)
    @Email(message = "invalid, you must create email")
    private String email;

    @NotBlank(message = "Masukan Password")
    @Size(min = 8, max = 40, message = "Minimal 8 Karakter!")
    private String password;
    private Set<String> role;
}
