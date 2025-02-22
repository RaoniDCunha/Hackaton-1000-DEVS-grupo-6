//realiza o processamento do conteudo no frontend assim que a pagina for carregada
window.onload = processarCarregamentoPagina;

//verifica se tem alguma mensagem para exibir para o usuario
//e realiza o carregamento dos dados dos usuários a partir da API de consulta

document.addEventListener("DOMContentLoaded", function () {
  processarCarregamentoPagina();
});

function processarCarregamentoPagina() {
  carregarPacientes();
}

//realiza o carregamento dos dados do usuário no backend
async function carregarPacientes() {
  try {
    //faz a chamada na API
    const response = await fetch("http://localhost:3000/paciente/consultar");

    //se a chamada retornar algum erro
    if (!response.ok) {
      //determina o codigo da mensagem base no status do http
      const codigoStatus = determinarCodigoStatus(response);

      //determina a mensagem a ser exibida
      mensagem = retornarMensagem(codigoStatus);

      //exibe o modal com a mensagem
      abrirModalMensagem("Consultar Usuários", mensagem);

      //cancela a execução do restante da funcao
      return;
    }

    //se chegou ate aqui a API executou com sucesso
    //converte os dados de JSON para objeto Java Script
    const usuarios = await response.json();

    //adiciona os dados dos usuários na div de listagem de usuarios
    adicionarUsuariosTabelaHtml(usuarios);
  } catch (error) {
    abrirModalMensagem("Consultar Usuários", "Erro: " + error.message);
  }
}

//realiza o cadastro do usuario, executando a API no backend
async function realizarCadastro(evento) {
  evento.preventDefault();

  // Obtém os valores dos campos do formulário
  const nome = document.getElementById("nome").value;
  const cpf = document.getElementById("cpf").value;
  const sexo = document.getElementById("sexo").value;
  const data_nascimento = document.getElementById("data_nascimento").value;

  // Constrói a URL com os query parameters

  try {
    // Realiza a chamada da API com os query parameters
    const httpResponse = await fetch(`http://localhost:3000/paciente/inserir?nome=${nome}&cpf=${cpf}&sexo=${sexo}&data_nascimento=${data_nascimento}`, {
      method: "POST",
      headers: { "Content-type": "application/x-www-form-urlencoded" },
    });

    // Verifica se a resposta da API foi bem-sucedida
    if (!httpResponse.ok) {
      alert("Erro ao cadastrar: " + httpResponse.status +", erro:"+ httpResponse.statusText);
      return;
    }
    // Exibe mensagem de sucesso e recarrega a lista de pacientes
    alert("Cadastrado com sucesso");
  } catch (error) {
    alert("Erro ao cadastrar: " + error.message);
  }
}

//realiza a exclusão do usuário no backend
async function excluirUsuario(botao) {
  let mensagem;

  try {
    //obtem o id do usuario a ser excluido
    const id = botao.getAttribute("data-id");

    //constroi o objeto contendo o conteudo a ser inserido na requisicao http
    const conteudoHttp = {
      method: "DELETE",
    };

    //realiza a chamada da API
    const response = await fetch(
      `http://localhost:3000/paciente/excluir/${id}`,
      conteudoHttp
    );

    //determina o codigo de status baseado no http response
    const codigoStatus = determinarCodigoStatus(response);

    //determina a mensagem a ser exibida para o usuario
    mensagem = retornarMensagem(codigoStatus);
  } catch (error) {
    mensagem = "Erro ao excluir usuário: " + error.message;
  }

  //fecha o modal excluir
  fecharModalExcluir();
  //abre o modal de status para informar o resultado da exclusao, passando os parametros
  abrirModalMensagem("Exclusão Usuário", mensagem);
}

function adicionarUsuariosTabelaHtml(usuarios) {
  //obtem a div que contem os dados dos usuarios
  const divUsuarios = document.getElementById("dadosUsuarios");

  //limpa os dados da div para atualizar com os novos dados
  divUsuarios.innerHTML = "";

  //acessa cada elemento do vetor de usuarios e executa a funcao
  //adicionarLinhaTabelaHtml com o usuario obtido
  for (let i = 0; i < usuarios.length; i++) {
    //cria uma nova linha da tabela html
    const linhaTabela = document.createElement("tr");

    //monta o conteudo da linha
    linhaTabela.innerHTML = `
    <td>${usuarios[i].id}</td>
    <td>${usuarios[i].nome}</td>
    <td>${usuarios[i].cpf}</td>
    <td>${usuarios[i].sexo}</td>
    <td>${usuarios[i].data_nascimento}</td>
    <td>
      <a href="../cadastro/editar/paciente.html?id=${usuarios[i].id}">
        <button class="main_btn">Editar</button>
      </a>
      <button class="main_btn_light" onclick="abrirModalExcluir('${usuarios[i].id}')">Excluir</button>
    </td>
`;

    //adiciona a nova linha na div de usuarios
    divUsuarios.appendChild(linhaTabela);
  }
}

//padroniza o codigo de status dentro da aplicacao frontend
function determinarCodigoStatus(response) {
  if (!response.ok) return statusOperacao.ENDPOINT_NAO_ENCONTRADO;
  else if (response.status == 204) return statusOperacao.ID_NAO_ENCONTRADO;
  else if (response.status == 200) return statusOperacao.SUCESSO;
  else return statusOperacao.ERRO_GERAL;
}

//gera uma mensagem amigavel para o usuario baseado no codigo de status
function retornarMensagem(codigoStatus) {
  let mensagem = "Status informado desconhecido.";

  switch (codigoStatus) {
    case statusOperacao.SUCESSO:
      mensagem = "Operação realizada com sucesso!";
      break;
    case statusOperacao.ID_NAO_INFORMADO:
      mensagem = "Erro ao executar operação, ID não informado.";
      break;
    case statusOperacao.ERRO_GERAL:
      mensagem = "Erro ao processar a operação.";
      break;
    case statusOperacao.ID_NAO_ENCONTRADO:
      mensagem = "Erro ao executar operação, ID informado não encontrado.";
      break;
    case statusOperacao.ENDPOINT_NAO_ENCONTRADO:
      mensagem = "Erro ao executar operação, Endpoint não encontrado.";
      break;
    default:
      break;
  }

  //fecha o modal excluir
  return mensagem;
}

//realiza a obtencao do parametro id caso seja passado na URL
function obterParametroId() {
  // Obtem o objeto que representa a URL que esta no browser
  const url = new URL(window.location.href);

  // Converte o objeto URL para URLSearchParams para acessar os parametros da URL
  const parametros = new URLSearchParams(url.search);

  // Obtem o valor do parâmetro 'id'
  return Number(parametros.get("id"));
}

//realiza a obtencao do parametro status caso seja passado na URL
function obterParametroStatus() {
  // Obtem o objeto que representa a URL que esta no browser
  const url = new URL(window.location.href);

  // Converte o objeto URL para URLSearchParams para acessar os parametros da URL
  const parametros = new URLSearchParams(url.search);

  // Obtem o valor do parâmetro 'status'
  return Number(parametros.get("status"));
}

//realiza a abertura do modal de confirmação de exclusão do usuario
function abrirModalExcluir(id) {
  const botaoConfirmarExcluir = document.getElementById(
    "botaoConfirmarExcluir"
  );
  botaoConfirmarExcluir.setAttribute("data-id", id);

  const modalExcluir = document.getElementById("modalExcluir");
  modalExcluir.style.display = "block";
}

//realiza o fechamento do modal de confirmação de exclusão do usuario
function fecharModalExcluir() {
  const modalExcluir = document.getElementById("modalExcluir");
  modalExcluir.style.display = "none";
}

function abrirModalMensagem(titulo, mensagem) {
  // Configura o html do titulo do modal
  document.getElementById("mensagemTitulo").innerHTML = titulo;
  // Configura o html da mensagem do modal
  document.getElementById("mensagemStatus").innerHTML = mensagem;
  // Altera o atributo css do modal para ser exibido
  document.getElementById("modalMensagem").style.display = "block";
}

function fecharModalMensagem() {
  const modalExcluir = document.getElementById("modalMensagem");
  modalExcluir.style.display = "none";
}


//realiza a atualização do usuário, executando a API no backend
async function alterarPaciente(evento) {
  evento.preventDefault();

  // Obtém os valores dos campos do formulário
  const urlParams = new URLSearchParams(window.location.search);
  const id = urlParams.get('id');

  alert(id);
  const nome = document.getElementById("nome").value;
  const cpf = document.getElementById("cpf").value;
  const sexo = document.getElementById("sexo").value;
  const data_nascimento = document.getElementById("data_nascimento").value;

  try {
    // Realiza a chamada da API com os parâmetros
    const httpResponse = await fetch(`http://localhost:3000/paciente/atualizar/${id}?nome=${nome}&cpf=${cpf}&sexo=${sexo}&data_nascimento=${data_nascimento}`, {
      method: "PUT", // Método PUT
      headers: { "Content-type": "application/x-www-form-urlencoded" },
    });

    // Verifica se a resposta da API foi bem-sucedida
    if (!httpResponse.ok) {
      alert("Erro ao atualizar: " + httpResponse.status + ", erro: " + httpResponse.statusText);
      return;
    }

    // Exibe mensagem de sucesso e recarrega a lista de pacientes
    alert("Atualizado com sucesso");
    
    window.location.href = "../paciente.html";
  } catch (error) {
    alert("Erro ao atualizar: " + error.message);
  }
}