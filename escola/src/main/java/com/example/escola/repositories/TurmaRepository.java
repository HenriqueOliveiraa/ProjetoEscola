package com.example.escola.repositories;

import com.example.escola.entities.Turmas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turmas, Long> {
}
