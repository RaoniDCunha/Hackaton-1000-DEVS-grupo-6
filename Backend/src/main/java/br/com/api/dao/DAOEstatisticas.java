package br.com.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOEstatisticas {
    public static Connection conexao = null;

    // Método para contar as vacinas aplicadas por paciente
    public static int contarVacinasAplicadasPorPaciente(int idPaciente) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM imunizacoes WHERE id_paciente = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idPaciente);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
            }
        }
        return 0;
    }

    // Método para contar as vacinas aplicáveis no próximo mês por paciente
    public static int contarVacinasAplicaveisProximoMes(int idPaciente) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM dose d " +
                "JOIN vacina v ON d.id_vacina = v.id " +
                "WHERE d.idade_recomendada_aplicacao BETWEEN ? AND ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            // Supondo que o próximo mês seja 30 dias a partir de hoje
            comando.setInt(1, 0); // Idade mínima
            comando.setInt(2, 30); // Idade máxima (próximo mês)
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
            }
        }
        return 0;
    }

    // Método para contar as vacinas atrasadas por paciente
    public static int contarVacinasAtrasadas(int idPaciente) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM dose d " +
                "JOIN vacina v ON d.id_vacina = v.id " +
                "WHERE d.idade_recomendada_aplicacao < ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            // Supondo que a idade do paciente seja calculada em meses
            int idadePaciente = calcularIdadePaciente(idPaciente);
            comando.setInt(1, idadePaciente);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
            }
        }
        return 0;
    }

    // Método para contar as vacinas acima de uma determinada idade
    public static int contarVacinasAcimaDaIdade(int meses) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM dose WHERE idade_recomendada_aplicacao > ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, meses);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
            }
        }
        return 0;
    }

    // Método para contar as vacinas não aplicáveis por paciente
    public static int contarVacinasNaoAplicaveis(int idPaciente) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM dose d " +
                "JOIN vacina v ON d.id_vacina = v.id " +
                "WHERE d.idade_recomendada_aplicacao > ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            int idadePaciente = calcularIdadePaciente(idPaciente);
            comando.setInt(1, idadePaciente);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("total");
                }
            }
        }
        return 0;
    }

    // Método auxiliar para calcular a idade do paciente em meses
    private static int calcularIdadePaciente(int idPaciente) throws SQLException {
        String sql = "SELECT TIMESTAMPDIFF(MONTH, data_nascimento, CURDATE()) AS idade FROM paciente WHERE id = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idPaciente);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("idade");
                }
            }
        }
        return 0;
    }
}