package com.matiasanastasio.biblioteca.dto.usuario;

import com.matiasanastasio.biblioteca.model.enums.RolUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String email;
    private RolUsuario rol;
}
