package com.matiasanastasio.biblioteca.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matiasanastasio.biblioteca.dto.libro.LibroCreateRequest;
import com.matiasanastasio.biblioteca.dto.libro.LibroResponse;
import com.matiasanastasio.biblioteca.dto.libro.LibroUpdateRequest;
import com.matiasanastasio.biblioteca.service.LibroService;

import jakarta.validation.Valid;

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
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(libroService.crearLibro(req));  
    }

    // GET /api/libros obtener libros
    @GetMapping
    public ResponseEntity<List<LibroResponse>> obtenerLibros(){
        return ResponseEntity.ok(libroService.obtenerTodos());
    }

    // GET /api/libros obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(libroService.obtenerPorId(id));
    }

    // GET /api/libros/buscar?q=...&autorId=...&soloDisponibles=... buscar libros por titulo, autor y/o disponibles
    @GetMapping("/buscar")
    public ResponseEntity<Page<LibroResponse>> buscarLibros(
        @RequestParam(required=false) String q,
        @RequestParam(required=false) Long autorId,
        @RequestParam(required=false) Boolean soloDisponibles,
        Pageable pageable
    ){
        
        return ResponseEntity.ok(libroService.buscar(q, autorId, soloDisponibles, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody LibroUpdateRequest req
    ){
        return ResponseEntity.ok(libroService.actualizar(id,req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }

}
