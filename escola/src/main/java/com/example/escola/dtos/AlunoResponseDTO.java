package com.example.escola.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlunoResponseDTO {
    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private int idade;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private String matricula;
    private Long turmaId;
    private String turmaNome;
}
