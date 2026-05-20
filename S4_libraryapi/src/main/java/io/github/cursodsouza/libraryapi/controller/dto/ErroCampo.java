package io.github.cursodsouza.libraryapi.controller.dto;

import org.springframework.http.HttpStatus;
import java.util.List;

public record ErroCampo (String campo, String erro){
    public static ErroResposta respostaPadra(String mensagem){
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }

    public static ErroResposta conflito(String mensagem){
        return  new ErroResposta(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }
}
