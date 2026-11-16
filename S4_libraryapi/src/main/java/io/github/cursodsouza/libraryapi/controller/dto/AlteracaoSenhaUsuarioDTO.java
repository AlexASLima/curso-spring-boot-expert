package io.github.cursodsouza.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlteracaoSenhaUsuarioDTO(
        @NotBlank(message = "Nova senha obrigatoria")
        @Size(min = 8, max = 72, message = "Senha deve ter entre 8 e 72 caracteres")
        String novaSenha
) {
}
