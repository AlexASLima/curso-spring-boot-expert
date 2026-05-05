package io.github.cursodsouza.libraryapi.repository;

import io.github.cursodsouza.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

}
