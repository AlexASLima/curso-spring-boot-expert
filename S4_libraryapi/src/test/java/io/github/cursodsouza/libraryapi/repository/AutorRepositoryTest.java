package io.github.cursodsouza.libraryapi.repository;

import io.github.cursodsouza.libraryapi.model.Autor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {
    @Autowired
    AutorRepository repository;

    @Test
    public void salvarTest(){
        Autor autor = new Autor();
        autor.setNome("Alex");
        autor.setNacionalidade("USA");
        autor.setDataNascimento(LocalDate.of(1982, 1, 31));

        var autorSalvo = repository.save(autor);
        System.out.println("Autor Salvo:" + autorSalvo);
    }

    @Test
    public void atualizarTest(){
        var id = UUID.fromString("3904a725-b44d-43a6-859f-ddada66b41e9");
        Optional<Autor> possivelAutor = repository.findById(id);

        if(possivelAutor.isPresent()){
            Autor autorEncontrado =  possivelAutor.get();
            System.out.println("Dados do Autor:");
            System.out.println(autorEncontrado);

            autorEncontrado.setNome("Maria");
            autorEncontrado.setDataNascimento(LocalDate.of(2021, 11, 27));

            repository.save(autorEncontrado); // Tendo o objeto o save funciona com update.
        }
    }

    @Test
    public void listarTest(){
        List<Autor> lista = repository.findAll();
        lista.forEach(System.out::println);
    }

    @Test
    public void countTest(){
        System.out.println("Contagem de autores: " + repository.count());
    }

    @Test
    public void deletePorIdTest(){
        var id = UUID.fromString("xxx");
        repository.deleteById(id);
    }

    @Test
    public void deleteTest(){
        var id = UUID.fromString("xxx");
        var maria = repository.findById(id).get();
        repository.delete(maria);
    }

}
