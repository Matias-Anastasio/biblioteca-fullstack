package com.matiasanastasio.biblioteca.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matiasanastasio.biblioteca.dto.prestamo.PrestamoCreateRequest;
import com.matiasanastasio.biblioteca.dto.prestamo.PrestamoResponse;
import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;
import com.matiasanastasio.biblioteca.service.PrestamoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {
    
    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService){
        this.prestamoService = prestamoService;
    }

    //POST /api/prestamos -> nuevo prestamo
    @PostMapping
    public ResponseEntity<PrestamoResponse> crear(@Valid @RequestBody PrestamoCreateRequest req, Authentication auth){

        PrestamoResponse creado = prestamoService.crearPrestamo(req, auth.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    //PUT /api/prestamos/{id}/devolucion -> devolver un prestamo
    @PutMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoResponse> devolverPrestamo(@PathVariable Long id){
        return ResponseEntity.ok(prestamoService.devolverPrestamo(id));
    }

    //GET /api/pretamos/{id} -> obtener prestamo por id
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(prestamoService.obtenerPorId(id));
    }

    // GET /api/prestamos?usuarioId=...&libroId=...&estado=... -> filtrar prestamos por usuario, libro y/o estado
    @GetMapping
    public ResponseEntity<Page<PrestamoResponse>> obtenerPrestamos(
        @RequestParam(required = false) Long usuarioId,
        @RequestParam(required = false) Long libroId,
        @RequestParam(required = false) EstadoPrestamo estado,
        Pageable pageable,
        Authentication auth
    ){
        return ResponseEntity.ok(prestamoService.buscar(usuarioId, libroId, estado, pageable, auth));
    }

    //PUT /api/{id}/renovacion -> renueva el prestamo una semana
    @PutMapping("/{id}/renovacion")
    public ResponseEntity<PrestamoResponse> renovarPrestamo(@PathVariable Long id){
        return ResponseEntity.ok(prestamoService.renovar(id));
    }
}
