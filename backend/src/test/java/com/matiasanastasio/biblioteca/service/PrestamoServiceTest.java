package com.matiasanastasio.biblioteca.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.matiasanastasio.biblioteca.dto.prestamo.PrestamoCreateRequest;
import com.matiasanastasio.biblioteca.dto.prestamo.PrestamoResponse;
import com.matiasanastasio.biblioteca.exception.ConflictException;
import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.model.entity.Autor;
import com.matiasanastasio.biblioteca.model.entity.Libro;
import com.matiasanastasio.biblioteca.model.entity.Prestamo;
import com.matiasanastasio.biblioteca.model.entity.Usuario;
import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;
import com.matiasanastasio.biblioteca.repository.LibroRepository;
import com.matiasanastasio.biblioteca.repository.PrestamoRepository;
import com.matiasanastasio.biblioteca.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    PrestamoRepository prestamoRepository;
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    LibroRepository libroRepository;

    @InjectMocks
    PrestamoService prestamoService;

    @Test
    void crearPrestamo_casoFeliz_guardaYDescuentaStock() {

        String email = "user@mail.com";
        UUID usuarioId = UUID.randomUUID();
        UUID libroId = UUID.randomUUID();

        PrestamoCreateRequest req = mock(PrestamoCreateRequest.class);
        when(req.getLibroId()).thenReturn(libroId);

        Usuario usuario = mock(Usuario.class);
        when(usuario.getId()).thenReturn(usuarioId);

        Autor autor = mock(Autor.class);
        Libro libro = new Libro("Libro test", autor, "123456", 2008, 5);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(libroRepository.findById(libroId)).thenReturn(Optional.of(libro));

        when(prestamoRepository.existsByUsuarioIdAndEstado(usuarioId, EstadoPrestamo.VENCIDO)).thenReturn(false);
        when(prestamoRepository.countByUsuarioIdAndEstado(usuarioId, EstadoPrestamo.ACTIVO)).thenReturn(0);
        when(prestamoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        int stockInicial = libro.getEjemplaresDisponibles();

        PrestamoResponse response = prestamoService.crearPrestamo(req, email);

        verify(prestamoRepository).save(any());
        assertNotNull(response);
        assertEquals(EstadoPrestamo.ACTIVO, response.getEstado());
        assertEquals(stockInicial - 1, libro.getEjemplaresDisponibles());
    }

    @Test
    void crearPrestamo_sinStock_lanzaConflict() {
        String email = "user@mail.com";
        UUID libroId = UUID.randomUUID();

        PrestamoCreateRequest req = mock(PrestamoCreateRequest.class);
        when(req.getLibroId()).thenReturn(libroId);

        Usuario usuario = mock(Usuario.class);
        Autor autor = mock(Autor.class);
        Libro libroSinStock = new Libro("Libro sin stock", autor, "123456", 2000, 0);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(libroRepository.findById(libroId)).thenReturn(Optional.of(libroSinStock));

        assertThrows(ConflictException.class, () -> prestamoService.crearPrestamo(req, email));
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void crearPrestamo_usuarioNoExiste_lanzaNotFound() {
        String email = "user@mail.com";

        PrestamoCreateRequest req = mock(PrestamoCreateRequest.class);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> prestamoService.crearPrestamo(req, email));
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void crearPrestamo_libroNoEncontrado_lanzaNotFound() {
        String email = "user@mail.com";
        UUID libroId = UUID.randomUUID();

        PrestamoCreateRequest req = mock(PrestamoCreateRequest.class);
        when(req.getLibroId()).thenReturn(libroId);

        Usuario usuario = mock(Usuario.class);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        when(libroRepository.findById(libroId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> prestamoService.crearPrestamo(req, email));
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void crearPrestamo_usuarioConVencidos_lanzaConflict() {
        String email = "user@mail.com";
        UUID usuarioId = UUID.randomUUID();
        UUID libroId = UUID.randomUUID();

        PrestamoCreateRequest req = mock(PrestamoCreateRequest.class);
        when(req.getLibroId()).thenReturn(libroId);

        Usuario usuarioDeudor = mock(Usuario.class);
        when(usuarioDeudor.getId()).thenReturn(usuarioId);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioDeudor));

        Libro libro = mock(Libro.class);
        when(libroRepository.findById(libroId)).thenReturn(Optional.of(libro));
        when(libro.getEjemplaresDisponibles()).thenReturn(5);
        when(prestamoRepository.existsByUsuarioIdAndEstado(usuarioId, EstadoPrestamo.VENCIDO)).thenReturn(true);

        assertThrows(ConflictException.class, () -> prestamoService.crearPrestamo(req, email));
        verify(prestamoRepository, never()).countByUsuarioIdAndEstado(any(UUID.class), eq(EstadoPrestamo.ACTIVO));
        verify(libro, never()).prestarUnEjemplar();
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void crearPrestamo_superaMaximosActivos_lanzaConflict() {
        String email = "user@mail.com";
        UUID usuarioId = UUID.randomUUID();
        UUID libroId = UUID.randomUUID();

        PrestamoCreateRequest req = mock(PrestamoCreateRequest.class);
        when(req.getLibroId()).thenReturn(libroId);

        Usuario usuarioConDemasiadosPrestamos = mock(Usuario.class);
        when(usuarioConDemasiadosPrestamos.getId()).thenReturn(usuarioId);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioConDemasiadosPrestamos));

        Libro libro = mock(Libro.class);
        when(libroRepository.findById(libroId)).thenReturn(Optional.of(libro));
        when(libro.getEjemplaresDisponibles()).thenReturn(5);

        when(prestamoRepository.existsByUsuarioIdAndEstado(usuarioId, EstadoPrestamo.VENCIDO)).thenReturn(false);
        when(prestamoRepository.countByUsuarioIdAndEstado(usuarioId, EstadoPrestamo.ACTIVO)).thenReturn(3);

        assertThrows(ConflictException.class, () -> prestamoService.crearPrestamo(req, email));
        verify(libro, never()).prestarUnEjemplar();
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void devolverPrestamo_casoFeliz_marcaDevueltoYSumaStock() {
        UUID prestamoId = UUID.randomUUID();

        Usuario usuario = mock(Usuario.class);
        Autor autor = mock(Autor.class);
        Libro libro = new Libro("Libro test", autor, "123456", 2000, 5);
        libro.prestarUnEjemplar(); // disponibles = 4

        Prestamo prestamo = new Prestamo(usuario, libro, LocalDate.now(), LocalDate.now().plusDays(7), EstadoPrestamo.ACTIVO);

        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.of(prestamo));

        prestamoService.devolverPrestamo(prestamoId);

        assertEquals(5, libro.getEjemplaresDisponibles());
        assertEquals(EstadoPrestamo.DEVUELTO, prestamo.getEstado());
    }

    @Test
    void devolverPrestamo_prestamoNoExiste_lanzaNotFound() {

    }
}