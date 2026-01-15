package com.matiasanastasio.biblioteca.dto.libro;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibroResponse {
    
    private Long id;
    private String titulo;
    private Long autorId;
    private String autorNombre;
    private String isbn;
    private Integer anioPublicacion;
    private Integer ejemplaresDisponibles;
    private Integer ejemplaresTotales;

}
