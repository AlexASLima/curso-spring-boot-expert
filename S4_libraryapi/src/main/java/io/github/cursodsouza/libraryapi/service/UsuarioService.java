package io.github.cursodsouza.libraryapi.service;

import io.github.cursodsouza.libraryapi.controller.dto.AlteracaoSenhaUsuarioDTO;
import io.github.cursodsouza.libraryapi.controller.dto.AtualizacaoUsuarioDTO;
import io.github.cursodsouza.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.cursodsouza.libraryapi.model.Usuario;
import io.github.cursodsouza.libraryapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public void salvar(Usuario usuario) {
        Usuario usuarioExistente = repository.findByLogin(usuario.getLogin());

        if (usuarioExistente != null) {
            throw new RegistroDuplicadoException("Login já cadastrado!");
        }

        var senha = usuario.getSenha();
        usuario.setSenha(encoder.encode(senha));

        repository.save(usuario);
    }

    public Usuario obterPorLogin(String login){
        return repository.findByLogin(login);
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Optional<Usuario> obterPorId(UUID id) {
        return repository.findById(id);
    }

    public void atualizar(UUID id, AtualizacaoUsuarioDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!")
                );

        Usuario usuarioExistente = repository.findByLogin(dto.login());

        if (usuarioExistente != null && !usuarioExistente.getId().equals(id)) {
            throw new RegistroDuplicadoException("Login já cadastrado!");
        }

        usuario.setLogin(dto.login());
        repository.save(usuario);
    }

    public void alterarSenha(UUID id, AlteracaoSenhaUsuarioDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!")
                );

        String senhaCriptografada = encoder.encode(dto.novaSenha());
        usuario.setSenha(senhaCriptografada);
        repository.save(usuario);
    }

    public void deletar(UUID id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!")
                );

        repository.delete(usuario);
    }
}
