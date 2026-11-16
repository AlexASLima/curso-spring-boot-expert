package io.github.cursodsouza.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtualizacaoUsuarioDTO(

        @NotBlank(message = "Login obrigatorio")
        @Size(min = 3, max = 100,
                message = "Login deve ter entre 3 e 100 caracteres")
        String login

) {
}