package com.matiasanastasio.biblioteca.dto.prestamo;

import java.time.LocalDate;

import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrestamoResponse {
    private Long id;

    private Long usuarioId;
    private String usuarioNombre;

    private Long libroId;
    private String libroTitulo;

    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;

    private EstadoPrestamo estado;
}
