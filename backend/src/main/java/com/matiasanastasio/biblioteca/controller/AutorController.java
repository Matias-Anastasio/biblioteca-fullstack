package com.matiasanastasio.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matiasanastasio.biblioteca.dto.autor.AutorCreateRequest;
import com.matiasanastasio.biblioteca.dto.autor.AutorResponse;
import com.matiasanastasio.biblioteca.model.entity.Autor;
import com.matiasanastasio.biblioteca.service.AutorService;

import jakarta.validation.Valid;
import mapper.AutorMapper;

@RestController
@RequestMapping("/api/autores")
public class AutorController {
    
    private final AutorService autorService;

    public AutorController(AutorService autorService){
        this.autorService = autorService;
    }

    //POST /api/autores -> crear autor
    @PostMapping
    public ResponseEntity<AutorResponse> crear(@Valid @RequestBody AutorCreateRequest req){

        Autor creado = autorService.crearAutor(
            req.getNombre(),
            req.getApellido());
        
        AutorResponse resp = AutorMapper.toResponse(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // GET /api/autores -> obtener autores
    @GetMapping
    public ResponseEntity<List<AutorResponse>> obtenerAutores(){
        List<Autor> autores = autorService.obtenerTodos();
        List<AutorResponse> autoresResponse = autores.stream()
            .map(AutorMapper::toResponse)
            .toList();
        return ResponseEntity.ok(autoresResponse);
    }

    //GET /api/autores/{id} -> obtener autor por id
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponse> obtenerPorId(@PathVariable Long id){
        Autor autor = autorService.obtenerPorId(id);
        return ResponseEntity.ok(AutorMapper.toResponse(autor));
    }
}
