package io.github.cursodsouza.libraryapi.controller;

import io.github.cursodsouza.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.cursodsouza.libraryapi.controller.dto.ErroResposta;
import io.github.cursodsouza.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.cursodsouza.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController {
    private final LivroService service;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        try {
            /* Autor autorEntidade = autor.mapearParaAutor();
            service.salvar(autorEntidade);

            //http://localhost:8080/autores/02cda449-b1f0-4115-b088-34410069e6e0 (no headers de resposta resposta)
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(autorEntidade.getId())
                    .toUri();

            return ResponseEntity.created(location).build(); */
            return  ResponseEntity.ok(dto);
        } catch (RegistroDuplicadoException e){
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return  ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }
}
