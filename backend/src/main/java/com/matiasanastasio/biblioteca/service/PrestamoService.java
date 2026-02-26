package com.matiasanastasio.biblioteca.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matiasanastasio.biblioteca.dto.prestamo.PrestamoCreateRequest;
import com.matiasanastasio.biblioteca.dto.prestamo.PrestamoResponse;
import com.matiasanastasio.biblioteca.exception.ConflictException;
import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.mapper.PrestamoMapper;
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
    private static final int MAX_PRESTAMOS_ACTIVOS = 3;

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

    protected Prestamo buscarEntidadPorId(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe el prestamo con el id: " + id));
    }

    private boolean esAdmin(Authentication auth){
        return auth.getAuthorities().stream()
            .anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
    }

    private void validarLimitePrestamos(Long usuarioId) {

        if (prestamoRepository.existsByUsuarioIdAndEstado(usuarioId, EstadoPrestamo.VENCIDO)) {
            throw new ConflictException("No se puede crear un prestamo: el usuario tiene prestamos vencidos");
        }

        int activos = prestamoRepository.countByUsuarioIdAndEstado(usuarioId, EstadoPrestamo.ACTIVO);
        if (activos >= MAX_PRESTAMOS_ACTIVOS) {
            throw new ConflictException(
                    "No se puede crear un prestamo: el usuario alcanzó el máximo de prestamos activos ("
                            + MAX_PRESTAMOS_ACTIVOS + ")");
        }
    }

    @Transactional
    public PrestamoResponse crearPrestamo(PrestamoCreateRequest req, String emailAutenticado) {

        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Libro libro = libroRepository.findById(req.getLibroId())
                .orElseThrow(() -> new NotFoundException("Libro no encontrado con id:" + req.getLibroId()));

        if (libro.getEjemplaresDisponibles() <= 0) {
            throw new ConflictException("No hay ejemplares disponibles para el libro id: " + req.getLibroId());
        }

        validarLimitePrestamos(usuario.getId());

        libro.prestarUnEjemplar();

        LocalDate hoy = LocalDate.now();

        LocalDate vencimiento = hoy.plusDays(DIAS_PRESTAMO_DEFAULT);

        Prestamo prestamo = new Prestamo(usuario, libro, hoy, vencimiento, EstadoPrestamo.ACTIVO);

        Prestamo creado = prestamoRepository.save(prestamo);

        return PrestamoMapper.toResponse(creado);
    }

    @Transactional
    public PrestamoResponse devolverPrestamo(Long prestamoId) {

        Prestamo prestamo = buscarEntidadPorId(prestamoId);
        prestamo.devolver();

        prestamo.getLibro().devolverUnEjemplar();

        return PrestamoMapper.toResponse(prestamo);
    }

    @Transactional(readOnly = true)
    public PrestamoResponse obtenerPorId(Long id) {
        return PrestamoMapper.toResponse(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public Page<PrestamoResponse> buscar(Long usuarioId, Long libroId, EstadoPrestamo estado, Pageable pageable, Authentication auth) {

        String email = auth.getName();

        if(!esAdmin(auth)){
            Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
            
            usuarioId = u.getId();
        }

        Specification<Prestamo> spec = (root, query, cb) -> cb.conjunction();

        if (usuarioId != null) {
            spec = spec.and(PrestamoSpecifications.conUsuarioId(usuarioId));
        }
        if (libroId != null) {
            spec = spec.and(PrestamoSpecifications.conLibroId(libroId));
        }
        if (estado != null) {
            spec = spec.and(PrestamoSpecifications.conEstado(estado));
        }

        return prestamoRepository.findAll(spec, pageable)
                .map(PrestamoMapper::toResponse);
    }

    @Transactional
    public PrestamoResponse renovar(Long id) {
        Prestamo prestamo = buscarEntidadPorId(id);
        prestamo.renovar();
        return PrestamoMapper.toResponse(prestamo);
    }

}
