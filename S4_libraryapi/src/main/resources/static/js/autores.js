console.log("JavaScript de autores carregado!");

async function carregarAutores() {
    console.log("Buscando autores...");
    const response = await fetch("/autores");
    console.log("Status da requisição:", response.status);

    const autores = await response.json();
    console.log("Resposta da API:", autores);

    const tabela = document.getElementById("tabela-autores");
    tabela.innerHTML = "";
    autores.forEach(autor => {
        const linha = document.createElement("tr");
        linha.innerHTML = `
            <td>${autor.nome}</td>
            <td>${autor.dataNascimento}</td>
            <td>${autor.nacionalidade}</td>
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

carregarAutores();

async function salvarAutor() {
    const autor = {
        nome: document.getElementById("nome").value,
        dataNascimento: document.getElementById("dataNascimento").value,
        nacionalidade: document.getElementById("nacionalidade").value
    };
    console.log("Autor que será enviado:", autor);
    const response = await fetch("/autores", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(autor)
    });
    console.log("Status do cadastro:", response.status);

    if (response.ok) {
       console.log("Autor cadastrado com sucesso!");
       const modalElement = document.getElementById("modalAutor");
       const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
       modal.hide();

       await carregarAutores();
    }
}

//--- Eventos ---
document.getElementById("btn-salvar-autor").addEventListener("click", salvarAutor);

const modalAutor = document.getElementById("modalAutor");
modalAutor.addEventListener("hidden.bs.modal", function () {
    document.getElementById("form-autor").reset();
    console.log("Formulário de autor limpo");
});