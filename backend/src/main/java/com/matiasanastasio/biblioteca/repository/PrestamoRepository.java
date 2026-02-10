package com.matiasanastasio.biblioteca.repository;


import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.matiasanastasio.biblioteca.model.entity.Prestamo;
import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;

import jakarta.transaction.Transactional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long>, JpaSpecificationExecutor<Prestamo> {

    @Modifying
    @Transactional
    @Query("""
            update Prestamo p
            set p.estado = com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo.VENCIDO
            where p.estado = com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo.ACTIVO
            and p.fechaVencimiento < :hoy
            """)
    int marcarVencidos(@Param("hoy") LocalDate hoy);

    int countByUsuarioIdAndEstado(Long usuarioId, EstadoPrestamo estado);

    boolean existsByUsuarioIdAndEstado(Long usuarioId, EstadoPrestamo estado);
    
    boolean existsByLibroIdAndEstadoIn(Long libroId, java.util.Collection<EstadoPrestamo> estados);


}
