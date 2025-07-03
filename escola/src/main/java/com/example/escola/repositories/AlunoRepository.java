package com.example.escola.repositories;

import com.example.escola.entities.Alunos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Alunos, Long> {
    boolean existsByCpf(String cpf);
}
