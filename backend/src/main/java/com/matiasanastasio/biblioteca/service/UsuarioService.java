package com.matiasanastasio.biblioteca.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matiasanastasio.biblioteca.exception.ConflictException;
import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.model.entity.Usuario;
import com.matiasanastasio.biblioteca.model.enums.RolUsuario;
import com.matiasanastasio.biblioteca.repository.UsuarioRepository;


@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Crear usuario
    @Transactional
    public Usuario crearUsuario(String nombre, String email, String contrasena, RolUsuario rol){
        if(usuarioRepository.existsByEmail(email)){
            throw new ConflictException("El email ya está en uso");
        }

        RolUsuario rolFinal = (rol == null) ? RolUsuario.USER : rol;

        String hash = passwordEncoder.encode(contrasena);

        Usuario nuevo = new Usuario(nombre, email, hash, rolFinal);

        return usuarioRepository.save(nuevo);
    }

    // Buscar por email
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public Usuario cambiarRol(Long id, RolUsuario nuevoRol){
        Usuario u = buscarPorId(id);
        u.cambiarRol(nuevoRol);
        return u;
    }
}
