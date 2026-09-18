console.log("JavaScript carregado!");

let idLivroEdicao = null;
let idLivroExcluir = null;

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
                <button class="btn btn-sm btn-warning"
                        onclick="editarLivro('${livro.id}')">
                    Editar
                </button>

                <button class="btn btn-sm btn-danger"
                        onclick="abrirModalExcluir('${livro.id}')">
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

async function salvarLivro() {
    //esconderErro(); Agora estão no evento de fechar o form Novo Livro.
    //limparErrosCampos();

    const livro = {
        isbn: document.getElementById("isbn").value,
        titulo: document.getElementById("titulo").value,
        dataPublicacao: document.getElementById("dataPublicacao").value,
        genero: document.getElementById("genero").value,
        preco: Number(document.getElementById("preco").value),
        idAutor: document.getElementById("autor").value
    };
    console.log("Livro que será enviado to save_update:", livro);
    console.log("Modo edição?", idLivroEdicao);

    let url;
    let metodo;
    if (idLivroEdicao === null) {
        url = "/livros";
        metodo = "POST";
    } else {
        url = `/livros/${idLivroEdicao}`;
        metodo = "PUT";
    }

    console.log("URL:", url);
    console.log("Método:", metodo);

    const response = await fetch(url, {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(livro)
    });

    console.log("Status do cadastro:", response.status);
    if (response.ok) {
        console.log("Livro cadastrado_atualizado com sucesso!");

        const modalElement = document.getElementById("modalLivro");
        const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
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
}

function abrirModalExcluir(id) {
    idLivroExcluir = id;

    console.log("Livro selecionado para exclusão:", idLivroExcluir);
    const modalElement = document.getElementById("modalExcluir");
    const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
    modal.show();
}

async function editarLivro(id) {
    idLivroEdicao = id;
    console.log("Livro em edição:", idLivroEdicao);

    const response = await fetch(`/livros/${id}`);
    console.log("Status da busca:", response.status);
    const livro = await response.json();
    console.log("Livro encontrado:", livro);

    document.getElementById("isbn").value = livro.isbn;
    document.getElementById("titulo").value = livro.titulo;
    document.getElementById("dataPublicacao").value = livro.dataPublicacao;
    document.getElementById("genero").value = livro.genero;
    document.getElementById("preco").value = livro.preco;
    document.getElementById("autor").value = livro.autor.id;

    document.getElementById("titulo-modal-livro").textContent = "Editar Livro";

    const modalElement = document.getElementById("modalLivro");
    const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
    modal.show();
}

async function excluirLivro(id) {
    console.log("ID do livro para excluir:", id);
    const response = await fetch(`/livros/${id}`, {
        method: "DELETE"
    });

    console.log("Status da exclusão:", response.status);
    if (response.ok) {
        console.log("Livro excluído com sucesso!");
        const modalElement = document.getElementById("modalExcluir");
        const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
        modal.hide();

        await carregarLivros();
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

    // ==========================
    // EVENTOS / INICIALIZAÇÃO
    // ==========================

    // Evento do botão Salvar
    document.getElementById("btn-salvar").addEventListener("click", salvarLivro);

    // Evento disparado quando o modal é fechado
    const modalLivro = document.getElementById("modalLivro");
    modalLivro.addEventListener("hidden.bs.modal", function () {
        // Limpa os campos
        document.getElementById("form-livro").reset();
        // Remove campos vermelhos e mensagens
        limparErrosCampos();
        // Esconde o alerta vermelho
        esconderErro();

        idLivroEdicao = null;
        document.getElementById("titulo-modal-livro").textContent = "Novo Livro";
    });

    document.getElementById("btn-novo-livro").addEventListener("click", prepararNovoLivro);
    function prepararNovoLivro() {
        idLivroEdicao = null;
        document.getElementById("titulo-modal-livro").textContent ="Novo Livro";

        console.log("Modo cadastro");
    }

    document.getElementById("btn-confirmar-exclusao").addEventListener("click", function () {
        console.log("Confirmando exclusão:", idLivroExcluir);
        excluirLivro(idLivroExcluir);
    });

    const modalExcluir = document.getElementById("modalExcluir");
    modalExcluir.addEventListener("hidden.bs.modal", function () {
        idLivroExcluir = null;
        console.log("ID para exclusão limpo:", idLivroExcluir);
    });