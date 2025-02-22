package br.com.api.service;

import br.com.api.dao.DAODose;
import br.com.api.dao.DAOImunizacao;
import br.com.api.dao.DAOPaciente;
import br.com.api.dto.DTOImunizacao;
import br.com.api.model.Imunizacao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Date;
import java.util.List;

public class ServicoImunizacao {

    // Método para lidar com a rota de adicionar imunização
    public static Route cadastrarImunizacao() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // Verifica se o corpo da requisição está vazio
                    if (request.body() == null || request.body().isEmpty()) {
                        response.status(400); // 400 Bad Request
                        return "{\"message\": \"O corpo da requisição não pode estar vazio.\"}";
                    }

                    // Lê o corpo da requisição como um JsonNode
                    JsonNode jsonNode = mapper.readTree(request.body());

                    // Extrai cada campo do JSON manualmente
                    int idPaciente = jsonNode.get("idPaciente").asInt();
                    int idDose = jsonNode.get("idDose").asInt();
                    Date dataAplicacao = Date.valueOf(jsonNode.get("dataAplicacao").asText());
                    String fabricante = jsonNode.get("fabricante").asText();
                    String lote = jsonNode.get("lote").asText();
                    String localAplicacao = jsonNode.get("localAplicacao").asText();
                    String profissionalAplicador = jsonNode.get("profissionalAplicador").asText();

                    // Verifica se o paciente existe no banco de dados
                    if (!DAOPaciente.pacienteExiste(idPaciente)) {
                        response.status(404); // 404 Not Found
                        return "{\"message\": \"Paciente não encontrado.\"}";
                    }

                    // Verifica se a dose existe no banco de dados
                    if (!DAODose.doseExiste(idDose)) {
                        response.status(404); // 404 Not Found
                        return "{\"message\": \"Dose não encontrada.\"}";
                    }

                    // Cria o objeto Imunizacao usando o construtor
                    Imunizacao imunizacao = new Imunizacao(
                            idPaciente,
                            idDose,
                            dataAplicacao,
                            fabricante,
                            lote,
                            localAplicacao,
                            profissionalAplicador
                    );

                    // Adiciona a imunização no banco de dados e obtém o ID gerado
                    int idImunizacao = DAOImunizacao.adicionar(imunizacao);

                    // Define o status HTTP 201 (Created)
                    response.status(201);

                    // Retorna uma mensagem de sucesso com o ID gerado
                    return "{\"message\": \"Imunização cadastrada com sucesso. ID: " + idImunizacao + "\"}";
                } catch (IllegalArgumentException e) {
                    // Em caso de erro na conversão da data
                    response.status(400); // 400 Bad Request
                    return "{\"message\": \"Formato de data inválido. Use o formato yyyy-MM-dd.\"}";
                } catch (JsonProcessingException e) {
                    // Em caso de erro ao processar o JSON
                    response.status(400); // 400 Bad Request
                    return "{\"message\": \"Formato de JSON inválido.\"}";
                } catch (Exception e) {
                    // Em caso de erro genérico
                    response.status(500); // 500 Internal Server Error
                    return "{\"message\": \"Erro ao cadastrar imunização: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Método para lidar com a rota de consultar imunização por ID
    public static Route consultarImunizacaoPorId() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // Extrai o ID da URL
                    int id = Integer.parseInt(request.params(":id"));

                    // Busca a lista de imunizações pelo ID
                    List<DTOImunizacao> imunizacoes = DAOImunizacao.consultarPorId(id);

                    if (!imunizacoes.isEmpty()) {
                        // Define o status HTTP 200 (OK)
                        response.status(200);

                        // Retorna a lista de imunizações em formato JSON
                        return mapper.writeValueAsString(imunizacoes);
                    } else {
                        // Define o status HTTP 404 (Not Found)
                        response.status(404);
                        return "{\"message\": \"Nenhuma imunização encontrada para o ID fornecido.\"}";
                    }
                } catch (NumberFormatException e) {
                    // Em caso de erro de conversão do ID, define o status HTTP 400 (Bad Request)
                    response.status(400);
                    return "{\"message\": \"ID fornecido está no formato incorreto.\"}";
                } catch (Exception e) {
                    // Em caso de erro, define o status HTTP 500 (Internal Server Error)
                    response.status(500);
                    return "{\"message\": \"Erro ao consultar imunização: " + e.getMessage() + "\"}";
                }
            }
        };
    }


    // Método para lidar com a rota de consultar todas as imunizações
    public static Route consultarTodasImunizacoes() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // Busca todas as imunizações no banco de dados
                    List<DTOImunizacao> imunizacoes = DAOImunizacao.listar();

                    if (imunizacoes.isEmpty()) {
                        // Define o status HTTP 200 (OK) com mensagem de lista vazia
                        response.status(200);
                        return "{\"message\": \"Nenhuma imunização encontrada.\"}";
                    } else {
                        // Define o status HTTP 200 (OK) e retorna a lista de imunizações em formato JSON
                        response.status(200);
                        return mapper.writeValueAsString(imunizacoes);
                    }
                } catch (Exception e) {
                    // Em caso de erro, define o status HTTP 500 (Internal Server Error)
                    response.status(500);
                    return "{\"message\": \"Erro ao consultar imunizações: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Método para lidar com a rota de atualizar imunização
    public static Route alterarImunizacao() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // Verifica se o corpo da requisição está vazio
                    if (request.body() == null || request.body().isEmpty()) {
                        response.status(400); // 400 Bad Request
                        return "{\"message\": \"O corpo da requisição não pode estar vazio.\"}";
                    }

                    // Extrai o ID da URL
                    int id = Integer.parseInt(request.params(":id"));

                    // Lê o corpo da requisição como um JsonNode
                    JsonNode jsonNode = mapper.readTree(request.body());

                    // Extrai cada campo do JSON manualmente
                    int idPaciente = jsonNode.get("idPaciente").asInt();
                    int idDose = jsonNode.get("idDose").asInt();
                    Date dataAplicacao = Date.valueOf(jsonNode.get("dataAplicacao").asText());
                    String fabricante = jsonNode.get("fabricante").asText();
                    String lote = jsonNode.get("lote").asText();
                    String localAplicacao = jsonNode.get("localAplicacao").asText();
                    String profissionalAplicador = jsonNode.get("profissionalAplicador").asText();

                    // Verifica se o paciente existe no banco de dados
                    if (!DAOPaciente.pacienteExiste(idPaciente)) {
                        response.status(404); // 404 Not Found
                        return "{\"message\": \"Paciente não encontrado.\"}";
                    }

                    // Verifica se a dose existe no banco de dados
                    if (!DAODose.doseExiste(idDose)) {
                        response.status(404); // 404 Not Found
                        return "{\"message\": \"Dose não encontrada.\"}";
                    }

                    // Cria o objeto Imunizacao usando o construtor
                    Imunizacao imunizacao = new Imunizacao(
                            idPaciente,
                            idDose,
                            dataAplicacao,
                            fabricante,
                            lote,
                            localAplicacao,
                            profissionalAplicador
                    );
                    imunizacao.setId(id); // Define o ID da imunização

                    // Atualiza a imunização no banco de dados
                    int linhasAfetadas = DAOImunizacao.atualizar(imunizacao);

                    if (linhasAfetadas > 0) {
                        // Define o status HTTP 200 (OK)
                        response.status(200);
                        return "{\"message\": \"Imunização atualizada com sucesso.\"}";
                    } else {
                        // Define o status HTTP 404 (Not Found)
                        response.status(404);
                        return "{\"message\": \"Imunização não encontrada.\"}";
                    }
                } catch (NumberFormatException e) {
                    // Em caso de erro de conversão do ID, define o status HTTP 400 (Bad Request)
                    response.status(400);
                    return "{\"message\": \"ID fornecido está no formato incorreto.\"}";
                } catch (IllegalArgumentException e) {
                    // Em caso de erro na conversão da data
                    response.status(400); // 400 Bad Request
                    return "{\"message\": \"Formato de data inválido. Use o formato yyyy-MM-dd.\"}";
                } catch (JsonProcessingException e) {
                    // Em caso de erro ao processar o JSON
                    response.status(400); // 400 Bad Request
                    return "{\"message\": \"Formato de JSON inválido.\"}";
                } catch (Exception e) {
                    // Em caso de erro genérico
                    response.status(500); // 500 Internal Server Error
                    return "{\"message\": \"Erro ao atualizar imunização: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Método para lidar com a rota de excluir imunização
    public static Route excluirImunizacao() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    // Extrai o ID da URL
                    int id = Integer.parseInt(request.params(":id"));

                    // Exclui a imunização no banco de dados
                    int linhasAfetadas = DAOImunizacao.excluirPorId(id);

                    if (linhasAfetadas > 0) {
                        // Define o status HTTP 200 (OK)
                        response.status(200);
                        return "{\"message\": \"Imunização excluída com sucesso.\"}";
                    } else {
                        // Define o status HTTP 404 (Not Found)
                        response.status(404);
                        return "{\"message\": \"Imunização não encontrada.\"}";
                    }
                } catch (NumberFormatException e) {
                    // Em caso de erro de conversão do ID, define o status HTTP 400 (Bad Request)
                    response.status(400);
                    return "{\"message\": \"ID fornecido está no formato incorreto.\"}";
                } catch (Exception e) {
                    // Em caso de erro, define o status HTTP 500 (Internal Server Error)
                    response.status(500);
                    return "{\"message\": \"Erro ao excluir imunização: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Método para lidar com a rota de excluir todas as imunizações de um paciente
    public static Route excluirImunizacoesPorPaciente() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    // Extrai o ID do paciente da URL
                    int idPaciente = Integer.parseInt(request.params(":id"));

                    // Exclui todas as imunizações do paciente no banco de dados
                    int linhasAfetadas = DAOImunizacao.excluirPorPaciente(idPaciente);

                    if (linhasAfetadas > 0) {
                        // Define o status HTTP 200 (OK)
                        response.status(200);
                        return "{\"message\": \"Todas as imunizações do paciente foram excluídas com sucesso.\"}";
                    } else {
                        // Define o status HTTP 404 (Not Found)
                        response.status(404);
                        return "{\"message\": \"Nenhuma imunização encontrada para o paciente.\"}";
                    }
                } catch (NumberFormatException e) {
                    // Em caso de erro de conversão do ID, define o status HTTP 400 (Bad Request)
                    response.status(400);
                    return "{\"message\": \"ID do paciente está no formato incorreto.\"}";
                } catch (Exception e) {
                    // Em caso de erro, define o status HTTP 500 (Internal Server Error)
                    response.status(500);
                    return "{\"message\": \"Erro ao excluir imunizações do paciente: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Método para lidar com a rota de consultar imunizações por paciente e intervalo de datas
    public static Route consultarImunizacoesPorPacienteEData() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // Extrai o ID do paciente e as datas da URL
                    int idPaciente = Integer.parseInt(request.params(":id"));
                    Date dataInicio = Date.valueOf(request.params(":dt_ini"));
                    Date dataFim = Date.valueOf(request.params(":dt_fim"));

                    // Busca as imunizações no banco de dados
                    List<DTOImunizacao> imunizacoes = DAOImunizacao.consultarPorPacienteEData(idPaciente, dataInicio, dataFim);

                    if (imunizacoes.isEmpty()) {
                        // Define o status HTTP 200 (OK) com mensagem de lista vazia
                        response.status(200);
                        return "{\"message\": \"Nenhuma imunização encontrada para o paciente no período especificado.\"}";
                    } else {
                        // Define o status HTTP 200 (OK) e retorna a lista de imunizações em formato JSON
                        response.status(200);
                        return mapper.writeValueAsString(imunizacoes);
                    }
                } catch (NumberFormatException e) {
                    // Em caso de erro de conversão do ID ou datas, define o status HTTP 400 (Bad Request)
                    response.status(400);
                    return "{\"message\": \"ID do paciente ou datas fornecidas estão no formato incorreto.\"}";
                } catch (Exception e) {
                    // Em caso de erro, define o status HTTP 500 (Internal Server Error)
                    response.status(500);
                    return "{\"message\": \"Erro ao consultar imunizações: " + e.getMessage() + "\"}";
                }
            }
        };
    }

    // Método para lidar com a rota de consultar imunizações por paciente
    public static Route consultarImunizacoesPorPaciente() {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // Extrai o ID do paciente da URL
                    int idPaciente = Integer.parseInt(request.params(":id"));

                    // Busca as imunizações no banco de dados
                    List<DTOImunizacao> imunizacoes = DAOImunizacao.consultarPorPaciente(idPaciente);

                    if (imunizacoes.isEmpty()) {
                        // Define o status HTTP 200 (OK) com mensagem de lista vazia
                        response.status(200);
                        return "{\"message\": \"Nenhuma imunização encontrada para o paciente.\"}";
                    } else {
                        // Define o status HTTP 200 (OK) e retorna a lista de imunizações em formato JSON
                        response.status(200);
                        return mapper.writeValueAsString(imunizacoes);
                    }
                } catch (NumberFormatException e) {
                    // Em caso de erro de conversão do ID, define o status HTTP 400 (Bad Request)
                    response.status(400);
                    return "{\"message\": \"ID do paciente está no formato incorreto.\"}";
                } catch (Exception e) {
                    // Em caso de erro, define o status HTTP 500 (Internal Server Error)
                    response.status(500);
                    return "{\"message\": \"Erro ao consultar imunizações: " + e.getMessage() + "\"}";
                }
            }
        };
    }
}

