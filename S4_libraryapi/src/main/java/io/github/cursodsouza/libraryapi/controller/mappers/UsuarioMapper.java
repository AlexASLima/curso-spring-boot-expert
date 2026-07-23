package io.github.cursodsouza.libraryapi.controller.mappers;

import io.github.cursodsouza.libraryapi.controller.dto.UsuarioDTO;
import io.github.cursodsouza.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario toEntity(UsuarioDTO dto);
}
