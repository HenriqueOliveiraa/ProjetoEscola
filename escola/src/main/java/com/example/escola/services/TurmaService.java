package com.example.escola.services;

import com.example.escola.dtos.TurmaRequestDTO;
import com.example.escola.dtos.TurmaResponseDTO;
import com.example.escola.entities.Alunos;
import com.example.escola.entities.Professores;
import com.example.escola.entities.Turmas;
import com.example.escola.repositories.ProfessorRepository;
import com.example.escola.repositories.TurmaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository repository;

    @Autowired
    private ProfessorRepository professorRepository;

    public TurmaResponseDTO create(TurmaRequestDTO dto) {
        Turmas turmas = new Turmas();
        turmas.setNome(dto.getNome());
        turmas.setAno(dto.getAno());
        turmas.setPeriodo(dto.getPeriodo());
        turmas.setCapacidadeMaxima(dto.getCapacidadeMaxima());

        if (dto.getProfessoresIds() != null && !dto.getProfessoresIds().isEmpty()) {
            List<Professores> professores = professorRepository.findAllById(dto.getProfessoresIds());
            turmas.setProfessores(professores);
        } else {
            turmas.setProfessores(new ArrayList<>());
        }

        Turmas saved = repository.save(turmas);
        return mapToResponseDTO(saved);
    }

    public List<TurmaResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public TurmaResponseDTO findById(Long id) {
        Turmas turma = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
        return mapToResponseDTO(turma);
    }

    public List<TurmaResponseDTO> findByProfessorId(Long professorId) {
        return repository.findByProfessoresId(professorId).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public TurmaResponseDTO update(Long id, TurmaRequestDTO dto) {
        Turmas turmas = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

        turmas.setNome(dto.getNome());
        turmas.setAno(dto.getAno());
        turmas.setPeriodo(dto.getPeriodo());
        turmas.setCapacidadeMaxima(dto.getCapacidadeMaxima());

        if (dto.getProfessoresIds() != null) {
            List<Professores> professores = professorRepository.findAllById(dto.getProfessoresIds());
            turmas.setProfessores(professores);
        }

        turmas = repository.save(turmas);
        return mapToResponseDTO(turmas);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Turma não encontrada");
        }
        repository.deleteById(id);
    }

    public TurmaResponseDTO mapToResponseDTO(Turmas turmas) {
        TurmaResponseDTO dto = new TurmaResponseDTO();
        dto.setId(turmas.getId());
        dto.setNome(turmas.getNome());
        dto.setAno(turmas.getAno());
        dto.setPeriodo(turmas.getPeriodo());
        dto.setCapacidadeMaxima(turmas.getCapacidadeMaxima());
        dto.setAlunosCount(turmas.getAlunos().size());

        List<String> nomesProfessores = turmas.getProfessores().stream()
                .map(Professores::getNome)
                .toList();
        dto.setProfessores(nomesProfessores);

        List<String> alunosDTO = turmas.getAlunos().stream()
                .map(Alunos::getNome)
                .toList();
        dto.setAlunos(alunosDTO);
        return dto;
    }
}
