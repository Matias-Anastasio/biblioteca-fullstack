package com.matiasanastasio.biblioteca.dto.prestamo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoCreateRequest {
    @NotNull
    private Long usuarioId;
    @NotNull
    private Long libroId;
}
