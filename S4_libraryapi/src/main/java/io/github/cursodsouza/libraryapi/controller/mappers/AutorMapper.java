package io.github.cursodsouza.libraryapi.controller.mappers;

import io.github.cursodsouza.libraryapi.controller.dto.AutorDTO;
import io.github.cursodsouza.libraryapi.model.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {
    //@Mapping(source "nome", target = "nomeAutor') // Para DTO com nome diferente da Entidade. Ex.: NomeAuto na entidade. Quando igual não necessita dessa linha.
    Autor toEntity(AutorDTO dto);

    AutorDTO toDTO(Autor autor);
}
