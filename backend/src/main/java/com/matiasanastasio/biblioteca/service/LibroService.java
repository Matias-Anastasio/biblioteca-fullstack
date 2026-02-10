package com.matiasanastasio.biblioteca.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;
import com.matiasanastasio.biblioteca.repository.AutorRepository;
import com.matiasanastasio.biblioteca.repository.LibroRepository;
import com.matiasanastasio.biblioteca.repository.PrestamoRepository;
import com.matiasanastasio.biblioteca.repository.spec.LibroSpecifications;

@Service
public class LibroService {
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final PrestamoRepository prestamoRepository;

    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository, PrestamoRepository prestamoRepository){
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
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
        Libro libro = buscarEntidadPorId(id);

        if(prestamoRepository.existsByLibroIdAndEstadoIn(id, java.util.List.of(EstadoPrestamo.ACTIVO,EstadoPrestamo.VENCIDO))){
            throw new ConflictException("No se puede eliminar el libro: tiene prestamos activos o vencidos");
        }

        libroRepository.delete(libro);
    }

    @Transactional
    public Page<LibroResponse> buscar(String q, Long autorId, Boolean soloDisponibles, Pageable pageable){

        Specification<Libro> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(LibroSpecifications.buscarGlobal(q));

        if(autorId!=null){
            spec = spec.and(LibroSpecifications.conAutorId(autorId));
        }

        if(Boolean.TRUE.equals(soloDisponibles)){
            spec = spec.and(LibroSpecifications.soloDisponibles());
        }

        return libroRepository.findAll(spec, pageable)
            .map(LibroMapper::toResponse);
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
