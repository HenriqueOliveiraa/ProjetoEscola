package com.example.escola.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TurmaRequestDTO {
    private String nome;
    private List<Long> professoresIds;
}
