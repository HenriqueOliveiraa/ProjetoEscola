package com.example.escola.dtos;

import lombok.Data;
import java.util.List;

@Data
public class TurmaResponseDTO {
    private Long id;
    private String nome;
    private String ano;
    private String periodo;
    private Integer capacidadeMaxima;
    private int alunosCount;
    private List<String> professores;
    private List<String> alunos;
}
