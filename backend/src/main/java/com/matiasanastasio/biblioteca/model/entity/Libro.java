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

    public Libro(String titulo, Autor autor, String isbn, Integer anioPublicacion, Integer ejemplaresTotales, Integer ejemplaresDisponibles) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.ejemplaresTotales = ejemplaresTotales;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
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

}