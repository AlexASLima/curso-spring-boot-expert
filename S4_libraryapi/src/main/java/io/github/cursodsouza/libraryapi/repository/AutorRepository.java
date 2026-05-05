package io.github.cursodsouza.libraryapi.repository;

import io.github.cursodsouza.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

}
