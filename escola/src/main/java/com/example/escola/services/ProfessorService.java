package com.example.escola.services;

import com.example.escola.dtos.ProfessorRequestDTO;
import com.example.escola.dtos.ProfessorResponseDTO;
import com.example.escola.entities.Professores;
import com.example.escola.repositories.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository repository;

    public ProfessorResponseDTO create (ProfessorRequestDTO dto){
        if (repository.existsByCpf(dto.getCpf())){
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        Professores professores = new Professores();

        professores.setNome(dto.getNome());
        professores.setSobrenome(dto.getSobrenome());
        professores.setDisciplina(dto.getDisciplina());
        professores.setCpf(dto.getCpf());
        professores.setIdade(dto.getIdade());
        Professores saved = repository.save(professores);
        return mapToResponseDTO(saved);
    }

    public List<ProfessorResponseDTO> findAll(){
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

    private ProfessorResponseDTO mapToResponseDTO(Professores professores) {

        ProfessorResponseDTO dto = new ProfessorResponseDTO();

        dto.setId(professores.getId());
        dto.setNome(professores.getNome());
        dto.setSobrenome(professores.getSobrenome());
        dto.setDisciplina(professores.getDisciplina());
        dto.setCpf(professores.getCpf());
        dto.setIdade(professores.getIdade());
        return dto;
    }
}
