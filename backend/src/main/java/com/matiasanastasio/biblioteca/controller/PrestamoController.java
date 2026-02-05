package com.matiasanastasio.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.matiasanastasio.biblioteca.model.entity.Prestamo;
import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;
import com.matiasanastasio.biblioteca.service.PrestamoService;

import jakarta.validation.Valid;
import mapper.PrestamoMapper;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {
    
    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService){
        this.prestamoService = prestamoService;
    }

    //POST /api/prestamos -> nuevo prestamo
    @PostMapping
    public ResponseEntity<PrestamoResponse> crear(@Valid @RequestBody PrestamoCreateRequest req){

        Prestamo creado = prestamoService.crearPrestamo(req.getUsuarioId(), req.getLibroId());

        PrestamoResponse resp = PrestamoMapper.toResponse(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    //PUT /api/prestamos/{id}/devolucion -> devolver un prestamo
    @PutMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoResponse> devolverPrestamo(@PathVariable Long id){
        Prestamo prestamo = prestamoService.devolverPrestamo(id);
        return ResponseEntity.ok(PrestamoMapper.toResponse(prestamo));
    }

    //GET /api/pretamos/{id} -> obtener prestamo por id
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse> obtenerPorId(@PathVariable Long id){
        Prestamo prestamo = prestamoService.obtenerPorId(id);
        return ResponseEntity.ok(PrestamoMapper.toResponse(prestamo));
    }

    // GET /api/prestamos?usuarioId=...&libroId=...&estado=... -> filtrar prestamos por usuario, libro y/o estado
    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> obtenerPrestamos(
        @RequestParam(required = false) Long usuarioId,
        @RequestParam(required = false) Long libroId,
        @RequestParam(required = false) EstadoPrestamo estado
    ){
        List<Prestamo> prestamos = prestamoService.buscar(usuarioId, libroId, estado);
        List<PrestamoResponse> prestamosResp = prestamos.stream()
            .map(PrestamoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(prestamosResp);
    }

    //PUT /api/{id}/renovacion -> renueva el prestamo una semana
    @PutMapping("/{id}/renovacion")
    public ResponseEntity<PrestamoResponse> renovarPrestamo(@PathVariable Long id){
        Prestamo prestamo = prestamoService.renovar(id);
        return ResponseEntity.ok(PrestamoMapper.toResponse(prestamo));
    }
}
