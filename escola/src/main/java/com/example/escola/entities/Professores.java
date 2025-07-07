package com.example.escola.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_professores")
@Data
public class Professores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sobrenome;
    private String disciplina;
    @Column(unique = true)
    private String cpf;
    private int idade;
    @ManyToMany(mappedBy = "professores")
    private List<Turmas> turmas = new ArrayList<>();
}
