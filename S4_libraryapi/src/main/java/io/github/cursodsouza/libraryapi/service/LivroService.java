package io.github.cursodsouza.libraryapi.service;

import io.github.cursodsouza.libraryapi.model.GeneroLivro;
import io.github.cursodsouza.libraryapi.model.Livro;
import io.github.cursodsouza.libraryapi.repository.LivroRepository;
import io.github.cursodsouza.libraryapi.repository.specs.LivroSpecs;
import io.github.cursodsouza.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository repository;
    private final LivroValidator validator;

    public Livro salvar(Livro livro) {
        validator.validar(livro);
        return repository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id) {
        return repository.findById(id);
    }

    public void deletar(Livro livro){
        repository.delete(livro);
    }

    public void atualizar(Livro livro){
        if (livro.getId() == null) {
            throw new IllegalArgumentException("Para atualizar, é necessario que o livro esteja salvo na base de dados!");
        }

        validator.validar(livro);
        repository.save(livro);
    }

    public List<Livro> pesquisa(
            String isbn, String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao){

        // select * from livro where isbn = :isbn and nomeAutor =

        /* Specification<Livro> specs = Specification
                .where(LivroSpecs.isbnEqual(isbn))
                .and(LivroSpecs.tituloLike(titulo))
                .and(LivroSpecs.generoEqual(genero)) */

        // select * from livro where 0 = 0
        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (isbn != null) {
            // query = query and isbn = :isbn
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }

        if (titulo != null) {
            specs = specs.and(LivroSpecs.tituloLike(titulo));
        }

        if (genero != null) {
            specs = specs.and(LivroSpecs.generoEqual(genero));
        }

        if (anoPublicacao != null) {
            specs = specs.and(LivroSpecs.anoPublicacaoEqual(anoPublicacao));
        }

        if (nomeAutor != null) {
            specs = specs.and(LivroSpecs.nomeAutorLike(nomeAutor));
        }

        return repository.findAll(specs);
    }
}
