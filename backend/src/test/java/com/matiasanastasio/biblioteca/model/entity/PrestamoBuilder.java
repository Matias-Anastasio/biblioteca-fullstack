package com.matiasanastasio.biblioteca.model.entity;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;

import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;

public class PrestamoBuilder {
    
    private Usuario usuario = mock(Usuario.class);
    private Libro libro = mock(Libro.class);
    private LocalDate fechaPrestamo = LocalDate.now();
    private LocalDate fechaVencimiento;
    private EstadoPrestamo estado = EstadoPrestamo.ACTIVO;

    private PrestamoBuilder(){}

    public static PrestamoBuilder unPrestamo(){
        return new PrestamoBuilder();
    }

    public PrestamoBuilder conUsuario(Usuario usuario){
        this.usuario=usuario;
        return this;
    }

    public PrestamoBuilder conLibro(Libro libro){
        this.libro = libro;
        return this;
    }

    public PrestamoBuilder conFechaPrestamo(LocalDate fecha){
        this.fechaPrestamo = fecha;
        return this;
    }

    public PrestamoBuilder conVencimiento(LocalDate fecha){
        this.fechaVencimiento = fecha;
        return this;
    }

    public PrestamoBuilder conEstado(EstadoPrestamo estado){
        this.estado = estado;
        return this;
    }

    public PrestamoBuilder activo(){
        this.estado = EstadoPrestamo.ACTIVO;
        return this;
    }

    public PrestamoBuilder vencido(){
        this.estado = EstadoPrestamo.VENCIDO;
        return this;
    }
    
    public PrestamoBuilder devuelto(){
        this.estado = EstadoPrestamo.DEVUELTO;
        return this;
    }

    public Prestamo build(){
        if(fechaVencimiento == null){
            fechaVencimiento = fechaPrestamo.plusDays(7);
        }

        return new Prestamo(
            usuario,
            libro,
            fechaPrestamo,
            fechaVencimiento,
            estado
        );
    }

}
