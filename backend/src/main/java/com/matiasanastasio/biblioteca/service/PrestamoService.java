package com.matiasanastasio.biblioteca.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matiasanastasio.biblioteca.exception.ConflictException;
import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.model.entity.Libro;
import com.matiasanastasio.biblioteca.model.entity.Prestamo;
import com.matiasanastasio.biblioteca.model.entity.Usuario;
import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;
import com.matiasanastasio.biblioteca.repository.LibroRepository;
import com.matiasanastasio.biblioteca.repository.PrestamoRepository;
import com.matiasanastasio.biblioteca.repository.UsuarioRepository;
import com.matiasanastasio.biblioteca.repository.spec.PrestamoSpecifications;

@Service
public class PrestamoService {

    private static final int DIAS_PRESTAMO_DEFAULT = 14;

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    public PrestamoService(
            PrestamoRepository prestamoRepository,
            UsuarioRepository usuarioRepository,
            LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional
    public Prestamo crearPrestamo(Long usuarioId, Long libroId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(()-> new NotFoundException("Usuario no encontrado con id: " + usuarioId));

        Libro libro = libroRepository.findById(libroId)
            .orElseThrow(()-> new NotFoundException("Libro no encontrado con id:" + libroId));
        
        if(libro.getEjemplaresDisponibles() <= 0){
            throw new ConflictException("No hay ejemplares disponibles para el libro id: " + libroId);
        }

        libro.prestarUnEjemplar();
        
        LocalDate hoy = LocalDate.now();

        LocalDate vencimiento = hoy.plusDays(DIAS_PRESTAMO_DEFAULT);

        Prestamo prestamo = new Prestamo(usuario, libro, hoy, vencimiento, EstadoPrestamo.ACTIVO);

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo devolverPrestamo(Long prestamoId){
        
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
            .orElseThrow(()-> new NotFoundException("No existe el prestamo con el id: " + prestamoId));

        prestamo.devolver();

        prestamo.getLibro().devolverUnEjemplar();

        return prestamo;
    }


    @Transactional
    public Prestamo obtenerPorId(Long id){
        return prestamoRepository.findById(id)
            .orElseThrow(()-> new NotFoundException("No existe el prestamo con id: " + id));
    }

    @Transactional
    public List<Prestamo> buscar(Long usuarioId, Long libroId, EstadoPrestamo estado){

        Specification<Prestamo> spec = (root, query, cb) -> cb.conjunction();

        if(usuarioId != null){
            spec=spec.and(PrestamoSpecifications.conUsuarioId(usuarioId));
        }

        if(libroId != null){
            spec = spec.and(PrestamoSpecifications.conLibroId(libroId));
        }

        if(estado != null){
            spec = spec.and(PrestamoSpecifications.conEstado(estado));
        }

        return prestamoRepository.findAll(spec);
    }

    @Transactional
    public Prestamo renovar(Long id){
        Prestamo prestamo = prestamoRepository.findById(id)
            .orElseThrow(()-> new NotFoundException("No existe el prestamo con id: " +id));
        prestamo.renovar();
        return prestamo;
    }

}
