package com.matiasanastasio.biblioteca.dto.usuario;

import com.matiasanastasio.biblioteca.model.enums.RolUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String contrasena;
    private RolUsuario rol;
}
