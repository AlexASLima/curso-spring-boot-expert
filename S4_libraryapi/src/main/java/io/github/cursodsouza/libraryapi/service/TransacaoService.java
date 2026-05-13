package io.github.cursodsouza.libraryapi.service;

import io.github.cursodsouza.libraryapi.model.Autor;
import io.github.cursodsouza.libraryapi.model.GeneroLivro;
import io.github.cursodsouza.libraryapi.model.Livro;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.cursodsouza.libraryapi.repository.AutorRepository;
import io.github.cursodsouza.libraryapi.repository.LivroRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LivroRepository livroRepository;

    /// livro (titulo,..., nome_arquivo) -> id.png
    @Transactional
    public void salvarLivroComFoto(){
        // salva o livro
        // repository.save(livro);

        // pega o id do livro = livro.getId();
        // var id = livro.getId();

        // salvar foto do livro -> bucket na nuvem
        // bucketService.salvar(livro.getFoto(), id + ".png");

        // atualizar o nome arquivo que foi salvo
        // livro.setNomeArquivoFoto(id + ".png");

        //*** Não necessita novamente do repository.save(livro) aqui para atualizar com o nomeArquivoFoto, pois no
        // primeiro .save(...), entra-se no estado managed, é atualizado acima e por fim com o commit no fim desse
        // funcao devido o @Transation, o registro estará atualizado com a info acima. Explicacao do slide da aula 71
    }

    @Transactional // essa notação só pode em metodo public
    public void atualizacaoSemAtualizar(){
        var livro = livroRepository
                .findById(UUID.fromString("5c87ab1b-09f7-4692-a337-ef05e8d0a822"))
                .orElse(null); // Da o find vai para o estado managed (explicações desses comentarios na aula 71)

        livro.setDataPublicacao(LocalDate.of(2024,6,1));

        // Por fim aqui é dado o commit do @Transaction (fim da função), então não necessita dar o salvar (.save/update()).
    }

    @Transactional
    public void executar(){
        // salva o autor
        Autor autor = new Autor();
        autor.setNome("Teste Francisco");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        autorRepository.save(autor);

        // salva o livro
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Teste Livro do Francisco");
        livro.setDataPublicacao(LocalDate.of(1980, 1, 2));

        livro.setAutor(autor);

        livroRepository.save(livro);

        if(autor.getNome().equals("Teste Francisco")){
            throw new RuntimeException("Rollback!"); // Devido o @Transactional, não chega no fim da funcao e não realiza o commit
        }
    }
}
