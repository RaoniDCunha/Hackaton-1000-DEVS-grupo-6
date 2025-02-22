package br.com.api.dao;

import br.com.api.dto.DTOImunizacao;
import br.com.api.model.Imunizacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOImunizacao {
    // Atributo utilizado para receber a conexão criada no método main
    public static Connection conexao = null;

    // Método para adicionar uma nova imunização
    public static int adicionar(Imunizacao imunizacao) throws SQLException {
        String sql = "INSERT INTO imunizacoes (id_paciente, id_dose, data_aplicacao, fabricante, lote, local_aplicacao, profissional_aplicador) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setInt(1, imunizacao.getIdPaciente());
            comando.setInt(2, imunizacao.getIdDose());
            comando.setDate(3, new Date(imunizacao.getDataAplicacao().getTime()));
            comando.setString(4, imunizacao.getFabricante());
            comando.setString(5, imunizacao.getLote());
            comando.setString(6, imunizacao.getLocalAplicacao());
            comando.setString(7, imunizacao.getProfissionalAplicador());

            // Executa a inserção
            comando.executeUpdate();

            // Obtém o ID gerado
            try (ResultSet idGerado = comando.getGeneratedKeys()) {
                if (idGerado.next()) {
                    return idGerado.getInt(1); // Retorna o ID gerado
                }
            }
        }

        // Caso o fluxo de execução chegue até este ponto, lança uma exceção
        throw new SQLException("Erro ao inserir imunização: nenhum ID gerado.");
    }

    // Método para listar todas as imunizações
    public static List<DTOImunizacao> listar() throws SQLException {
        List<DTOImunizacao> imunizacoes = new ArrayList<>();
        String sql = "SELECT i.id, p.nome AS nome_paciente, v.vacina, d.dose, i.data_aplicacao, i.fabricante, i.lote, i.local_aplicacao, i.profissional_aplicador " +
                "FROM imunizacoes i " +
                "JOIN paciente p ON i.id_paciente = p.id " +
                "JOIN dose d ON i.id_dose = d.id " +
                "JOIN vacina v ON d.id_vacina = v.id";

        try (Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DTOImunizacao dto = new DTOImunizacao(
                        rs.getInt("id"),
                        rs.getString("nome_paciente"), // Nome do paciente
                        rs.getString("vacina"),        // Nome da vacina
                        rs.getString("dose"),         // Nome da dose
                        rs.getDate("data_aplicacao"),
                        rs.getString("fabricante"),
                        rs.getString("lote"),
                        rs.getString("local_aplicacao"),
                        rs.getString("profissional_aplicador")
                );
                imunizacoes.add(dto);
            }
        }

        return imunizacoes;
    }

    // Método para consultar imunizações por ID
    public static List<DTOImunizacao> consultarPorId(int id) throws SQLException {
        List<DTOImunizacao> imunizacoes = new ArrayList<>();
        String sql = "SELECT i.id, p.nome AS nome_paciente, v.vacina, d.dose, i.data_aplicacao, i.fabricante, i.lote, i.local_aplicacao, i.profissional_aplicador " +
                "FROM imunizacoes i " +
                "JOIN paciente p ON i.id_paciente = p.id " +
                "JOIN dose d ON i.id_dose = d.id " +
                "JOIN vacina v ON d.id_vacina = v.id " +
                "WHERE i.id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    DTOImunizacao dto = new DTOImunizacao(
                            rs.getInt("id"),
                            rs.getString("nome_paciente"),
                            rs.getString("vacina"),
                            rs.getString("dose"),
                            rs.getDate("data_aplicacao"),
                            rs.getString("fabricante"),
                            rs.getString("lote"),
                            rs.getString("local_aplicacao"),
                            rs.getString("profissional_aplicador")
                    );
                    imunizacoes.add(dto);
                }
            }
        }

        return imunizacoes;
    }


    // Método para atualizar uma imunização
    public static int atualizar(Imunizacao imunizacao) throws SQLException {
        String sql = "UPDATE imunizacoes SET id_paciente = ?, id_dose = ?, data_aplicacao = ?, fabricante = ?, lote = ?, local_aplicacao = ?, profissional_aplicador = ? WHERE id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, imunizacao.getIdPaciente());
            comando.setInt(2, imunizacao.getIdDose());
            comando.setDate(3, new Date(imunizacao.getDataAplicacao().getTime()));
            comando.setString(4, imunizacao.getFabricante());
            comando.setString(5, imunizacao.getLote());
            comando.setString(6, imunizacao.getLocalAplicacao());
            comando.setString(7, imunizacao.getProfissionalAplicador());
            comando.setInt(8, imunizacao.getId());

            // Retorna a quantidade de linhas atualizadas
            return comando.executeUpdate();
        }
    }

    // Método para excluir uma imunização por ID
    public static int excluirPorId(int id) throws SQLException {
        String sql = "DELETE FROM imunizacoes WHERE id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            return comando.executeUpdate(); // Retorna a quantidade de linhas excluídas
        }
    }

    // Método para excluir todas as imunizações de um paciente
    public static int excluirPorPaciente(int idPaciente) throws SQLException {
        String sql = "DELETE FROM imunizacoes WHERE id_paciente = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idPaciente);
            return comando.executeUpdate(); // Retorna a quantidade de linhas excluídas
        }
    }

    // Método para consultar imunizações por ID do paciente
    public static List<DTOImunizacao> consultarPorPaciente(int idPaciente) throws SQLException {
        List<DTOImunizacao> imunizacoes = new ArrayList<>();
        String sql = "SELECT i.id, p.nome AS nome_paciente, v.vacina, d.dose, i.data_aplicacao, i.fabricante, i.lote, i.local_aplicacao, i.profissional_aplicador " +
                "FROM imunizacoes i " +
                "JOIN paciente p ON i.id_paciente = p.id " +
                "JOIN dose d ON i.id_dose = d.id " +
                "JOIN vacina v ON d.id_vacina = v.id " +
                "WHERE i.id_paciente = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idPaciente);

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    DTOImunizacao dto = new DTOImunizacao(
                            rs.getInt("id"),
                            rs.getString("nome_paciente"),
                            rs.getString("vacina"),
                            rs.getString("dose"),
                            rs.getDate("data_aplicacao"),
                            rs.getString("fabricante"),
                            rs.getString("lote"),
                            rs.getString("local_aplicacao"),
                            rs.getString("profissional_aplicador")
                    );
                    imunizacoes.add(dto);
                }
            }
        }

        return imunizacoes;
    }

    // Método para consultar imunizações por ID do paciente e intervalo de datas
    public static List<DTOImunizacao> consultarPorPacienteEData(int idPaciente, Date dataInicio, Date dataFim) throws SQLException {
        List<DTOImunizacao> imunizacoes = new ArrayList<>();
        String sql = "SELECT i.id, p.nome AS nome_paciente, v.vacina, d.dose, i.data_aplicacao, i.fabricante, i.lote, i.local_aplicacao, i.profissional_aplicador " +
                "FROM imunizacoes i " +
                "JOIN paciente p ON i.id_paciente = p.id " +
                "JOIN dose d ON i.id_dose = d.id " +
                "JOIN vacina v ON d.id_vacina = v.id " +
                "WHERE i.id_paciente = ? AND i.data_aplicacao BETWEEN ? AND ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idPaciente);
            comando.setDate(2, new Date(dataInicio.getTime()));
            comando.setDate(3, new Date(dataFim.getTime()));

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    DTOImunizacao dto = new DTOImunizacao(
                            rs.getInt("id"),
                            rs.getString("nome_paciente"),
                            rs.getString("vacina"),
                            rs.getString("dose"),
                            rs.getDate("data_aplicacao"),
                            rs.getString("fabricante"),
                            rs.getString("lote"),
                            rs.getString("local_aplicacao"),
                            rs.getString("profissional_aplicador")
                    );
                    imunizacoes.add(dto);
                }
            }
        }

        return imunizacoes;
    }
}