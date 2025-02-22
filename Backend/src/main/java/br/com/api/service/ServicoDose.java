package br.com.api.service;

import br.com.api.dao.DAODose;
import br.com.api.model.Dose;
import spark.Request;
import spark.Response;
import spark.Route;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

public class ServicoDose {
    public static Route consultarDosesPorVacina() {
        return (Request request, Response response) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                int idVacina = Integer.parseInt(request.params(":idVacina"));
                List<Dose> doses = DAODose.consultarPorVacina(idVacina);
                response.status(200); // OK
                return mapper.writeValueAsString(doses);
            } catch (SQLException e) {
                response.status(500); // Erro no servidor
                return "{\"message\": \"" + e.getMessage() + "\"}";
            }
        };
    }

    // Método que define uma rota para consultar doses recomendadas com base na idade (em meses).
    public static Route consultarDosesPorIdade() {
        return (Request request, Response response) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                int idadeMeses = Integer.parseInt(request.params(":idadeMeses"));
                List<Dose> doses = DAODose.consultarPorIdadeRecomendada(idadeMeses);

                if (doses.isEmpty()) {
                    response.status(404); // Recurso não encontrado
                    return "{\"message\": \"Nenhuma dose encontrada para a idade informada.\"}";
                }

                response.status(200); // OK
                return mapper.writeValueAsString(doses);
            } catch (SQLException e) {
                response.status(500); // Erro no servidor
                return "{\"message\": \"Erro interno no servidor: " + e.getMessage() + "\"}";
            } catch (NumberFormatException e) {
                response.status(400); // Requisição inválida
                return "{\"message\": \"Idade inválida. Informe um número válido.\"}";
            }
        };
    }

}