package io.github.cursodsouza.libraryapi.controller;

import io.github.cursodsouza.libraryapi.controller.dto.AlteracaoSenhaUsuarioDTO;
import io.github.cursodsouza.libraryapi.controller.dto.AtualizacaoUsuarioDTO;
import io.github.cursodsouza.libraryapi.controller.dto.CadastroUsuarioDTO;
import io.github.cursodsouza.libraryapi.controller.dto.ResultadoUsuarioDTO;
import io.github.cursodsouza.libraryapi.controller.mappers.UsuarioMapper;
import io.github.cursodsouza.libraryapi.model.Usuario;
import io.github.cursodsouza.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid CadastroUsuarioDTO dto) {
        var usuario = mapper.toEntity(dto);
        service.salvar(usuario);
    }

    @GetMapping
    public List<ResultadoUsuarioDTO> listarTodos() {
        List<Usuario> usuarios = service.listarTodos();

        return usuarios.stream()
                .map(mapper::toDTO)
                .toList();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultadoUsuarioDTO> obterDetalhes(
            @PathVariable("id") String id) {
        var idUsuario = UUID.fromString(id);

        return service.obterPorId(idUsuario)
                .map(usuario -> {
                    ResultadoUsuarioDTO dto = mapper.toDTO(usuario);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(
            @PathVariable("id") UUID id,
            @RequestBody @Valid AtualizacaoUsuarioDTO dto) {

        service.atualizar(id, dto);
    }

    @PatchMapping("{id}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(
            @PathVariable("id") UUID id,
            @RequestBody @Valid AlteracaoSenhaUsuarioDTO dto) {

        service.alterarSenha(id, dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable("id") UUID id) {
        service.deletar(id);
    }
}
