package com.example.escola.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "tb_alunos")
public class Alunos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sobrenome;
    @Column(unique = true)
    private String cpf;
    private int idade;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private String matricula;
    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turmas turma;

}
