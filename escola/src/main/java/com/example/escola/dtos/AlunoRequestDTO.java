package com.example.escola.dtos;

import lombok.Data;

@Data
public class AlunoRequestDTO {
    private String nome;
    private String sobrenome;
    private String cpf;
    private int idade;
    private Long turmaId;
}