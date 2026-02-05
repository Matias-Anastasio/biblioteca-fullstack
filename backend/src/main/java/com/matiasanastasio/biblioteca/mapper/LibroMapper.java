package com.matiasanastasio.biblioteca.mapper;

import com.matiasanastasio.biblioteca.dto.libro.LibroResponse;
import com.matiasanastasio.biblioteca.model.entity.Libro;

public class LibroMapper {
    
    public static LibroResponse toResponse(Libro libro){
        return new LibroResponse(
            libro.getId(),
            libro.getTitulo(),
            libro.getAutor().getId(),
            libro.getAutor().getNombre() + " " + libro.getAutor().getApellido(),
            libro.getIsbn(),
            libro.getAnioPublicacion(),
            libro.getEjemplaresDisponibles(),
            libro.getEjemplaresTotales()
        );
    }
}
