package com.example.escola.controllers;

import com.example.escola.dtos.AlunoRequestDTO;
import com.example.escola.dtos.AlunoResponseDTO;
import com.example.escola.dtos.ProfessorRequestDTO;
import com.example.escola.dtos.ProfessorResponseDTO;
import com.example.escola.repositories.ProfessorRepository;
import com.example.escola.services.ProfessorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProfessorResponseDTO> create (@RequestBody ProfessorRequestDTO dto){
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public  ResponseEntity<List<ProfessorResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> update(@PathVariable Long id, @RequestBody ProfessorRequestDTO dto) {
        try {
            ProfessorResponseDTO updatedProfessor = service.update(id, dto);
            return ResponseEntity.ok(updatedProfessor);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
