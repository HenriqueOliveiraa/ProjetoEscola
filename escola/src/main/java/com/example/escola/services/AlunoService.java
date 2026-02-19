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

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private TurmaRepository turmaRepository;

    private String gerarMatricula() {
        String prefix = "ALU" + Year.now().getValue();
        long count = repository.countByMatriculaStartingWith(prefix);
        return String.format("%s%04d", prefix, count + 1);
    }

    public AlunoResponseDTO create(AlunoRequestDTO dto) {
        if (dto.getCpf() != null && repository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        Alunos aluno = new Alunos();

        aluno.setNome(dto.getNome());
        aluno.setSobrenome(dto.getSobrenome());
        aluno.setCpf(dto.getCpf());
        aluno.setIdade(dto.getIdade());
        aluno.setEmail(dto.getEmail());
        aluno.setTelefone(dto.getTelefone());
        aluno.setDataNascimento(dto.getDataNascimento());
        aluno.setMatricula(gerarMatricula());

        if (dto.getTurmaId() != null) {
            Turmas turma = turmaRepository.findById(dto.getTurmaId())
                    .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
            aluno.setTurma(turma);
        }

        Alunos saved = repository.save(aluno);
        return mapToResponseDTO(saved);
    }

    public List<AlunoResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AlunoResponseDTO findById(Long id) {
        Alunos aluno = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
        return mapToResponseDTO(aluno);
    }

    public AlunoResponseDTO update(Long id, AlunoRequestDTO dto) {
        Alunos aluno = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        aluno.setNome(dto.getNome());
        aluno.setSobrenome(dto.getSobrenome());
        aluno.setCpf(dto.getCpf());
        aluno.setIdade(dto.getIdade());
        aluno.setEmail(dto.getEmail());
        aluno.setTelefone(dto.getTelefone());
        aluno.setDataNascimento(dto.getDataNascimento());
        // matrícula NÃO é alterada no update

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

    public AlunoResponseDTO mapToResponseDTO(Alunos alunos) {
        AlunoResponseDTO dto = new AlunoResponseDTO();
        dto.setId(alunos.getId());
        dto.setNome(alunos.getNome());
        dto.setSobrenome(alunos.getSobrenome());
        dto.setCpf(alunos.getCpf());
        dto.setIdade(alunos.getIdade());
        dto.setEmail(alunos.getEmail());
        dto.setTelefone(alunos.getTelefone());
        dto.setDataNascimento(alunos.getDataNascimento());
        dto.setMatricula(alunos.getMatricula());

        if (alunos.getTurma() != null) {
            dto.setTurmaId(alunos.getTurma().getId());
            dto.setTurmaNome(alunos.getTurma().getNome());
        }
        return dto;
    }
}
