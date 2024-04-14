package com.example.quran.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.security.Principal;

@Entity
@Data
@NoArgsConstructor
@Table(name = "doa")
public class Doa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "arab",columnDefinition = "text")
    private String arabDoa;
    @Column(name = "indo",columnDefinition = "text")
    private String translateDoa;
    @Column(name = "judul", columnDefinition = "text")
    private String typeDoa;
    @Column(name = "source")
    private String source;
}
