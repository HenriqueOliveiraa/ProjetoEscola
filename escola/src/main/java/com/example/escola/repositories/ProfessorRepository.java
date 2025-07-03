package com.example.escola.repositories;

import com.example.escola.entities.Professores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professores, Long> {
    boolean existsByCpf(String cpf);
}
