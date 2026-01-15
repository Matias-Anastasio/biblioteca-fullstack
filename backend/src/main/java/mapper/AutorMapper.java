package mapper;

import com.matiasanastasio.biblioteca.dto.autor.AutorResponse;
import com.matiasanastasio.biblioteca.model.entity.Autor;

public class AutorMapper {

    public static AutorResponse toResponse(Autor autor){
        return new AutorResponse(autor.getId(),autor.getNombre(),autor.getApellido());
    }
    
}
