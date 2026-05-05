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

		AutorRepository repository = context.getBean(AutorRepository.class);
		exemploSalvarRegistro(repository);
	}

	public static void exemploSalvarRegistro(AutorRepository autorRepository) {
		Autor autor = new Autor();
		autor.setNome("José");
		autor.setNacionalidade("Brasileira");
		autor.setDataNascimento(LocalDate.of(1950, 1, 31));

		var autorSalvo = autorRepository.save(autor);
		System.out.println("Autor Salvo:" + autorSalvo);
	}
}
