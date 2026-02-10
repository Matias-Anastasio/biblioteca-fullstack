package com.matiasanastasio.biblioteca.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matiasanastasio.biblioteca.dto.libro.LibroCreateRequest;
import com.matiasanastasio.biblioteca.dto.libro.LibroResponse;
import com.matiasanastasio.biblioteca.dto.libro.LibroUpdateRequest;
import com.matiasanastasio.biblioteca.exception.ConflictException;
import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.mapper.LibroMapper;
import com.matiasanastasio.biblioteca.model.entity.Autor;
import com.matiasanastasio.biblioteca.model.entity.Libro;
import com.matiasanastasio.biblioteca.repository.AutorRepository;
import com.matiasanastasio.biblioteca.repository.LibroRepository;
import com.matiasanastasio.biblioteca.repository.spec.LibroSpecifications;

@Service
public class LibroService {
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository){
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    protected Libro buscarEntidadPorId(Long id){
        return libroRepository.findById(id)
            .orElseThrow(()-> new NotFoundException("El libro con id '" + id + "' no existe."));
    }


    // Crear libro
    @Transactional
    public LibroResponse crearLibro(LibroCreateRequest req){

        if(libroRepository.existsByIsbn(req.getIsbn())){
            throw new ConflictException("El ISBN ya esta en uso");   
        }
    
        Autor autor = autorRepository.findById(req.getAutorId())
            .orElseThrow(() -> new NotFoundException("Autor no encontrado"));

        Libro libro = new Libro(req.getTitulo(), autor, req.getIsbn(), req.getAnioPublicacion(), req.getEjemplaresTotales() );

        Libro creado = libroRepository.save(libro);

        return LibroMapper.toResponse(creado);
    }

    // Obtener todos
    @Transactional
    public List<LibroResponse> obtenerTodos(){
        return libroRepository.findAll().stream()
            .map(LibroMapper::toResponse)
            .toList();
    }

    // Obtener por ID
    @Transactional
    public LibroResponse obtenerPorId(Long id){
        return LibroMapper.toResponse(buscarEntidadPorId(id));
    }

    // Obtener por ISBN
    @Transactional
    public LibroResponse obtenerPorISBN(String isbn){
        Libro libro = libroRepository.findByIsbn(isbn)
            .orElseThrow(()-> new NotFoundException("Libro no encontrado"));
        return LibroMapper.toResponse(libro);
    }

    //Eliminar libro
    @Transactional
    public void eliminarLibro(Long id){
        libroRepository.delete(buscarEntidadPorId(id));
    }

    @Transactional
    public List<LibroResponse> buscar(String q, Long autorId, Boolean soloDisponibles){

        Specification<Libro> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(LibroSpecifications.buscarGlobal(q));

        if(autorId!=null){
            spec = spec.and(LibroSpecifications.conAutorId(autorId));
        }

        if(Boolean.TRUE.equals(soloDisponibles)){
            spec = spec.and(LibroSpecifications.soloDisponibles());
        }

        return libroRepository.findAll(spec).stream()
            .map(LibroMapper::toResponse)
            .toList();
    }

    @Transactional
    public LibroResponse actualizar(Long id, LibroUpdateRequest req){
        Libro libro = buscarEntidadPorId(id);
        if(req.getTitulo()!=null){
            libro.actualizarTitulo(req.getTitulo());
        }

        if(req.getIsbn()!=null){
            String nuevoIsbn = req.getIsbn().trim();
            if(libroRepository.existsByIsbnAndIdNot(nuevoIsbn,id)){
                throw new ConflictException("El ISBN ya está en uso");
            }
        libro.actualizarIsbn(nuevoIsbn);
        }
        

        if(req.getAnioPublicacion() != null){
            libro.actualizarAnioPublicacion(req.getAnioPublicacion());
        }

        if(req.getAutorId() != null){
            Autor autor = autorRepository.findById(req.getAutorId())
                .orElseThrow(()-> new NotFoundException("El autor con id '"+req.getAutorId()+"' no existe"));
            libro.cambiarAutor(autor);
        }

        if(req.getEjemplaresTotales() != null){
            libro.actualizarEjemplaresTotales(req.getEjemplaresTotales());
        }

        Libro guardado = libroRepository.save(libro);
        return LibroMapper.toResponse(guardado);
    }
}
