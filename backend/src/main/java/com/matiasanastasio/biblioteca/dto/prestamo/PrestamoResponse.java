package com.matiasanastasio.biblioteca.dto.prestamo;

import java.time.LocalDate;
import java.util.UUID;

import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrestamoResponse {
    private UUID id;

    private UUID usuarioId;
    private String usuarioNombre;

    private UUID libroId;
    private String libroTitulo;

    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;

    private EstadoPrestamo estado;
}
