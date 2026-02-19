package com.example.escola.controllers;

import com.example.escola.dtos.ProfessorRequestDTO;
import com.example.escola.dtos.ProfessorResponseDTO;
import com.example.escola.entities.Usuario;
import com.example.escola.repositories.ProfessorRepository;
import com.example.escola.services.ProfessorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository repository;

    @Autowired
    private ProfessorService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO')")
    public ResponseEntity<ProfessorResponseDTO> create(@Valid @RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO')")
    public ResponseEntity<List<ProfessorResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<ProfessorResponseDTO> getMyProfile(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        try {
            return ResponseEntity.ok(service.findById(usuario.getLinkedEntityId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINMAX', 'GESTAO')")
    public ResponseEntity<ProfessorResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProfessorRequestDTO dto) {
        try {
            ProfessorResponseDTO updatedProfessor = service.update(id, dto);
            return ResponseEntity.ok(updatedProfessor);
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
