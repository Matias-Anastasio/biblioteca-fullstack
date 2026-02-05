package com.matiasanastasio.biblioteca.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.matiasanastasio.biblioteca.exception.ConflictException;
import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.model.entity.Autor;
import com.matiasanastasio.biblioteca.model.entity.Libro;
import com.matiasanastasio.biblioteca.repository.AutorRepository;
import com.matiasanastasio.biblioteca.repository.LibroRepository;
import com.matiasanastasio.biblioteca.repository.spec.LibroSpecifications;

import jakarta.transaction.Transactional;

@Service
public class LibroService {
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository){
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }


    // Crear libro
    @Transactional
    public Libro crearLibro(String titulo, Long autorId, String isbn, Integer anioPublicacion, Integer ejemplaresTotales){
        if(libroRepository.existsByIsbn(isbn)){
            throw new ConflictException("El ISBN ya esta en uso");   
        }
    
        Autor autor = autorRepository.findById(autorId)
            .orElseThrow(() -> new NotFoundException("Autor no encontrado"));

        Libro libro = new Libro(titulo, autor, isbn, anioPublicacion, ejemplaresTotales );

        return libroRepository.save(libro);
    }


    // Obtener todos
    @Transactional
    public List<Libro> obtenerTodos(){
        return libroRepository.findAll();
    }

    // Obtener por ID
    @Transactional
    public Libro obtenerPorId(Long id){
        return libroRepository.findById(id)
            .orElseThrow(()->new NotFoundException("Libro no encontrado"));
    }

    // Obtener por ISBN
    @Transactional
    public Libro obtenerPorISBN(String isbn){
        return libroRepository.findByIsbn(isbn)
            .orElseThrow(()-> new NotFoundException("Libro no encontrado"));
    }

    //Eliminar libro
    @Transactional
    public void eliminarLibro(Long id){
        Libro libro = obtenerPorId(id);
        libroRepository.delete(libro);
    }

    @Transactional
    public List<Libro> buscar(String q, Long autorId, Boolean soloDisponibles){

        Specification<Libro> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(LibroSpecifications.buscarGlobal(q));

        if(autorId!=null){
            spec = spec.and(LibroSpecifications.conAutorId(autorId));
        }

        if(Boolean.TRUE.equals(soloDisponibles)){
            spec = spec.and(LibroSpecifications.soloDisponibles());
        }

        return libroRepository.findAll(spec);
    }
}
