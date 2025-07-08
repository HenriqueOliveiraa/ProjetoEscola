package com.example.escola.services;

import com.example.escola.dtos.TurmaRequestDTO;
import com.example.escola.dtos.TurmaResponseDTO;
import com.example.escola.entities.Alunos;
import com.example.escola.entities.Professores;
import com.example.escola.entities.Turmas;
import com.example.escola.repositories.ProfessorRepository;
import com.example.escola.repositories.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository repository;

    @Autowired
    private ProfessorRepository professorRepository;

    public TurmaResponseDTO create(TurmaRequestDTO dto) {
        List<Professores> professores = professorRepository.findAllById(dto.getProfessoresIds());

        Turmas turmas = new Turmas();
        turmas.setNome(dto.getNome());
        turmas.setProfessores(professores);
        Turmas saved = repository.save(turmas);
        return mapToResponseDTO(saved);
    }

    public List<TurmaResponseDTO> findAll() {
        List<Turmas> turmas = repository.findAll();

        return turmas.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private TurmaResponseDTO mapToResponseDTO(Turmas turmas) {
        TurmaResponseDTO dto = new TurmaResponseDTO();
        dto.setId(turmas.getId());
        dto.setNome(turmas.getNome());

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