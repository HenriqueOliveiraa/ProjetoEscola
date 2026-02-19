package com.example.escola.repositories;

import com.example.escola.entities.Turmas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<Turmas, Long> {
    List<Turmas> findByProfessoresId(Long professorId);
}
