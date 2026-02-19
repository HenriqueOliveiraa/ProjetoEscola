package com.example.escola.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TurmaRequestDTO {
    private String nome;
    private String ano;
    private String periodo;
    private Integer capacidadeMaxima;
    private List<Long> professoresIds;
}
