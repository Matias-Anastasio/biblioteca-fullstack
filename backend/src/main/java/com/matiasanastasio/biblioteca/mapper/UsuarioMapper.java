package com.matiasanastasio.biblioteca.mapper;

import com.matiasanastasio.biblioteca.dto.usuario.UsuarioResponse;
import com.matiasanastasio.biblioteca.model.entity.Usuario;

public class UsuarioMapper {
    
    public static UsuarioResponse toResponse(Usuario u){
        return new UsuarioResponse(u.getId(),u.getNombre(),u.getEmail(), u.getRol());
    }
}
