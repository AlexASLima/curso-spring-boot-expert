package io.github.cursodsouza.libraryapi;

import io.github.cursodsouza.libraryapi.model.Autor;
import io.github.cursodsouza.libraryapi.repository.AutorRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class LibraryapiApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(LibraryapiApplication.class, args);

	}
}
