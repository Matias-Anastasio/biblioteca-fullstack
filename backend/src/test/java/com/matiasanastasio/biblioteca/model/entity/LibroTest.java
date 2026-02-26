package com.matiasanastasio.biblioteca.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class LibroTest {
    
    @Test
    void prestarUnEjemplar_conDisponibles_decrementaEnUno(){
        Libro libro = LibroBuilder.unLibro()
            .conTotal(3)
            .build();

        libro.prestarUnEjemplar();

        assertEquals(2,libro.getEjemplaresDisponibles());
    }

    @Test
    void prestarUnEjemplar_sinDisponibles_lanzaIllegalState(){
        Libro libro = LibroBuilder.unLibro()
            .conTotal(1)
            .build();
        
        libro.prestarUnEjemplar();

        assertThrows(IllegalStateException.class, () -> libro.prestarUnEjemplar());
    }

    @Test
    void devolverUnEjemplar_sinPrestados_lanzaIllegalState(){
        Libro libro = LibroBuilder.unLibro().build();

        assertThrows(IllegalStateException.class, () -> libro.devolverUnEjemplar());
    }

    @Test
    void devolverUnEjemplar_cuandoHayPrestados_incrementaDisponibles(){
        Libro libro = LibroBuilder.unLibro()
            .conTotal(3)
            .build();

        libro.prestarUnEjemplar();

        assertEquals(2, libro.getEjemplaresDisponibles());
        
        libro.devolverUnEjemplar();

        assertEquals(3, libro.getEjemplaresDisponibles());
    }

    @Test
    void actualizarEjemplaresTotales_cuandoHayPrestados(){
        Libro libro = LibroBuilder.unLibro()
            .conTotal(5)
            .build();

        libro.prestarUnEjemplar();
        libro.prestarUnEjemplar();

        assertEquals(3, libro.getEjemplaresDisponibles());

        libro.actualizarEjemplaresTotales(10);

        assertEquals(10, libro.getEjemplaresTotales());
        assertEquals(8, libro.getEjemplaresDisponibles());
    }

    @Test
    void actualizarEjemplaresTotales_cuandoNuevoTotalEsMenorQuePrestados_lanzaIllegalState(){
        Libro libro = LibroBuilder.unLibro()
            .conTotal(5)
            .build();

        libro.prestarUnEjemplar();
        libro.prestarUnEjemplar();

        assertThrows(IllegalStateException.class, ()-> libro.actualizarEjemplaresTotales(1));
    }

    @Test
    void actualizarEjemplaresTotales_igualesAPrestados(){
        Libro libro = LibroBuilder.unLibro()
            .conTotal(5)
            .build();

        libro.prestarUnEjemplar();
        libro.prestarUnEjemplar();

        assertEquals(3, libro.getEjemplaresDisponibles());

        libro.actualizarEjemplaresTotales(2);

        assertEquals(0, libro.getEjemplaresDisponibles());
        assertEquals(2, libro.getEjemplaresTotales());
    }


}
