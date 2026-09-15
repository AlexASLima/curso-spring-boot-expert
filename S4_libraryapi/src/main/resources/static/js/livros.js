console.log("JavaScript carregado!");

async function carregarLivros() {

    console.log("Buscando livros...");

    const response = await fetch("/livros");

    console.log("Status da requisição:", response.status);

    const resultado = await response.json();

    console.log("Resposta da API:", resultado);

    const livros = resultado.content;

    const tabela = document.getElementById("tabela-livros");

    tabela.innerHTML = "";

    livros.forEach(livro => {

        const linha = document.createElement("tr");

        linha.innerHTML = `
            <td>${livro.isbn}</td>
            <td>${livro.titulo}</td>
            <td>${livro.autor.nome}</td>
            <td>${livro.genero}</td>
            <td>R$ ${livro.preco.toFixed(2)}</td>
            <td>
                <button class="btn btn-sm btn-warning">
                    Editar
                </button>

                <button class="btn btn-sm btn-danger">
                    Excluir
                </button>
            </td>
        `;

        tabela.appendChild(linha);
    });
}

carregarLivros();