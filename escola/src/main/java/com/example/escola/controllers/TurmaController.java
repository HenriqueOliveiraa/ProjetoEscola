package com.example.escola.controllers;

import com.example.escola.dtos.TurmaRequestDTO;
import com.example.escola.dtos.TurmaResponseDTO;
import com.example.escola.services.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaService service;

    @PostMapping
    public ResponseEntity<TurmaResponseDTO> create(@RequestBody TurmaRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<TurmaResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
