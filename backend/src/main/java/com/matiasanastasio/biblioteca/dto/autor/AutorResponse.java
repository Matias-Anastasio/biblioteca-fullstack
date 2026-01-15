package com.matiasanastasio.biblioteca.dto.autor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AutorResponse {
    private Long id;
    private String nombre;
    private String apellido;    
}
