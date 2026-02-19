package com.example.escola.dtos;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AlunoRequestDTO {
    private String nome;
    private String sobrenome;

    @Pattern(
        regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$|^\\d{11}$",
        message = "CPF deve ter 11 dígitos ou estar no formato 000.000.000-00"
    )
    private String cpf;

    private int idade;
    private String email;

    @Pattern(
        regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$",
        message = "Telefone deve estar no formato (00) 00000-0000 ou (00) 0000-0000"
    )
    private String telefone;

    @Past(message = "Data de nascimento deve ser uma data no passado")
    private LocalDate dataNascimento;

    private String matricula;
    private Long turmaId;
}
