package com.matiasanastasio.biblioteca.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.matiasanastasio.biblioteca.model.entity.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

}
