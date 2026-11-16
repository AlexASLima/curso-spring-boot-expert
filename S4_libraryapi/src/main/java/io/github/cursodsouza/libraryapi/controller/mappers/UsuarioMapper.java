package io.github.cursodsouza.libraryapi.controller.mappers;

import io.github.cursodsouza.libraryapi.controller.dto.CadastroUsuarioDTO;
import io.github.cursodsouza.libraryapi.controller.dto.ResultadoUsuarioDTO;
import io.github.cursodsouza.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario toEntity(CadastroUsuarioDTO dto);

    ResultadoUsuarioDTO toDTO(Usuario usuario);
}