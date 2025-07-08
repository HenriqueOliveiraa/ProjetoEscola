package com.example.escola.services;

import com.example.escola.dtos.AlunoRequestDTO;
import com.example.escola.dtos.AlunoResponseDTO;
import com.example.escola.entities.Alunos;
import com.example.escola.entities.Turmas;
import com.example.escola.repositories.AlunoRepository;
import com.example.escola.repositories.TurmaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private TurmaRepository turmaRepository;

    public AlunoResponseDTO create(AlunoRequestDTO dto) {
        if (repository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        Alunos aluno = new Alunos();

        aluno.setNome(dto.getNome());
        aluno.setSobrenome(dto.getSobrenome());
        aluno.setCpf(dto.getCpf());
        aluno.setIdade(dto.getIdade());
        Turmas turma = turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
        aluno.setTurma(turma);
        Alunos saved = repository.save(aluno);
        return mapToResponseDTO(saved);
    }

    public List<AlunoResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AlunoResponseDTO update(Long id, AlunoRequestDTO dto) {
        Alunos aluno = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        aluno.setNome(dto.getNome());
        aluno.setSobrenome(dto.getSobrenome());
        aluno.setCpf(dto.getCpf());
        aluno.setIdade(dto.getIdade());

        if (dto.getTurmaId() != null) {
            Turmas turma = turmaRepository.findById(dto.getTurmaId())
                    .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
            aluno.setTurma(turma);
        } else {
            aluno.setTurma(null);
        }

        aluno = repository.save(aluno);

        return mapToResponseDTO(aluno);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Id do aluno não encontrado");
        }
        repository.deleteById(id);
    }

    private AlunoResponseDTO mapToResponseDTO(Alunos alunos) {

        AlunoResponseDTO dto = new AlunoResponseDTO();
        dto.setId(alunos.getId());
        dto.setNome(alunos.getNome());
        dto.setSobrenome(alunos.getSobrenome());
        dto.setCpf(alunos.getCpf());
        dto.setIdade(alunos.getIdade());

        if (alunos.getTurma() != null) {
            dto.setTurmaNome(alunos.getTurma().getNome());
        }
        return dto;
    }
}
