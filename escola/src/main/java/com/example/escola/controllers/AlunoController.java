package com.example.escola.controllers;

import com.example.escola.dtos.AlunoRequestDTO;
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
    public ResponseEntity<AlunoResponseDTO> create (@RequestBody AlunoRequestDTO dto){
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public  ResponseEntity<List<AlunoResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> update(@PathVariable Long id, @RequestBody AlunoRequestDTO dto) {
        try {
            AlunoResponseDTO updatedAluno = service.update(id, dto);
            return ResponseEntity.ok(updatedAluno);
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
