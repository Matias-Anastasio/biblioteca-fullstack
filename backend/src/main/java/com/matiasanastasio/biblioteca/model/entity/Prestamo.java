package com.matiasanastasio.biblioteca.model.entity;

import java.time.LocalDate;

import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "prestamos")
public class Prestamo {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;
    
    @NotNull
    @Column(nullable=false)
    private LocalDate fechaPrestamo;

    @NotNull
    @Column(nullable=false)
    private LocalDate fechaVencimiento;

    private LocalDate fechaDevolucion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estado; 
    protected Prestamo() {
    }

    public Prestamo(Usuario usuario, Libro libro, LocalDate fechaPrestamo, LocalDate fechaVencimiento, EstadoPrestamo estado) {
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
    }
    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public Libro getLibro() {
        return libro;
    }
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
    public EstadoPrestamo getEstado() {
        return estado;
    }
    
    public void devolver(){
        if(estado!=EstadoPrestamo.ACTIVO && estado!=EstadoPrestamo.VENCIDO){
            throw new IllegalStateException("No se puede devolver un prestamo que no este ACTIVO o VENCIDO");
        }
        this.estado = EstadoPrestamo.DEVUELTO;
        this.fechaDevolucion = LocalDate.now();
    }

    public void renovar() {
        if(this.estado != EstadoPrestamo.ACTIVO){
            throw new IllegalStateException("El prestamo no se encuentra activo");
        }
        this.fechaVencimiento = fechaVencimiento.plusDays(7);
    }
}
