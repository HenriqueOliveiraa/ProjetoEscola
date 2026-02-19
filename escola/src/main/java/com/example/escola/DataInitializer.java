package com.example.escola;

import com.example.escola.entities.UserRole;
import com.example.escola.entities.Usuario;
import com.example.escola.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario(
                "admin",
                passwordEncoder.encode("admin123"),
                UserRole.ADMINMAX
            );
            usuarioRepository.save(admin);
            System.out.println("=== ADMINMAX user criado: admin / admin123 ===");
        }
    }
}
