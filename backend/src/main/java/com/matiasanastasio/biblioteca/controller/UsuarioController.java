package com.matiasanastasio.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matiasanastasio.biblioteca.dto.usuario.UsuarioCreateRequest;
import com.matiasanastasio.biblioteca.dto.usuario.UsuarioResponse;
import com.matiasanastasio.biblioteca.dto.usuario.UsuarioRolUpdateRequest;
import com.matiasanastasio.biblioteca.model.entity.Usuario;
import com.matiasanastasio.biblioteca.service.UsuarioService;

import jakarta.validation.Valid;
import mapper.UsuarioMapper;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // POST /api/usuarios  -> crear usuario
    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioCreateRequest req){

        Usuario creado = usuarioService.crearUsuario(
            req.getNombre(),
            req.getEmail(),
            req.getContrasena(), 
            req.getRol());

        UsuarioResponse resp = UsuarioMapper.toResponse(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


    // GET /api/usuarios/{id}  -> obtener usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    // GET /api/usuarios -> obtener usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerUsuarios(){
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        List<UsuarioResponse> usuariosResponse = usuarios.stream()
            .map(UsuarioMapper::toResponse)
            .toList();
        return ResponseEntity.ok(usuariosResponse);
    }

    // PUT /api/usuarios/{id}/rol -> cambiar rol del usuario
    @PutMapping("/{id}/rol")
    public ResponseEntity<UsuarioResponse> cambiarRol(@PathVariable Long id, @Valid @RequestBody UsuarioRolUpdateRequest req){
        Usuario actualizado = usuarioService.cambiarRol(id,req.getRol());
        return ResponseEntity.ok(UsuarioMapper.toResponse(actualizado));
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
