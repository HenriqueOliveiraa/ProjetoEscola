package com.example.escola.repositories;

import com.example.escola.entities.Professores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfessorRepository extends JpaRepository<Professores, Long> {
    boolean existsByCpf(String cpf);

    @Query("SELECT COUNT(p) FROM Professores p WHERE p.registro LIKE :prefix%")
    long countByRegistroStartingWith(String prefix);
}
