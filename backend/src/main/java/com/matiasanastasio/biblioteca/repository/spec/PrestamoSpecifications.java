package com.matiasanastasio.biblioteca.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.matiasanastasio.biblioteca.model.entity.Prestamo;
import com.matiasanastasio.biblioteca.model.enums.EstadoPrestamo;

import java.util.UUID;


public class PrestamoSpecifications {
    
    public static Specification<Prestamo> conUsuarioId(UUID usuarioId){
        return (root, query, cb) ->
            cb.equal(root.get("usuario").get("id"), usuarioId);
    }
    
    public static Specification<Prestamo> conLibroId(UUID libroId){
        return (root, query, cb)->
            cb.equal(root.get("libro").get("id"), libroId);
    }

    public static Specification<Prestamo> conEstado(EstadoPrestamo estado){
        return (root, query, cb)->
            cb.equal(root.get("estado"),estado);
    }
}
