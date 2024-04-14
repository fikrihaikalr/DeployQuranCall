package com.example.quran.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "materi")
public class Materi {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @OneToMany(mappedBy = "materi", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Submateri> submateriList;
}
