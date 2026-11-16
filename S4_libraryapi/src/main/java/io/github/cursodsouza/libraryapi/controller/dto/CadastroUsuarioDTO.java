package io.github.cursodsouza.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CadastroUsuarioDTO(

        @NotBlank(message = "Login obrigatorio")
        @Size(min = 3, max = 100, message = "Login deve ter entre 3 e 100 caracteres")
        String login,

        @NotBlank(message = "Senha obrigatoria")
        @Size(min = 8, max = 72, message = "Senha deve ter entre 8 e 72 caracteres")
        String senha,

        List<String> roles
) {
}