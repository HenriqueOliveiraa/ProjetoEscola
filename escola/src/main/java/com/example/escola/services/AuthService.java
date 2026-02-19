package com.example.escola.services;

import com.example.escola.dtos.AuthResponseDTO;
import com.example.escola.dtos.LoginRequestDTO;
import com.example.escola.dtos.RegisterRequestDTO;
import com.example.escola.entities.Usuario;
import com.example.escola.repositories.UsuarioRepository;
import com.example.escola.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(usuario);
        return new AuthResponseDTO(token, usuario.getUsername(), usuario.getRole().name());
    }

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username já cadastrado");
        }

        Usuario usuario = new Usuario(
            dto.getUsername(),
            passwordEncoder.encode(dto.getPassword()),
            dto.getRole()
        );
        usuario.setLinkedEntityId(dto.getLinkedEntityId());
        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        return new AuthResponseDTO(token, usuario.getUsername(), usuario.getRole().name());
    }
}
