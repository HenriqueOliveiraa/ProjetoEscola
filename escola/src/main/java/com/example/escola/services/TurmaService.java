package com.example.escola.services;

import com.example.escola.dtos.TurmaRequestDTO;
import com.example.escola.dtos.TurmaResponseDTO;
import com.example.escola.entities.Professores;
import com.example.escola.entities.Turmas;
import com.example.escola.repositories.ProfessorRepository;
import com.example.escola.repositories.TurmaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public TurmaResponseDTO create(TurmaRequestDTO dto) {
        List<Professores> professores = professorRepository.findAllById(dto.getProfessoresIds());

        Turmas turmas = new Turmas();
        turmas.setNome(dto.getNome());
        turmas.setProfessores(professores);
        Turmas saved = turmaRepository.save(turmas);
        return mapToResponseDTO(saved);
    }

    public List<TurmaResponseDTO> findAll(){
        return  turmaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    public void delete(Long id) {
        if (!turmaRepository.existsById(id)) {
            throw new EntityNotFoundException("Id da turma não encontrado");
        }
        turmaRepository.deleteById(id);
    }

    private TurmaResponseDTO mapToResponseDTO(Turmas turmas) {
        TurmaResponseDTO dto = new TurmaResponseDTO();

        dto.setId(turmas.getId());
        dto.setNome(turmas.getNome());
        dto.setProfessores(
                turmas.getProfessores()
                        .stream()
                        .map(Professores::getNome)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}

