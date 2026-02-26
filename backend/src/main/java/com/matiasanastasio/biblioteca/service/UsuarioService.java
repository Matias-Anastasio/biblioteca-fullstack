package com.matiasanastasio.biblioteca.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matiasanastasio.biblioteca.dto.usuario.UsuarioCreateRequest;
import com.matiasanastasio.biblioteca.dto.usuario.UsuarioResponse;
import com.matiasanastasio.biblioteca.exception.ConflictException;
import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.mapper.UsuarioMapper;
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

    protected Usuario buscarEntidadPorId(Long id){
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    protected Usuario buscarEntidadPorEmail(String email){
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    // Crear usuario
    @Transactional
    public UsuarioResponse crearUsuario(UsuarioCreateRequest req){
        if(usuarioRepository.existsByEmail(req.getEmail())){
            throw new ConflictException("El email ya está en uso");
        }

        RolUsuario rolFinal = (req.getRol() == null) ? RolUsuario.USER : req.getRol();

        String hash = passwordEncoder.encode(req.getContrasena());

        Usuario nuevo = new Usuario(req.getNombre(), req.getEmail(), hash, rolFinal);

        Usuario creado = usuarioRepository.save(nuevo);

        return UsuarioMapper.toResponse(creado);
    }

    // Buscar por email
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email){
        return UsuarioMapper.toResponse(buscarEntidadPorEmail(email));
    }

    // Obtener por ID
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id){
        return UsuarioMapper.toResponse(buscarEntidadPorId(id));
    }

    // Cambiar rol
    @Transactional
    public UsuarioResponse cambiarRol(Long id, RolUsuario nuevoRol){
        Usuario u = buscarEntidadPorId(id);
        u.cambiarRol(nuevoRol);
        return UsuarioMapper.toResponse(u);
    }

    // Obtener todos
    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerTodos(){
        return usuarioRepository.findAll().stream()
            .map(UsuarioMapper::toResponse)
            .toList();
    }

    // Eliminar usuario
    @Transactional
    public void eliminarUsuario(Long id){
        Usuario usuario = buscarEntidadPorId(id);
        usuarioRepository.delete(usuario);
    }

}
