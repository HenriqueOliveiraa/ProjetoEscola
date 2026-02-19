package com.example.escola.controllers;

import com.example.escola.dtos.TurmaRequestDTO;
import com.example.escola.dtos.TurmaResponseDTO;
import com.example.escola.entities.Usuario;
import com.example.escola.services.TurmaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO', 'PROFESSOR', 'ALUNO')")
    public ResponseEntity<List<TurmaResponseDTO>> getAllTurmas() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/minhas")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<TurmaResponseDTO>> getMinhasTurmas(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(service.findByProfessorId(usuario.getLinkedEntityId()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO')")
    public ResponseEntity<TurmaResponseDTO> createTurma(@RequestBody TurmaRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO')")
    public ResponseEntity<TurmaResponseDTO> updateTurma(@PathVariable Long id, @RequestBody TurmaRequestDTO dto) {
        try {
            return ResponseEntity.ok(service.update(id, dto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO')")
    public ResponseEntity<Void> deleteTurma(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
