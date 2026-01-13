package com.matiasanastasio.biblioteca.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matiasanastasio.biblioteca.dto.UsuarioCreateRequest;
import com.matiasanastasio.biblioteca.dto.UsuarioResponse;
import com.matiasanastasio.biblioteca.model.entity.Usuario;
import com.matiasanastasio.biblioteca.service.UsuarioService;

import jakarta.validation.Valid;

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

        UsuarioResponse resp = toResponse(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


    // GET /api/usuarios/{id}  -> obtener usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(toResponse(usuario));
    }

    private UsuarioResponse toResponse(Usuario u){
        return new UsuarioResponse(
            u.getId(),
            u.getNombre(),
            u.getEmail(),
            u.getRol()
        );
    }
}
