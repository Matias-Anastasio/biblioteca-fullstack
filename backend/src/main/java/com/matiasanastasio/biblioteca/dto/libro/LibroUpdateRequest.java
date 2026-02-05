package com.matiasanastasio.biblioteca.dto.libro;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class LibroUpdateRequest {
    private String titulo;
    private Long autorId;
    private String isbn;

    @Min(0)
    private Integer anioPublicacion;

    @Min(0)
    private Integer ejemplaresTotales;
}
