package io.github.cursodsouza.libraryapi.exceptions;

import io.github.cursodsouza.libraryapi.controller.dto.ErroResposta;

public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String message) {
        super(message);
    }
}
