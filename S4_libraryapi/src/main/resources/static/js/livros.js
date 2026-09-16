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

async function carregarAutores() {
    console.log("Buscando autores...");

    const response = await fetch("/autores");
    console.log("Status autores:", response.status);

    const autores = await response.json();
    console.log("Resposta autores:", autores);

    const selectAutor = document.getElementById("autor");
    console.log("Select encontrado:", selectAutor);

    selectAutor.innerHTML = '<option value="">Selecione...</option>';
    autores.forEach(autor => {
        console.log("Adicionando autor:", autor.nome);
        const option = document.createElement("option");
        option.value = autor.id;
        option.textContent = autor.nome;

        selectAutor.appendChild(option);
    });
}

carregarLivros();
carregarAutores();

document.getElementById("btn-salvar").addEventListener("click", salvarLivro);
async function salvarLivro() {
    const livro = {
        isbn: document.getElementById("isbn").value,
        titulo: document.getElementById("titulo").value,
        dataPublicacao: document.getElementById("dataPublicacao").value,
        genero: document.getElementById("genero").value,
        preco: Number(document.getElementById("preco").value),
        idAutor: document.getElementById("autor").value
    };

    console.log("Livro que será enviado:", livro);
    const response = await fetch("/livros", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(livro)
    });

    console.log("Status do cadastro:", response.status);
    if (!response.ok) {
        const erro = await response.text();
        console.log("Erro retornado pelo servidor:", erro);
    }
}