import { apiBase, utils } from "./api.js";

export const imunizacaoModule = {
  async carregarEstatisticas(pacienteId,pacienteAge) {
    try {
      console.log("Buscando estatísticas para o paciente ID:", pacienteId);

      
      
      // Buscar dados das estatísticas em paralelo
      const [vacinasAplicadas, vacinasProximoMes, vacinasAtrasadas, vacinasNaoAplicaveis] = await Promise.all([
        apiBase.listar(`estatisticas/imunizacoes/paciente/${pacienteId}`),
        apiBase.listar(`estatisticas/proximas_imunizacoes/paciente/${pacienteId}`),
        apiBase.listar(`estatisticas/imunizacoes_atrasadas/paciente/${pacienteId}`),
        apiBase.listar(`/estatisticas/vacinas/nao_aplicaveis/paciente/${pacienteId}`)
      ]);
      
      // Buscar estatística de vacinas acima de determinada idade (exemplo: 12 meses)
      const idadeReferencia = 3;
      const vacinasIdade = await apiBase.listar(`estatisticas/imunizacoes/idade_maior/${ pacienteAge == '' ?  idadeReferencia : pacienteAge}`);
      
      // Construir objeto com os dados
      const estatisticas = {
        vacinasAplicadas: vacinasAplicadas.total || 0,
        vacinasProximoMes: vacinasProximoMes.total || 0,
        vacinasAtrasadas: vacinasAtrasadas.total || 0,
        vacinasIdade: vacinasIdade.total || 0,
        vacinasNaoAplicaveis: vacinasNaoAplicaveis.total || 0,
      };

      console.log("Estatísticas calculadas:", estatisticas);
      this.atualizarCards(estatisticas);
    } catch (error) {
      console.error("Erro ao carregar estatísticas:", error);
      utils.mostrarMensagem("Erro", error.message);
    }
  },

  atualizarCards(estatisticas) {
    document.getElementById("vacinas-aplicadas").textContent = estatisticas.vacinasAplicadas;
    document.getElementById("vacinas-proximo-mes").textContent = estatisticas.vacinasProximoMes;
    document.getElementById("vacinas-atrasadas").textContent = estatisticas.vacinasAtrasadas;
    document.getElementById("vacinas-idade").textContent = estatisticas.vacinasIdade;
    document.getElementById("vacinas-nao-aplicaveis").textContent = estatisticas.vacinasNaoAplicaveis;
  },
};

// Inicialização do script
document.addEventListener("DOMContentLoaded", () => {
  const searchBtn = document.getElementById("search-id-btn");
  if (searchBtn) {
    searchBtn.addEventListener("click", () => {
      const pacienteId = document.getElementById("search-id").value;
      const pacienteAge = document.getElementById("age-id").value;

      if (pacienteId) {
        imunizacaoModule.carregarEstatisticas(pacienteId,pacienteAge);
      } else {
        utils.mostrarMensagem("Erro", "Por favor, insira um ID de paciente.");
      }
      
    });
  }
});

window.imunizacaoModule = imunizacaoModule;
