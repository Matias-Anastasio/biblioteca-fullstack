package com.matiasanastasio.biblioteca.dto.libro;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LibroResponse {
    
    private UUID id;
    private String titulo;
    private UUID autorId;
    private String autorNombre;
    private String isbn;
    private Integer anioPublicacion;
    private Integer ejemplaresDisponibles;
    private Integer ejemplaresTotales;

}
