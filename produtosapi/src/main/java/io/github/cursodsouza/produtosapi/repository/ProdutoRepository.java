package io.github.cursodsouza.produtosapi.repository;

import io.github.cursodsouza.produtosapi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, String> { // String é o tipo do id. Colar aqui o tipo do id da class, nessa caso do Produto
    List<Produto> findByNome(String nome);
}
