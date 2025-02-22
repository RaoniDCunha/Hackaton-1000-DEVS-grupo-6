package br.com.api.service;

import br.com.api.dao.DAOVacina;
import br.com.api.dto.DTOVacina;
import br.com.api.model.Vacina;
import spark.Request;
import spark.Response;
import spark.Route;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ServicoVacina {

    // Lista de faixas etárias permitidas
    private static final List<String> FAIXAS_ETARIAS_PERMITIDAS = Arrays.asList(
            "CRIANCA", "ADOLESCENTE", "ADULTO", "GESTANTE"
    );

    // Método para lidar com a rota de consultar todas as vacinas
    public static Route consultarTodasVacinas() {
        return (Request request, Response response) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<DTOVacina> vacinas = DAOVacina.consultarTodasComDoses();
                response.status(200); // OK
                return mapper.writeValueAsString(vacinas);
            } catch (SQLException e) {
                response.status(500); // Erro no servidor
                return "{\"message\": \"Erro ao consultar vacinas: " + e.getMessage() + "\"}";
            }
        };
    }

    // Método para lidar com a rota de consultar vacinas por faixa etária
    public static Route consultarPorFaixaEtaria() {
        return (Request request, Response response) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String faixaEtaria = request.params(":faixa").toUpperCase(); // Converte para maiúsculas

                // Valida se a faixa etária informada é permitida
                if (!FAIXAS_ETARIAS_PERMITIDAS.contains(faixaEtaria)) {
                    response.status(400); // Bad Request
                    return "{\"message\": \"Faixa etária inválida. As faixas permitidas são: CRIANCA, CRIANCA, ADOLESCENTE, ADULTO, GESTANTE.\"}";
                }

                // Consulta as vacinas para a faixa etária informada
                List<Vacina> vacinas = DAOVacina.consultarPorFaixaEtaria(faixaEtaria);
                if (vacinas.isEmpty()) {
                    response.status(404); // Not Found
                    return "{\"message\": \"Nenhuma vacina encontrada para a faixa etária informada.\"}";
                }

                response.status(200); // OK
                return mapper.writeValueAsString(vacinas);
            } catch (SQLException e) {
                response.status(500); // Erro no servidor
                return "{\"message\": \"Erro ao consultar vacinas: " + e.getMessage() + "\"}";
            }
        };
    }

    // Método para lidar com a rota de consultar vacinas recomendadas acima de uma idade
    public static Route consultarPorIdadeMaior() {
        return (Request request, Response response) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                int meses = Integer.parseInt(request.params(":meses"));
                List<Vacina> vacinas = DAOVacina.consultarPorIdadeMaior(meses);
                response.status(200); // OK
                return mapper.writeValueAsString(vacinas);
            } catch (SQLException e) {
                response.status(500); // Erro no servidor
                return "{\"message\": \"Erro ao consultar vacinas: " + e.getMessage() + "\"}";
            }
        };
    }

    // Método para lidar com a rota de consultar vacinas não aplicáveis para um paciente
    public static Route consultarNaoAplicaveis() {
        return (Request request, Response response) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                int idPaciente = Integer.parseInt(request.params(":id"));
                List<Vacina> vacinas = DAOVacina.consultarNaoAplicaveis(idPaciente);
                response.status(200); // OK
                return mapper.writeValueAsString(vacinas);
            } catch (SQLException e) {
                response.status(500); // Erro no servidor
                return "{\"message\": \"Erro ao consultar vacinas: " + e.getMessage() + "\"}";
            }
        };
    }
}