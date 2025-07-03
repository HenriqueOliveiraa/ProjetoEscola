package com.example.escola.dtos;

import lombok.Data;

@Data
public class ProfessorRequestDTO {
    private String nome;
    private String sobrenome;
    private String disciplina;
    private String cpf;
    private int idade;
}
