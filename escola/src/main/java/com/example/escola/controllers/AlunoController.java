package com.example.escola.controllers;

import com.example.escola.dtos.AlunoRequestDTO;
import com.example.escola.dtos.AlunoResponseDTO;
import com.example.escola.entities.Usuario;
import com.example.escola.repositories.AlunoRepository;
import com.example.escola.services.AlunoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private AlunoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO')")
    public ResponseEntity<AlunoResponseDTO> create(@Valid @RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO', 'PROFESSOR')")
    public ResponseEntity<List<AlunoResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<AlunoResponseDTO> getMyProfile(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        try {
            return ResponseEntity.ok(service.findById(usuario.getLinkedEntityId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO')")
    public ResponseEntity<AlunoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AlunoRequestDTO dto) {
        try {
            AlunoResponseDTO updatedAluno = service.update(id, dto);
            return ResponseEntity.ok(updatedAluno);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
