package io.github.cursodsouza.libraryapi.controller.dto;

import java.util.List;
import java.util.UUID;

public record ResultadoUsuarioDTO(
        UUID id,
        String login,
        List<String> roles
) {
}
