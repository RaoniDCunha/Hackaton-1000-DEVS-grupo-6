package br.com.api.dao;

import br.com.api.model.Dose;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAODose {
    public static Connection conexao;

    // Consultar doses por vacina
    public static List<Dose> consultarPorVacina(int idVacina) throws SQLException {
        List<Dose> doses = new ArrayList<>();
        String sql = "SELECT * FROM dose WHERE id_vacina = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idVacina);
            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {
                Dose dose = new Dose(
                        resultado.getInt("id"),
                        resultado.getInt("id_vacina"),
                        resultado.getString("dose"),
                        resultado.getInt("idade_recomendada_aplicacao")
                );
                doses.add(dose);
            }
        }
        return doses;
    }

    public static boolean doseExiste(int idDose) throws SQLException {
        String sql = "SELECT id FROM dose WHERE id = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idDose);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next(); // Retorna true se a dose existir
            }
        }
    }

    // Método que busca no banco de dados todas as doses recomendadas para uma determinada idade (em meses).
    public static List<Dose> consultarPorIdadeRecomendada(int idadeMeses) throws SQLException {
        List<Dose> doses = new ArrayList<>();
        String sql = "SELECT * FROM dose WHERE idade_recomendada_aplicacao = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idadeMeses);
            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {
                Dose dose = new Dose(
                        resultado.getInt("id"),
                        resultado.getInt("id_vacina"),
                        resultado.getString("dose"),
                        resultado.getInt("idade_recomendada_aplicacao")
                );
                doses.add(dose);
            }
        }
        return doses;
    }

}