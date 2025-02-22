package br.com.api.service;

import br.com.api.dao.DAOEstatisticas;
import spark.Request;
import spark.Response;
import spark.Route;

public class ServicoEstatisticas {

    // Rota para contar as vacinas aplicadas por paciente
    public static Route contarVacinasAplicadasPorPaciente() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    int idPaciente = Integer.parseInt(request.params(":id"));
                    int total = DAOEstatisticas.contarVacinasAplicadasPorPaciente(idPaciente);
                    response.status(200); // 200 OK
                    return "{\"total\": " + total + "}";
                } catch (NumberFormatException e) {
                    response.status(400); // 400 Bad Request
                    return "{\"message\": \"ID do paciente está no formato incorreto.\"}";
                } catch (Exception e) {
                    response.status(500); // 500 Internal Server Error
                    return "{\"message\": \"Erro ao contar vacinas aplicadas: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Rota para contar as vacinas aplicáveis no próximo mês por paciente
    public static Route contarVacinasAplicaveisProximoMes() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    int idPaciente = Integer.parseInt(request.params(":id"));
                    int total = DAOEstatisticas.contarVacinasAplicaveisProximoMes(idPaciente);
                    response.status(200); // 200 OK
                    return "{\"total\": " + total + "}";
                } catch (NumberFormatException e) {
                    response.status(400); // 400 Bad Request
                    return "{\"message\": \"ID do paciente está no formato incorreto.\"}";
                } catch (Exception e) {
                    response.status(500); // 500 Internal Server Error
                    return "{\"message\": \"Erro ao contar vacinas aplicáveis no próximo mês: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Rota para contar as vacinas atrasadas por paciente
    public static Route contarVacinasAtrasadas() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    int idPaciente = Integer.parseInt(request.params(":id"));
                    int total = DAOEstatisticas.contarVacinasAtrasadas(idPaciente);
                    response.status(200); // 200 OK
                    return "{\"total\": " + total + "}";
                } catch (NumberFormatException e) {
                    response.status(400); // 400 Bad Request
                    return "{\"message\": \"ID do paciente está no formato incorreto.\"}";
                } catch (Exception e) {
                    response.status(500); // 500 Internal Server Error
                    return "{\"message\": \"Erro ao contar vacinas atrasadas: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Rota para contar as vacinas acima de uma determinada idade
    public static Route contarVacinasAcimaDaIdade() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    int meses = Integer.parseInt(request.params(":meses"));
                    int total = DAOEstatisticas.contarVacinasAcimaDaIdade(meses);
                    response.status(200); // 200 OK
                    return "{\"total\": " + total + "}";
                } catch (NumberFormatException e) {
                    response.status(400); // 400 Bad Request
                    return "{\"message\": \"Idade em meses está no formato incorreto.\"}";
                } catch (Exception e) {
                    response.status(500); // 500 Internal Server Error
                    return "{\"message\": \"Erro ao contar vacinas acima da idade: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Rota para contar as vacinas não aplicáveis por paciente
    public static Route contarVacinasNaoAplicaveis() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    int idPaciente = Integer.parseInt(request.params(":id"));
                    int total = DAOEstatisticas.contarVacinasNaoAplicaveis(idPaciente);
                    response.status(200); // 200 OK
                    return "{\"total\": " + total + "}";
                } catch (NumberFormatException e) {
                    response.status(400); // 400 Bad Request
                    return "{\"message\": \"ID do paciente está no formato incorreto.\"}";
                } catch (Exception e) {
                    response.status(500); // 500 Internal Server Error
                    return "{\"message\": \"Erro ao contar vacinas não aplicáveis: " + e.getMessage() + "\"}";
                }
            }
        };
    }
}