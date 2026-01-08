package com.matiasanastasio.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matiasanastasio.biblioteca.model.entity.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    
    Optional<Autor> findByNombre(String nombre);
}
