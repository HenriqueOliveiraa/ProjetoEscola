package com.example.escola.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_turmas")
@Data
public class Turmas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String ano;
    private String periodo;
    private Integer capacidadeMaxima;
    @ManyToMany
    @JoinTable(
            name = "tb_turmas_professores",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    private List<Professores> professores = new ArrayList<>();
    @OneToMany(mappedBy = "turma")
    private List<Alunos> alunos = new ArrayList<>();
}
