import { apiBase, utils } from "./api.js";

const ENDPOINT = "vacinas";

export const vacinasModule = {
  async carregarVacinas() {
    try {
      const vacinas = await apiBase.listar(ENDPOINT+"/consultar");
      this.renderizarTabela(vacinas);
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },

  async cadastrarVacina(event) {
    event.preventDefault();
    try {
      const dados = utils.getFormData(event.target);
      await apiBase.cadastrar(ENDPOINT, dados);
      utils.mostrarMensagem("Sucesso", "Vacina cadastrada com sucesso");
      event.target.reset();
      this.carregarVacinas(); // Atualiza a tabela após o cadastro
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },

  async carregaVacina() {
    const id = utils.obterParametroUrl("id");

    if (!id) {
      console.error("ID não encontrado na URL");
      return;
    }

    console.log("Carregando vacina com ID:", id);

    try {
      const vacina = await apiBase.buscarPorId(ENDPOINT, id);
      this.preencherFormulario(vacina);
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },

  async atualizarVacina(event) {
    event.preventDefault();
    const id = utils.obterParametroUrl("id");

    if (!id) {
      console.error("ID da vacina não encontrado!");
      return;
    }

    console.log("Atualizando vacina com ID:", id);

    try {
      const dados = utils.getFormData(event.target);
      await apiBase.atualizar(ENDPOINT, id, dados);
      utils.mostrarMensagem("Sucesso", "Vacina atualizada com sucesso");
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },

  async excluirVacina(id) {
    if (!confirm("Deseja realmente excluir esta vacina?")) return;

    try {
      await apiBase.excluir(ENDPOINT, id);
      utils.mostrarMensagem("Sucesso", "Vacina excluída com sucesso");
      await this.carregarVacinas();
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },

  preencherFormulario(vacina) {
    Object.keys(vacina).forEach((key) => {
      const input = document.getElementById(key);
      if (input) input.value = vacina[key];
    });
  },

  renderizarTabela(vacinas) {
    const resultado = document.getElementById("dados");
    if (!resultado) return; // Evita erro se o ID "dados" não existir

    resultado.innerHTML = vacinas
      .map(
        (vacina) => `
          <tr>
              <td>${vacina.vacina}</td>
              <td>${vacina.descricao}</td>
              <td>${vacina.limite_aplicacao}</td>
              <td>${vacina.publico_alvo}</td>
          </tr>
      `
      )
      .join("");
  },
};

// Inicialização do script
document.addEventListener("DOMContentLoaded", () => {
  const tabela = document.getElementById("dados");
  const form = document.querySelector("form");

  if (tabela) {
    vacinasModule.carregarVacinas();
  }

  if (utils.obterParametroUrl("id")) {
    vacinasModule.carregaVacina();
  }

  if (form) {
    form.addEventListener("submit", (event) => {
      event.preventDefault();
      if (utils.obterParametroUrl("id")) {
        vacinasModule.atualizarVacina(event);
      } else {
        vacinasModule.cadastrarVacina(event);
      }
    });
  }
});

window.vacinasModule = vacinasModule;
