package com.matiasanastasio.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.model.entity.Autor;
import com.matiasanastasio.biblioteca.repository.AutorRepository;

import jakarta.transaction.Transactional;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository){
        this.autorRepository = autorRepository;
    }

    // Crear autor
    @Transactional
    public Autor crearAutor(String nombre, String apellido){
        Autor autor = new Autor(nombre,apellido);
        return autorRepository.save(autor);
    }

    // Obtener todos
    @Transactional
    public List<Autor> obtenerTodos(){
        return autorRepository.findAll();
    }

    // Obtener por ID
    @Transactional
    public Autor obtenerPorId(Long id){
        return autorRepository.findById(id)
            .orElseThrow(()-> new NotFoundException("Autor no encontrado"));
    }

    // Eliminar autor
    @Transactional
    public void eliminarAutor(Long id){
        Autor autor = obtenerPorId(id);
        autorRepository.delete(autor);
    }
}
