package com.matiasanastasio.biblioteca.model.entity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;

class PrestamoTest {
    
    @Test
    void devolver_cuandoEstaActivo_pasaADevueltoYSeteaFechaDevolucion(){
        Prestamo prestamo = PrestamoBuilder.unPrestamo()
            .activo()
            .build();
        
        prestamo.devolver();

        assertEquals(prestamo.getEstado(), EstadoPrestamo.DEVUELTO);
        assertEquals(prestamo.getFechaDevolucion(), LocalDate.now());
    }
}
