package com.matiasanastasio.biblioteca.model.entity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;

class PrestamoTest {

    @Test
    void devolver_cuandoEstaActivo_pasaADevueltoYSeteaFechaDevolucion() {
        Prestamo prestamo = PrestamoBuilder.unPrestamo()
                .conFechaPrestamo(LocalDate.now())
                .activo()
                .build();

        prestamo.devolver();

        assertEquals(EstadoPrestamo.DEVUELTO, prestamo.getEstado());
        assertNotNull(prestamo.getFechaDevolucion());
        assertEquals(LocalDate.now(), prestamo.getFechaDevolucion());
    }

    @Test
    void devolver_cuandoEstaDevuelto_lanzaIllegalState() {
        Prestamo prestamo = PrestamoBuilder.unPrestamo()
                .devuelto()
                .build();

        assertThrows(IllegalStateException.class, () -> prestamo.devolver());
    }

    @Test
    void devolver_dosVeces_lanzaIllegalStateEnSegundoIntento() {
        Prestamo prestamo = PrestamoBuilder.unPrestamo()
                .activo()
                .build();

        prestamo.devolver();

        assertThrows(IllegalStateException.class, () -> prestamo.devolver());
    }

    @Test
    void renovar_cuandoNoEstaActivo_lanzaIllegalState() {
        Prestamo prestamo = PrestamoBuilder.unPrestamo()
                .vencido()
                .build();

        assertThrows(IllegalStateException.class, () -> prestamo.renovar());
    }

    @Test
    void renovar_cuandoEstaActivoPeroFechaVencida_lanzaIllegalState() {
        Prestamo prestamo = PrestamoBuilder.unPrestamo()
                .activo()
                .conVencimiento(LocalDate.now().minusDays(1))
                .build();

        assertThrows(IllegalStateException.class, () -> prestamo.renovar());
    }

    @Test
    void renovar_cuandoEstaActivoYNoVencido_extiendeSieteDias (){
        LocalDate hoy = LocalDate.now();

        Prestamo prestamo = PrestamoBuilder.unPrestamo()
                .conFechaPrestamo(hoy)
                .conVencimiento(hoy.plusDays(7))
                .activo()
                .build();
        
        prestamo.renovar();

        assertEquals(hoy.plusDays(14), prestamo.getFechaVencimiento());
    }
}
