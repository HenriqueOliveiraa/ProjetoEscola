package com.example.escola.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ProfessorResponseDTO {
    private Long id;
    private String nome;
    private String sobrenome;
    private String disciplina;
    private String cpf;
    private int idade;
    private List<TurmaNomeDTO> turmas;
}
