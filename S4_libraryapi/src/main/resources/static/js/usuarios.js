// Endereço da API de usuários
const API_USUARIOS = "/usuarios";

// Carrega os usuários cadastrados no banco
async function carregarUsuarios() {
    try {
        // Faz a requisição GET para a API
        const resposta = await fetch(API_USUARIOS);

        // Verifica se a requisição foi bem-sucedida
        if (!resposta.ok) {
            throw new Error("Erro ao buscar usuários");
        }

        // Converte o JSON recebido em um array JavaScript
        const usuarios = await resposta.json();

        // Ordena os usuários pelo login em ordem alfabética (A → Z)
        usuarios.sort((a, b) => a.login.localeCompare(b.login, "pt-BR"));

        // Localiza o corpo da tabela no HTML
        const tabela = document.getElementById("tabelaUsuarios");

        // Limpa a tabela antes de preenchê-la
        tabela.innerHTML = "";

        // Percorre a lista de usuários
        usuarios.forEach(usuario => {
            // Cria uma nova linha
            const linha = document.createElement("tr");

            // Cria a célula de login
            const colunaLogin = document.createElement("td");
            colunaLogin.textContent = usuario.login;

            // Cria a célula de perfis
            const colunaPerfis = document.createElement("td");
            colunaPerfis.textContent = usuario.roles.join(", ");

            // Cria a célula de ações
            const colunaAcoes = document.createElement("td");
            const botaoEditar = document.createElement("button");
            botaoEditar.type = "button";
            botaoEditar.className = "btn btn-warning btn-sm";
            botaoEditar.textContent = "Editar";
            botaoEditar.addEventListener("click", () => {
                editarUsuario(usuario);
            });
            const botaoAlterarSenha = document.createElement("button");
            botaoAlterarSenha.type = "button";
            botaoAlterarSenha.className = "btn btn-info btn-sm ms-2";
            botaoAlterarSenha.textContent = "Alterar Senha";

            botaoAlterarSenha.addEventListener("click", () => {
                abrirModalSenha(usuario);
            });
            const botaoExcluir = document.createElement("button");
            botaoExcluir.type = "button";
            botaoExcluir.className = "btn btn-danger btn-sm ms-2";
            botaoExcluir.textContent = "Excluir";
            botaoExcluir.addEventListener("click", () => {
                excluirUsuario(usuario);
            });

            colunaAcoes.appendChild(botaoEditar);
            colunaAcoes.appendChild(botaoAlterarSenha);
            colunaAcoes.appendChild(botaoExcluir);

            // Adiciona as células à linha
            linha.appendChild(colunaLogin);
            linha.appendChild(colunaPerfis);
            linha.appendChild(colunaAcoes);

            // Adiciona a linha à tabela
            tabela.appendChild(linha);
        });
    } catch (erro) {
        console.error("Erro ao carregar usuários:", erro);
        alert("Não foi possível carregar os usuários.");
    }
}

// Executa a função quando a página terminar de carregar
document.addEventListener("DOMContentLoaded", carregarUsuarios);

// Captura o envio do formulário de cadastro. *Salvar
document.getElementById("formUsuario").addEventListener("submit", async function (event) {
    // Impede que a página seja recarregada
    event.preventDefault();

    const idUsuario = document.getElementById("idUsuario").value;
    const modoEdicao = idUsuario !== "";
    // Recupera os valores preenchidos
    const login = document.getElementById("login").value.trim();
    const senha = document.getElementById("senha").value;
    const perfil = document.getElementById("roles").value;

    const mensagemErro = document.getElementById("mensagemErroUsuario");

    // Esconde mensagens anteriores
    mensagemErro.classList.add("d-none");
    mensagemErro.textContent = "";

    // Monta o objeto que será enviado para a API
    const usuario = {
        login: login,
        senha: senha,
        roles: [perfil]
    };

    try {
    // Monta o objeto conforme a operação
    const usuario = modoEdicao
        ? {
            login: login
        }
        : {
            login: login,
            senha: senha,
            roles: [perfil]
        };
    // Define o endpoint e o método HTTP
    const url = modoEdicao
        ? `${API_USUARIOS}/${idUsuario}`
        : API_USUARIOS;
    const metodo = modoEdicao ? "PUT" : "POST";

    // Envia os dados para o backend
    const resposta = await fetch(url, {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(usuario)
    });

    // Verifica se ocorreu algum erro
    if (!resposta.ok) {
            const respostaErro = await resposta.json();
            throw new Error(
                respostaErro.mensagem || "Erro ao cadastrar usuário."
            );
        }

        // Fecha o modal
        const modalElemento = document.getElementById("modalUsuario");
        const modal = bootstrap.Modal.getInstance(modalElemento);
        modal.hide();

        // Limpa o formulário
        document.getElementById("formUsuario").reset();

        // Atualiza a tabela
        await carregarUsuarios();

        alert(modoEdicao
            ? "Usuário atualizado com sucesso!"
            : "Usuário cadastrado com sucesso!");
    } catch (erro) {
        console.error("Erro ao cadastrar usuário:", erro);
        mensagemErro.textContent = erro.message;
        mensagemErro.classList.remove("d-none");
    }
});

function editarUsuario(usuario) {
    // Guarda o ID do usuário selecionado
    document.getElementById("idUsuario").value = usuario.id;

    // Preenche o login atual
    document.getElementById("login").value = usuario.login;

    // Na edição, não alteramos a senha
    document.getElementById("senha").value = "";
    document.getElementById("senha").required = false;

    // O perfil será apenas exibido, sem permitir alteração
    const campoPerfis = document.getElementById("roles");
    campoPerfis.value = usuario.roles[0] || "";
    campoPerfis.disabled = true;

    // Atualiza o título do modal
    document.getElementById("tituloModalUsuario").textContent = "Editar Usuário";

    // Limpa mensagens de erro anteriores
    const mensagemErro = document.getElementById("mensagemErroUsuario");
    mensagemErro.textContent = "";
    mensagemErro.classList.add("d-none");

    // Abre o modal
    const modal = bootstrap.Modal.getOrCreateInstance(
        document.getElementById("modalUsuario")
    );

    modal.show();
}

// Abre o modal de alteração de senha
function abrirModalSenha(usuario) {
    // Armazena o ID do usuário selecionado
    document.getElementById("idUsuarioSenha").value = usuario.id;

    // Exibe o login do usuário
    document.getElementById("loginUsuarioSenha").textContent = usuario.login;

    // Limpa a senha digitada anteriormente
    document.getElementById("novaSenha").value = "";

    // Limpa mensagens de erro anteriores
    const mensagemErro = document.getElementById("mensagemErroSenha");
    mensagemErro.textContent = "";
    mensagemErro.classList.add("d-none");

    // Abre o modal
    const modal = bootstrap.Modal.getOrCreateInstance(
        document.getElementById("modalSenha")
    );

    modal.show();
}

// Captura o envio do formulário de alteração de senha
document.getElementById("formSenha").addEventListener("submit", async function (event) {
    // Impede o recarregamento da página
    event.preventDefault();

    // Recupera os dados do formulário
    const idUsuario = document.getElementById("idUsuarioSenha").value;
    const novaSenha = document.getElementById("novaSenha").value;

    const mensagemErro = document.getElementById("mensagemErroSenha");
    // Limpa mensagens anteriores
    mensagemErro.textContent = "";
    mensagemErro.classList.add("d-none");

    // Monta o objeto esperado pelo AlteracaoSenhaDTO
    const dadosSenha = {
        novaSenha: novaSenha
    };

    try {
        // Envia a nova senha para a API
        const resposta = await fetch(`${API_USUARIOS}/${idUsuario}/senha`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosSenha)
        });

        // Verifica se ocorreu algum erro
        if (!resposta.ok) {
            const respostaErro = await resposta.json();
            throw new Error(
                respostaErro.mensagem || "Erro ao alterar senha."
            );
        }

        // Fecha o modal
        const modalElemento = document.getElementById("modalSenha");
        const modal = bootstrap.Modal.getInstance(modalElemento);
        modal.hide();

        // Limpa o formulário
        document.getElementById("formSenha").reset();

        alert("Senha alterada com sucesso!");
    } catch (erro) {
        console.error("Erro ao alterar senha:", erro);
        mensagemErro.textContent = erro.message;
        mensagemErro.classList.remove("d-none");
    }
});

// Exclui um usuário após confirmação
async function excluirUsuario(usuario) {
    // Solicita confirmação antes de excluir
    const confirmou = confirm(
        `Deseja realmente excluir o usuário "${usuario.login}"?`
    );

    // Se clicar em Cancelar, interrompe a função
    if (!confirmou) {
        return;
    }

    try {
        // Envia a requisição DELETE para o backend
        const resposta = await fetch(
            `${API_USUARIOS}/${usuario.id}`,
            {
                method: "DELETE"
            }
        );

        // Verifica se ocorreu algum erro
        if (!resposta.ok) {
            const respostaErro = await resposta.json();
            throw new Error(
                respostaErro.mensagem || "Erro ao excluir usuário."
            );
        }

        // Informa que a exclusão foi realizada
        alert("Usuário excluído com sucesso!");

        // Atualiza a tabela após excluir
        await carregarUsuarios();
    } catch (erro) {
        console.error("Erro ao excluir usuário:", erro);
        alert(erro.message);
    }
}

document.getElementById("modalUsuario").addEventListener("hidden.bs.modal", function () {
        document.getElementById("formUsuario").reset();
        document.getElementById("idUsuario").value = "";
        document.getElementById("senha").required = true;
        document.getElementById("roles").disabled = false;
        document.getElementById("tituloModalUsuario").textContent ="Novo Usuário";
        const mensagemErro =
            document.getElementById("mensagemErroUsuario");

        mensagemErro.textContent = "";
        mensagemErro.classList.add("d-none");
    });