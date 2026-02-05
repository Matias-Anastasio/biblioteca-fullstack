package com.matiasanastasio.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.matiasanastasio.biblioteca.model.entity.Libro;

public interface  LibroRepository extends JpaRepository<Libro, Long>, JpaSpecificationExecutor<Libro> {
    
    boolean existsByIsbn(String isbn);

    Optional<Libro> findByIsbn(String isbn);
}
