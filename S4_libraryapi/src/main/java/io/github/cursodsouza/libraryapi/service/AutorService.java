package io.github.cursodsouza.libraryapi.service;

import io.github.cursodsouza.libraryapi.exceptions.OperacaoNaoPermitida;
import io.github.cursodsouza.libraryapi.model.Autor;
import io.github.cursodsouza.libraryapi.repository.AutorRepository;
import io.github.cursodsouza.libraryapi.repository.LivroRepository;
import io.github.cursodsouza.libraryapi.validator.AutorValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {
    private final AutorRepository repository;
    private final AutorValidator validator;
    private final LivroRepository livroRepository;

    public AutorService(AutorRepository repository, AutorValidator validator, LivroRepository livroRepository){
        this.repository = repository;
        this.validator =  validator;
        this.livroRepository = livroRepository;
    }

    public Autor salvar(Autor autor){
        validator.validar(autor);
        return repository.save(autor);
    }

    public Autor atualizar(Autor autor){
        if (autor.getId() == null) {
            throw new IllegalArgumentException("Para atualizar é necessario que autor esteja na base de dados!");
        }
        validator.validar(autor);
        return repository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID id) {
        return repository.findById(id);
    }

    public void deletar(Autor autor){
        if(possueLivro(autor)){
            throw new OperacaoNaoPermitida("Não é permitido excluir um Autor que possui livro(s) cadastrado(s)!");
        }
        repository.delete(autor);
    }

    public List<Autor> pesquisa(String nome, String nacionalidade){
        if(nome != null && nacionalidade !=null){
            return repository.findByNomeAndNacionalidade(nome, nacionalidade);
        }

        if(nome != null){
            return repository.findByNome(nome);
        }

        if(nacionalidade != null){
            return repository.findByNacionalidade(nacionalidade);
        }

        return repository.findAll();
    }

    private boolean possueLivro(Autor autor){
        return  livroRepository.existsByAutor(autor);
    }
}
