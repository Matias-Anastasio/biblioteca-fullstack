package com.matiasanastasio.biblioteca.dto.autor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AutorResponse {
    private UUID id;
    private String nombre;
    private String apellido;    
}
