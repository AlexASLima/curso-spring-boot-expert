package io.github.cursodsouza.libraryapi.validator;

import io.github.cursodsouza.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.cursodsouza.libraryapi.model.Autor;
import io.github.cursodsouza.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {
    private AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor){
        if(existeAutorCadastrado(autor)){
            throw new RegistroDuplicadoException("Autor já cadastrado!");
        }
    }

    private boolean existeAutorCadastrado(Autor autor){
        Optional<Autor> autorEncontrado = repository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()
        );

        if(autor.getId() == null){ // Cadastrando, não atualizando.
            return autorEncontrado.isPresent();
        }
              // Teste se não é o autor que esta atualizando
        return !autor.getId().equals(autorEncontrado.get().getId()) && autorEncontrado.isPresent();
    }
}
