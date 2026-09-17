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
    esconderErro();
    limparErrosCampos();

    const livro = {
        isbn: document.getElementById("isbn").value,
        titulo: document.getElementById("titulo").value,
        dataPublicacao: document.getElementById("dataPublicacao").value,
        genero: document.getElementById("genero").value,
        preco: Number(document.getElementById("preco").value),
        idAutor: document.getElementById("autor").value
    };

    console.log("Livro que será enviado to save:", livro);
    const response = await fetch("/livros", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(livro)
    });

    console.log("Status do cadastro:", response.status);
    if (response.ok) {
        console.log("Livro cadastrado com sucesso!");

        document.getElementById("form-livro").reset(); // limpa os campos

        const modalElement = document.getElementById("modalLivro");
        const modal = bootstrap.Modal.getInstance(modalElement);
        modal.hide();

        await carregarLivros();
    } else {
        const respostaErro = await response.json();
        console.log("Erro retornado pelo servidor:", respostaErro);
        let mensagem = respostaErro.mensagem;

        if (respostaErro.erros && respostaErro.erros.length > 0) {
            respostaErro.erros.forEach(erro => {
                mostrarErroCampo(erro.campo, erro.erro);

                mensagem += `\n${erro.campo}: ${erro.erro}`;
        });

        mostrarErro(mensagem);
    }

    }

    function mostrarErro(mensagem) {
        const alerta = document.getElementById("alerta-erro");
        alerta.textContent = mensagem;
        alerta.classList.remove("d-none");
    }

    function esconderErro() {
        const alerta = document.getElementById("alerta-erro");
        alerta.textContent = "";
        alerta.classList.add("d-none");
    }

function obterIdCampo(nomeCampo) {
    if (nomeCampo === "idAutor") {
        return "autor";
    }

    return nomeCampo;
}

function mostrarErroCampo(nomeCampo, mensagem) {
    const idCampo = obterIdCampo(nomeCampo);
    const campo = document.getElementById(idCampo);

    if (!campo) {
        console.log("Campo não encontrado no HTML:", idCampo);
        return;
    }

    campo.classList.add("is-invalid");
    const feedback = campo.parentElement.querySelector(".invalid-feedback");
    if (feedback) {
        feedback.textContent = mensagem;
    }
}

    function limparErrosCampos() {
        const camposInvalidos = document.querySelectorAll(".is-invalid");
        camposInvalidos.forEach(campo => {
            campo.classList.remove("is-invalid");
        });

        const mensagensErro = document.querySelectorAll(".invalid-feedback");
        mensagensErro.forEach(mensagem => {
            mensagem.textContent = "";
        });
    }
}