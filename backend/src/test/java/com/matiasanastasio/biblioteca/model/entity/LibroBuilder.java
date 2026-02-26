package com.matiasanastasio.biblioteca.model.entity;

import static org.mockito.Mockito.mock;

public class LibroBuilder {
    
    private String titulo= "Titulo Default";
    private Autor autor = mock(Autor.class);
    private String isbn = "1234567890";
    private Integer anioPublicacion = 2000;
    private Integer ejemplaresTotales = 5;

    private LibroBuilder(){
    }

    public static LibroBuilder unLibro(){
        return new LibroBuilder();
    }

    public LibroBuilder conTitulo(String titulo){
        this.titulo = titulo;
        return this;
    }

    public LibroBuilder conAutor(Autor autor){
        this.autor = autor;
        return this;
    }

    public LibroBuilder conIsbn(String isbn){
        this.isbn = isbn;
        return this;
    }

    public LibroBuilder conAnio(Integer anio){
        this.anioPublicacion = anio;
        return this;
    }

    public LibroBuilder conTotal(Integer total){
        this.ejemplaresTotales = total;
        return this;
    }

    public Libro build(){
        return new Libro(
            titulo,
            autor,
            isbn,
            anioPublicacion,
            ejemplaresTotales
        );
    }
}

