package com.example.quran.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sub_materi")
public class Submateri {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String author;
    private String title;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "materi_id", nullable = false)
    private Materi materi;
    @Column (columnDefinition = "text")
    private String content;
}
