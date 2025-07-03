package com.example.escola.controllers;

import com.example.escola.dtos.AlunoResponseDTO;
import com.example.escola.repositories.AlunoRepository;
import com.example.escola.services.AlunoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AlunoResponseDTO> create (@RequestBody AlunoResponseDTO dto){
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public  ResponseEntity<List<AlunoResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
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
