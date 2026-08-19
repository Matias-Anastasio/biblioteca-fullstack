package com.matiasanastasio.biblioteca.dto.libro;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.UUID;

@Data
public class LibroUpdateRequest {
    private String titulo;
    private UUID autorId;
    private String isbn;

    @Min(1400)
    private Integer anioPublicacion;

    @Min(0)
    private Integer ejemplaresTotales;
}
