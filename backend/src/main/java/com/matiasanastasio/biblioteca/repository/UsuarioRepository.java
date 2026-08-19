package com.matiasanastasio.biblioteca.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matiasanastasio.biblioteca.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
