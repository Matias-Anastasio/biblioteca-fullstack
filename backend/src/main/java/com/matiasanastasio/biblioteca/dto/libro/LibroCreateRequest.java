package com.matiasanastasio.biblioteca.dto.libro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LibroCreateRequest {
    @NotBlank
    private String titulo;
    @NotNull
    private Long autorId;
    @NotBlank
    private String isbn;
    @NotNull
    private Integer anioPublicacion;
    @NotNull
    private Integer ejemplaresTotales;
}
