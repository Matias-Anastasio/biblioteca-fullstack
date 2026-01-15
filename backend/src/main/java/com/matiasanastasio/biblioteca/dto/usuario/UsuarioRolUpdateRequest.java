package com.matiasanastasio.biblioteca.dto.usuario;

import com.matiasanastasio.biblioteca.model.enums.RolUsuario;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioRolUpdateRequest {
    
    @NotNull
    private RolUsuario rol;
}
