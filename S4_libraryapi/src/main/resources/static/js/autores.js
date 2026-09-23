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
    limparErrosAutor();

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

//--- Eventos ---
document.getElementById("btn-salvar-autor").addEventListener("click", salvarAutor);

const modalAutor = document.getElementById("modalAutor");
modalAutor.addEventListener("hidden.bs.modal", function () {
    document.getElementById("form-autor").reset();
    limparErrosAutor();
    console.log("Formulário de autor limpo");
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