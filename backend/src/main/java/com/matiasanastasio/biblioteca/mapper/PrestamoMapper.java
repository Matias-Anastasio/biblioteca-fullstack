package com.matiasanastasio.biblioteca.mapper;

import com.matiasanastasio.biblioteca.dto.prestamo.PrestamoResponse;
import com.matiasanastasio.biblioteca.model.entity.Prestamo;

public class PrestamoMapper {
    
    public static PrestamoResponse toResponse(Prestamo prestamo){
        return new PrestamoResponse(
            prestamo.getId(),
            prestamo.getUsuario().getId(),
            prestamo.getUsuario().getNombre(),
            prestamo.getLibro().getId(),
            prestamo.getLibro().getTitulo(),
            prestamo.getFechaPrestamo(),
            prestamo.getFechaVencimiento(),
            prestamo.getFechaDevolucion(),
            prestamo.getEstado()
        );
    }
}
