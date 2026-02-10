package com.matiasanastasio.biblioteca.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "libros")
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable=false)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @NotBlank
    @Column(nullable=false,unique = true)
    private String isbn;

    @NotNull
    @Min(1400)
    private Integer anioPublicacion;

    @NotNull
    @Min(0)
    private Integer ejemplaresTotales;

    @NotNull
    @Min(0)
    private Integer ejemplaresDisponibles;

    protected Libro() {
    }

    public Libro(String titulo, Autor autor, String isbn, Integer anioPublicacion, Integer ejemplaresTotales) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.ejemplaresTotales = ejemplaresTotales;
        this.ejemplaresDisponibles = ejemplaresTotales;
    }

    public Long getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public Autor getAutor() {
        return autor;
    }
    public String getIsbn() {
        return isbn;
    }
    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }
    public Integer getEjemplaresTotales() {
        return ejemplaresTotales;
    }
    public Integer getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public void prestarUnEjemplar(){
        if (ejemplaresDisponibles<=0){
            throw new IllegalStateException("No hay ejemplares disponibles");
        }
        ejemplaresDisponibles--;
    }

    public void devolverUnEjemplar(){
        if(ejemplaresDisponibles>=ejemplaresTotales){
            throw new IllegalStateException("No se pueden devolver mas ejemplares");
        }
        ejemplaresDisponibles++;
    }

    public void actualizarTitulo(String nuevoTitulo){
        if(nuevoTitulo == null || nuevoTitulo.isBlank()){
            throw new IllegalArgumentException("El titulo no puede quedar vacio");
        }
        this.titulo = nuevoTitulo.trim();
    }

    public void actualizarIsbn (String nuevoIsbn){
        if(nuevoIsbn == null || nuevoIsbn.isBlank()){
            throw new IllegalArgumentException("El ISBN no puede quedar vacio");
        }
        this.isbn = nuevoIsbn.trim();
    }

    public void actualizarAnioPublicacion(Integer nuevoAnio){
        if(nuevoAnio == null || nuevoAnio<1400){
            throw new IllegalArgumentException("El año de publicación es inválido");
        }
        this.anioPublicacion = nuevoAnio;
    }

    public void cambiarAutor(Autor nuevoAutor){
        if(nuevoAutor == null){
            throw new IllegalArgumentException("El libro no puede quedar sin autor");
        }
        this.autor = nuevoAutor;
    }

    public void actualizarEjemplaresTotales(Integer nuevoTotal){
        if(nuevoTotal == null || nuevoTotal<0){
            throw new IllegalArgumentException("El total de ejemplares no puede ser nulo o negativo");
        }
        int prestados = this.ejemplaresTotales - this.ejemplaresDisponibles;

        if(nuevoTotal<prestados){
            throw new IllegalStateException("No se puede reducir el total por debajo de los ejemplares prestados: "+prestados);
        }
        this.ejemplaresTotales= nuevoTotal;
        this.ejemplaresDisponibles = nuevoTotal-prestados;
    }
}