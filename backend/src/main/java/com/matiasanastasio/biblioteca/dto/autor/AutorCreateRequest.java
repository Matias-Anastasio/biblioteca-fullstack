package com.matiasanastasio.biblioteca.dto.autor;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AutorCreateRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
}
