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

import com.matiasanastasio.biblioteca.dto.libro.LibroCreateRequest;
import com.matiasanastasio.biblioteca.dto.libro.LibroResponse;
import com.matiasanastasio.biblioteca.model.entity.Libro;
import com.matiasanastasio.biblioteca.service.LibroService;

import jakarta.validation.Valid;
import mapper.LibroMapper;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    
    private final LibroService libroService;

    public LibroController(LibroService libroService){
        this.libroService = libroService;
    }

    // POST /api/libros -> crear libro
    @PostMapping
    public ResponseEntity<LibroResponse> crear(@Valid @RequestBody LibroCreateRequest req){

        Libro creado = libroService.crearLibro(
            req.getTitulo(), 
            req.getAutorId(), 
            req.getIsbn(), 
            req.getAnioPublicacion(), 
            req.getEjemplaresTotales());
        
        LibroResponse resp = LibroMapper.toResponse(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);   
    }

    // GET /api/libros obtener libros
    @GetMapping
    public ResponseEntity<List<LibroResponse>> obtenerLibros(){
        List<Libro> libros = libroService.obtenerTodos();
        List<LibroResponse> librosResponse = libros.stream()
            .map(LibroMapper::toResponse)
            .toList();
        return ResponseEntity.ok(librosResponse);
    }

    // GET /api/libros obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> obtenerPorId(@PathVariable Long id){
        Libro libro = libroService.obtenerPorId(id);
        return ResponseEntity.ok(LibroMapper.toResponse(libro));
    }
}
