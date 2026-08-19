package com.matiasanastasio.biblioteca.dto.usuario;

import com.matiasanastasio.biblioteca.model.enums.RolUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private UUID id;
    private String nombre;
    private String email;
    private RolUsuario rol;
}
