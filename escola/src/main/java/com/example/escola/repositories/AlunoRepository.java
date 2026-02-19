package com.example.escola.repositories;

import com.example.escola.entities.Alunos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlunoRepository extends JpaRepository<Alunos, Long> {
    boolean existsByCpf(String cpf);

    @Query("SELECT COUNT(a) FROM Alunos a WHERE a.matricula LIKE :prefix%")
    long countByMatriculaStartingWith(String prefix);
}
