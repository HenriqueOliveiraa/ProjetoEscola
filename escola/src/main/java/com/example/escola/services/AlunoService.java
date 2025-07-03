package com.example.escola.services;

import com.example.escola.dtos.AlunoResponseDTO;
import com.example.escola.entities.Alunos;
import com.example.escola.repositories.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    public AlunoResponseDTO create(AlunoResponseDTO dto){
        if (repository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        Alunos aluno = new Alunos();

        aluno.setNome(dto.getNome());
        aluno.setSobrenome(dto.getSobrenome());
        aluno.setCpf(dto.getCpf());
        aluno.setIdade(dto.getIdade());
        Alunos saved = repository.save(aluno);
        return mapToResponseDTO(saved);
    }

    public List<AlunoResponseDTO> findAll(){
        return  repository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Carro não encontrado");
        }
        repository.deleteById(id);
    }

    private AlunoResponseDTO mapToResponseDTO(Alunos alunos){

        AlunoResponseDTO dto = new AlunoResponseDTO();
        dto.setId(alunos.getId());
        dto.setNome(alunos.getNome());
        dto.setSobrenome(alunos.getSobrenome());
        dto.setCpf(alunos.getCpf());
        dto.setIdade(alunos.getIdade());
        return dto;
    }
}
