package com.matiasanastasio.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matiasanastasio.biblioteca.dto.autor.AutorCreateRequest;
import com.matiasanastasio.biblioteca.dto.autor.AutorResponse;
import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.mapper.AutorMapper;
import com.matiasanastasio.biblioteca.model.entity.Autor;
import com.matiasanastasio.biblioteca.repository.AutorRepository;

import jakarta.transaction.Transactional;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository){
        this.autorRepository = autorRepository;
    }

    protected Autor buscarEntidadPorId(Long id){
        return autorRepository.findById(id)
            .orElseThrow(()-> new NotFoundException("No existe autor con id: " + id));
    }

    // Crear autor
    @Transactional
    public AutorResponse crearAutor(AutorCreateRequest req){
        Autor autor = new Autor(req.getNombre(),req.getApellido()); 
        Autor creado = autorRepository.save(autor);
        return AutorMapper.toResponse(creado);
    }

    // Obtener todos
    @Transactional
    public List<AutorResponse> obtenerTodos(){
        return autorRepository.findAll().stream()
            .map(AutorMapper::toResponse)
            .toList();
    }

    // Obtener por ID
    @Transactional
    public AutorResponse obtenerPorId(Long id){
        return AutorMapper.toResponse(buscarEntidadPorId(id));
    }

    // Eliminar autor
    @Transactional
    public void eliminarAutor(Long id){
        Autor autor = buscarEntidadPorId(id);
        autorRepository.delete(autor);
    }
}
