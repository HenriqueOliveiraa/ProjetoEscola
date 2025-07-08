package com.example.escola.dtos;

import lombok.Data;

@Data
public class AlunoResponseDTO {

    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private int idade;
    private String turmaNome;
}