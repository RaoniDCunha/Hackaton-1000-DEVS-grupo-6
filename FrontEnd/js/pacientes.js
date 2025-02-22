import { apiBase, utils } from "./api.js";

const ENDPOINT = "paciente";

export const pacientesModule = {
  async carregarPacientes() {
    try {
      const pacientes = await apiBase.listar(ENDPOINT);
      this.renderizarTabela(pacientes);
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },
  async cadastrarPaciente(event) {
    event.preventDefault();
    try {
      const dados = utils.getFormData(event.target);
      await apiBase.cadastrar(ENDPOINT, dados);
      utils.mostrarMensagem("Sucesso", "Paciente Cadastrado com sucesso");
      event.target.reset();
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },
  async carregaPaciente() {
    const id = utils.obterParametroUrl("id");
    try {
      const paciente = await apiBase.buscarPorId(ENDPOINT, id);
      this.preencherFormulario(paciente);
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },
  async atualizarPaciente(event) {
    event.preventDefault();
    const id = utils.obterParametroUrl("id");
    try {
      const dados = utils.getFormData(event.target);
      await apiBase.atualizar(ENDPOINT, id, dados);
      utils.mostrarMensagem("sucesso", "Paciente atualizado com sucesso");
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },
  async excluirPaciente(id) {
    if (!confirm("Deseja realmente excluir o paciente ?")) return;

    try {
      await apiBase.excluir(ENDPOINT, id);
      utils.mostrarMensagem("sucesso", "Paciente excluido com sucesso");
      await this.carregarPacientes();
    } catch (error) {
      utils.mostrarMensagem("Erro", error.message);
    }
  },
  preencherFormulario(paciente) {
    Object.keys(paciente).forEach((key) => {
      const input = document.getElementById(key);
      if (input) input.value = paciente[key];
    });
  },
  renderizarTabela(pacientes) {
    const resultado = document.getElementById("dados");
    resultado.innerHTML = pacientes.map(
      (paciente) => `
            <tr>
                <td>${paciente.nome}</td>
                <td>
                    <a href="/frontend/cadastro/editar/paciente.html?id=${paciente.id}">
                        <button class="w3-button w3-green w3-round">Editar</button>
                    </a>
                </td>
                <td>
                    <button class="w3-button w3-red w3-round" 
                        id="btn_excluir"
                            onclick=pacientesModule.excluirPaciente('${paciente.id}')>
                        Excluir
                    </button>
                </td>
            </tr>
        `
    );
  },
};

//incialização dos scripts

document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("dados")) {
    pacientesModule.carregarPacientes();
  }

  if (utils.obterParametroUrl("id")) {
    pacientesModule.carregarPacientes();
  }

  //Configuração do formulario
  const form = document.querySelector("form");
  if (form) {
    form.addEventListener("submit", (event) => {
      if (utils.obterParametroUrl("id")) {
        pacientesModule.atualizarPaciente(event);
      } else {
        pacientesModule.cadastrarPaciente(event);
      }
    });
  }
});

window.pacientesModule = pacientesModule;
