//realiza o processamento do conteudo no frontend assim que a pagina for carregada
window.onload = processarCarregamentoPagina;

document.addEventListener("DOMContentLoaded", function () {
  processarCarregamentoPagina();
});

function processarCarregamentoPagina() {
  // Aqui você pode carregar dados adicionais, como listas de pacientes ou doses, se necessário
}

//realiza o cadastro da imunização, executando a API no backend
async function realizarCadastroImunizacao(evento) {
  evento.preventDefault();

  // Obtém os valores dos campos do formulário
  const idPaciente = document.getElementById("idPaciente").value;
  const idDose = document.getElementById("idDose").value;
  const dataAplicacao = document.getElementById("dataAplicacao").value;
  const fabricante = document.getElementById("fabricante").value;
  const lote = document.getElementById("lote").value;
  const localAplicacao = document.getElementById("localAplicacao").value;
  const profissionalAplicador = document.getElementById("profissionalAplicador").value;

  // Constrói o objeto de imunização
  const imunizacao = {
    idPaciente: parseInt(idPaciente),
    idDose: parseInt(idDose),
    dataAplicacao: dataAplicacao,
    fabricante: fabricante,
    lote: lote,
    localAplicacao: localAplicacao,
    profissionalAplicador: profissionalAplicador,
  };

  try {
    // Realiza a chamada da API
    const httpResponse = await fetch("http://localhost:3000/imunizacao/inserir", {
      method: "POST",
      headers: { "Content-type": "application/json" },
      body: JSON.stringify(imunizacao),
    });

    // Verifica se a resposta da API foi bem-sucedida
    if (!httpResponse.ok) {
      alert("Erro ao cadastrar: " + httpResponse.status + ", erro: " + httpResponse.statusText);
      return;
    }

    // Exibe mensagem de sucesso
    alert("Imunização cadastrada com sucesso!");
    window.location.href = "../../imunizacoes.html"; 
  } catch (error) {
    alert("Erro ao cadastrar: " + error.message);
  }
}

// imunizacao.js
import { apiBase, utils } from "./api.js";

const ENDPOINT = "imunizacoes/paciente/"; // Endpoint para consultar imunizações por paciente

export const imunizacaoModule = {
  async carregarImunizacoes(pacienteId) {
    try {
      console.log("Buscando imunizações para o paciente ID:", pacienteId);

      // Filtra as imunizações pelo ID do paciente
      const imunizacoes = await apiBase.listar(`${ENDPOINT}${pacienteId}`);
      console.log("Dados retornados:", imunizacoes);

      this.atualizarTabela(imunizacoes); // Atualiza a tabela com os dados das imunizações
    } catch (error) {
      console.error("Erro ao carregar imunizações:", error);
      utils.mostrarMensagem("Erro", error.message);
    }
  },

  atualizarTabela(imunizacoes) {
    const tabela = document.getElementById("tabela-imunizacoes");
    if (tabela) {
      tabela.innerHTML = imunizacoes
        .map(
          (imunizacao) => `
          <tr>
            <td>${imunizacao.id}</td>
            <td>${imunizacao.nomePaciente}</td>
            <td>${imunizacao.nomeVacina}</td>
            <td>${imunizacao.nomeDose}</td>
            <td>${imunizacao.dataAplicacao}</td>
            <td>${imunizacao.fabricante}</td>
            <td>${imunizacao.lote}</td>
            <td>${imunizacao.localAplicacao}</td>
            <td>${imunizacao.profissionalAplicador}</td>
             <td>
                <a href="../../cadastro/editar/imunizacao.html?id=${imunizacao.id}">
                  <button class="main_btn">Editar</button>
                </a>
                <button class="main_btn_light" onclick="abrirModalExcluir('${imunizacao.id}')">Excluir</button>
              </td>
          </tr>
        `
        )
        .join("");
    } else {
      console.error("Tabela de imunizações não encontrada.");
    }
  },
};


//realiza a exclusão da imunização no backend
async function excluirImunizacao(botao) {
  let mensagem;

  try {
    //obtem o id da imunização a ser excluída
    const id = botao.getAttribute("data-id");

    //realiza a chamada da API
    const response = await fetch(`http://localhost:3000/imunizacao/excluir/${id}`, {
      method: "DELETE",
    });

    //determina o codigo de status baseado no http response
    const codigoStatus = determinarCodigoStatus(response);

    //determina a mensagem a ser exibida para o usuario
    mensagem = retornarMensagem(codigoStatus);
  } catch (error) {
    mensagem = "Erro ao excluir imunização: " + error.message;
  }

  //fecha o modal excluir
  fecharModalExcluir();
  //abre o modal de status para informar o resultado da exclusao
  abrirModalMensagem("Exclusão Imunização", mensagem);

  // Recarrega a lista de imunizações
  carregarImunizacoes();
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

  return mensagem;
}

//realiza a abertura do modal de confirmação de exclusão
function abrirModalExcluir(id) {
  const botaoConfirmarExcluir = document.getElementById("botaoConfirmarExcluir");
  botaoConfirmarExcluir.setAttribute("data-id", id);

  const modalExcluir = document.getElementById("modalExcluir");
  modalExcluir.style.display = "block";
}

//realiza o fechamento do modal de confirmação de exclusão
function fecharModalExcluir() {
  const modalExcluir = document.getElementById("modalExcluir");
  modalExcluir.style.display = "none";
}

//realiza a abertura do modal de mensagem
function abrirModalMensagem(titulo, mensagem) {
  document.getElementById("mensagemTitulo").innerHTML = titulo;
  document.getElementById("mensagemStatus").innerHTML = mensagem;
  document.getElementById("modalMensagem").style.display = "block";
}

//realiza o fechamento do modal de mensagem
function fecharModalMensagem() {
  const modalMensagem = document.getElementById("modalMensagem");
  modalMensagem.style.display = "none";
}

// Inicialização do script
document.addEventListener("DOMContentLoaded", () => {
  const searchBtn = document.getElementById("search-id-btn");

  if (searchBtn) {
    searchBtn.addEventListener("click", () => {
      console.log("Botão de busca clicado!");
      const pacienteId = document.getElementById("search-id").value;
      if (pacienteId) {
        imunizacaoModule.carregarImunizacoes(pacienteId);
      } else {
        utils.mostrarMensagem("Erro", "Por favor, insira um ID de paciente.");
      }
    });
  }
});
// Função para carregar os dados da imunização a ser editada
async function carregarDadosImunizacao() {
  const urlParams = new URLSearchParams(window.location.search);
  const id = urlParams.get('id');

  if (!id) {
    alert("ID da imunização não encontrado na URL.");
    return;
  }

  try {
    // Faz a chamada na API para buscar os dados da imunização
    const response = await fetch(`http://localhost:3000/imunizacao/consultar/${id}`);

    if (!response.ok) {
      const codigoStatus = determinarCodigoStatus(response);
      const mensagem = retornarMensagem(codigoStatus);
      abrirModalMensagem("Editar Imunização", mensagem);
      return;
    }

    // Converte os dados de JSON para objeto JavaScript
    const imunizacao = await response.json();

    // Preenche os campos do formulário com os dados da imunização
    document.getElementById("idPaciente").value = imunizacao.idPaciente;
    document.getElementById("idDose").value = imunizacao.idDose;
    document.getElementById("dataAplicacao").value = imunizacao.dataAplicacao;
    document.getElementById("fabricante").value = imunizacao.fabricante;
    document.getElementById("lote").value = imunizacao.lote;
    document.getElementById("localAplicacao").value = imunizacao.localAplicacao;
    document.getElementById("profissionalAplicador").value = imunizacao.profissionalAplicador;
  } catch (error) {
    abrirModalMensagem("Editar Imunização", "Erro: " + error.message);
  }
}

// Função para alterar a imunização
async function alterarImunizacao(event) {
  
  event.preventDefault();

  // Obtém os valores dos campos do formulário
  const urlParams = new URLSearchParams(window.location.search);
  const id = urlParams.get('id');

  const idPaciente = document.getElementById("idPaciente").value;
  const idDose = document.getElementById("idDose").value;
  const dataAplicacao = document.getElementById("dataAplicacao").value;
  const fabricante = document.getElementById("fabricante").value;
  const lote = document.getElementById("lote").value;
  const localAplicacao = document.getElementById("localAplicacao").value;
  const profissionalAplicador = document.getElementById("profissionalAplicador").value;
  

  // Constrói o objeto de imunização
  const imunizacao = {
    idPaciente: parseInt(idPaciente),
    idDose: parseInt(idDose),
    dataAplicacao: dataAplicacao,
    fabricante: fabricante,
    lote: lote,
    localAplicacao: localAplicacao,
    profissionalAplicador: profissionalAplicador,
  };

  try {
    // Realiza a chamada da API para atualizar a imunização
    const httpResponse = await fetch(`http://localhost:3000/imunizacao/alterar/${id}`, {
      method: "PUT",
      headers: { "Content-type": "application/json" },
      body: JSON.stringify(imunizacao),
    });

    // Verifica se a resposta da API foi bem-sucedida
    if (!httpResponse.ok) {
      alert("Erro ao atualizar: " + httpResponse.status + ", erro: " + httpResponse.statusText);
      return;
    }

    // Exibe mensagem de sucesso e redireciona para a página de imunizações
    alert("Imunização atualizada com sucesso!");
  } catch (error) {
    alert("Erro ao atualizar: " + error.message);
  }
}






window.imunizacaoModule = imunizacaoModule;

