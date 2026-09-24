console.log("JavaScript de autores carregado!");
let idAutorEdicao = null;
let idAutorExcluir = null;

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
                <button class="btn btn-sm btn-warning"
                    onclick="editarAutor('${autor.id}')">
                    Editar
                </button>
                <button class="btn btn-sm btn-danger"
                    onclick="abrirModalExcluirAutor('${autor.id}')">
                    Excluir
                </button>
            </td>
        `;
        tabela.appendChild(linha);
    });
}

carregarAutores();

async function salvarAutor() {
    limparErrosAutor();

    const autor = {
        nome: document.getElementById("nome").value,
        dataNascimento: document.getElementById("dataNascimento").value,
        nacionalidade: document.getElementById("nacionalidade").value
    };
    console.log("Autor que será enviado:", autor);
    console.log("Autor em edição:", idAutorEdicao);
    let url;
    let metodo;

    if (idAutorEdicao === null) {
        url = "/autores";
        metodo = "POST";
    } else {
        url = `/autores/${idAutorEdicao}`;
        metodo = "PUT";
    }
    console.log("URL:", url);
    console.log("Método:", metodo);

    const response = await fetch(url, {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(autor)
    });

    console.log("Status do cadastro:", response.status);
    if (response.ok) {
        if (idAutorEdicao === null) {
            console.log("Autor cadastrado com sucesso!");
        } else {
            console.log("Autor atualizado com sucesso!");
        }
       const modalElement = document.getElementById("modalAutor");
       const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
       modal.hide();

       await carregarAutores();
    } else {
          const respostaErro = await response.json();
          console.log("Erro retornado pelo servidor:", respostaErro);
          let mensagem = respostaErro.mensagem;
          if (respostaErro.erros && respostaErro.erros.length > 0) {
              respostaErro.erros.forEach(erro => {
                  mostrarErroCampoAutor(
                      erro.campo,
                      erro.erro
                  );
                  mensagem += `\n${erro.campo}: ${erro.erro}`;
              });
          }
          mostrarErroAutor(mensagem);
      }
}

async function editarAutor(id) {
    idAutorEdicao = id;
    console.log("Autor em edição:", idAutorEdicao);
    const response = await fetch(`/autores/${id}`);
    console.log("Status da busca:", response.status);

    const autor = await response.json();
    console.log("Autor encontrado:", autor);
    document.getElementById("nome").value = autor.nome;
    document.getElementById("dataNascimento").value = autor.dataNascimento;
    document.getElementById("nacionalidade").value = autor.nacionalidade;

    const modalElement = document.getElementById("modalAutor");
    const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
    modal.show();
}

async function excluirAutor(id) {
    console.log("ID do autor para excluir:", id);
    const response = await fetch(`/autores/${id}`, {
        method: "DELETE"
    });
    console.log("Status da exclusão:", response.status);
    if (response.ok) {
        console.log("Autor excluído com sucesso!");
        const modalElement = document.getElementById("modalExcluirAutor");
        const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
        modal.hide();

        await carregarAutores();
    } else {
       const alerta = document.getElementById("alerta-erro-exclusao-autor");
       alerta.textContent =
        "Não foi possível excluir o autor. Verifique se ele está vinculado a algum livro.";
       alerta.classList.remove("d-none");
    }
}

function abrirModalExcluirAutor(id) {
    idAutorExcluir = id;
    console.log("Autor selecionado para exclusão:", idAutorExcluir);

    const modalElement = document.getElementById("modalExcluirAutor");
    const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
    modal.show();
}

//--- Eventos ---
document.getElementById("btn-salvar-autor").addEventListener("click", salvarAutor);

document.getElementById("btn-confirmar-exclusao-autor").addEventListener("click", function () {
     console.log("Confirmando exclusão:", idAutorExcluir);
     excluirAutor(idAutorExcluir);
});

const modalAutor = document.getElementById("modalAutor");
modalAutor.addEventListener("hidden.bs.modal", function () {
    document.getElementById("form-autor").reset();
    limparErrosAutor();
    idAutorEdicao = null;
    console.log("Formulário de autor limpo");
});

modalExcluirAutor.addEventListener("hidden.bs.modal", function () {
    idAutorExcluir = null;
    const alerta = document.getElementById(
        "alerta-erro-exclusao-autor"
    );
    alerta.textContent = "";
    alerta.classList.add("d-none");
    console.log("ID de exclusão do autor limpo");
});

function mostrarErroAutor(mensagem) {
    const alerta = document.getElementById("alerta-erro-autor");
    alerta.textContent = mensagem;
    alerta.classList.remove("d-none");
}

function mostrarErroCampoAutor(nomeCampo, mensagem) {
    const campo = document.getElementById(nomeCampo);
    if (!campo) {
        console.log("Campo não encontrado no HTML:", nomeCampo);
        return;
    }
    campo.classList.add("is-invalid");
    const feedback = campo.parentElement.querySelector(".invalid-feedback");
    if (feedback) {
        feedback.textContent = mensagem;
    }
}

function limparErrosAutor() {
    const alerta = document.getElementById("alerta-erro-autor");
    alerta.textContent = "";
    alerta.classList.add("d-none");

    const camposInvalidos = document.querySelectorAll("#form-autor .is-invalid");
    camposInvalidos.forEach(campo => {
        campo.classList.remove("is-invalid");
    });
    const mensagens = document.querySelectorAll("#form-autor .invalid-feedback");
    mensagens.forEach(mensagem => {mensagem.textContent = "";});
}