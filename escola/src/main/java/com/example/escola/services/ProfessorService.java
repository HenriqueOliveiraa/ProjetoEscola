package com.example.escola.services;

import com.example.escola.dtos.*;
import com.example.escola.entities.Professores;
import com.example.escola.repositories.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository repository;

    private String gerarRegistro() {
        String prefix = "PROF" + Year.now().getValue();
        long count = repository.countByRegistroStartingWith(prefix);
        return String.format("%s%04d", prefix, count + 1);
    }

    public ProfessorResponseDTO create(ProfessorRequestDTO dto) {
        if (dto.getCpf() != null && repository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        Professores professores = new Professores();

        professores.setNome(dto.getNome());
        professores.setSobrenome(dto.getSobrenome());
        professores.setDisciplina(dto.getDisciplina());
        professores.setCpf(dto.getCpf());
        professores.setIdade(dto.getIdade());
        professores.setEmail(dto.getEmail());
        professores.setTelefone(dto.getTelefone());
        professores.setRegistro(gerarRegistro());

        Professores saved = repository.save(professores);
        return mapToResponseDTO(saved);
    }

    public List<ProfessorResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProfessorResponseDTO findById(Long id) {
        Professores professor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));
        return mapToResponseDTO(professor);
    }

    public ProfessorResponseDTO update(Long id, ProfessorRequestDTO dto) {
        Professores professores = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

        professores.setNome(dto.getNome());
        professores.setSobrenome(dto.getSobrenome());
        professores.setDisciplina(dto.getDisciplina());
        professores.setIdade(dto.getIdade());
        professores.setCpf(dto.getCpf());
        professores.setEmail(dto.getEmail());
        professores.setTelefone(dto.getTelefone());
        // registro NÃO é alterado no update

        professores = repository.save(professores);
        return mapToResponseDTO(professores);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Professor não encontrado");
        }
        repository.deleteById(id);
    }

    public ProfessorResponseDTO mapToResponseDTO(Professores professores) {
        ProfessorResponseDTO dto = new ProfessorResponseDTO();

        dto.setId(professores.getId());
        dto.setNome(professores.getNome());
        dto.setSobrenome(professores.getSobrenome());
        dto.setDisciplina(professores.getDisciplina());
        dto.setCpf(professores.getCpf());
        dto.setIdade(professores.getIdade());
        dto.setEmail(professores.getEmail());
        dto.setTelefone(professores.getTelefone());
        dto.setRegistro(professores.getRegistro());

        List<TurmaNomeDTO> turmas = professores.getTurmas()
                .stream()
                .map(t -> new TurmaNomeDTO(t.getNome()))
                .toList();

        dto.setTurmas(turmas);
        return dto;
    }
}
