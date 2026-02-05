package com.matiasanastasio.biblioteca.repository.spec;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.matiasanastasio.biblioteca.model.entity.Autor;
import com.matiasanastasio.biblioteca.model.entity.Libro;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class LibroSpecifications {
    
    public static Specification<Libro> conAutorId(Long autorId){
        return (root,query,cb) -> cb.equal(root.get("autor").get("id"), autorId);
    }
    
    public static Specification<Libro> soloDisponibles(){
        return (root, query, cb)-> cb.greaterThan(root.get("ejemplaresDisponibles"),0);
    }
    
    public static Specification<Libro> buscarGlobal(String q){
        if(q == null || q.isBlank()){
            return (root,query,cb)-> cb.conjunction();
        }

        String normalized = q.trim().toLowerCase(Locale.ROOT);

        List<String> tokens = Arrays.stream(normalized.split("\\s+"))
            .filter(t->!t.isBlank())
            .collect(Collectors.toList());

        return (root, query, cb)-> {
            Join<Libro, Autor> autor = root.join("autor", JoinType.INNER);

            query.distinct(true);

            return cb.and(tokens.stream().map(token->{
                String like = "%"+token+"%";

                return cb.or(
                    cb.like(cb.lower(root.get("titulo")),like),
                    cb.like(cb.lower(autor.get("nombre")),like),
                    cb.like(cb.lower(autor.get("apellido")),like),
                    cb.like(cb.lower(root.get("isbn")),like)
                );
            }).toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }
}
