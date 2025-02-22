package br.com.api.dao;

import br.com.api.dto.DTODose;
import br.com.api.dto.DTOVacina;
import br.com.api.model.Vacina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOVacina {
    public static Connection conexao;

    // -- consultar todas as vacinas com suas doses
    public static List<DTOVacina> consultarTodasComDoses() throws SQLException {
        List<DTOVacina> vacinas = new ArrayList<>();
        Map<Integer, DTOVacina> vacinaMap = new HashMap<>(); // Para agrupar vacinas e doses

        String sql = "SELECT v.id AS vacina_id, v.vacina, v.descricao, v.limite_aplicacao, v.publico_alvo, " +
                "d.id AS dose_id, d.dose, d.idade_recomendada_aplicacao " +
                "FROM vacina v " +
                "LEFT JOIN dose d ON v.id = d.id_vacina";

        try (PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                int vacinaId = resultado.getInt("vacina_id");

                // Verifica se a vacina já foi adicionada ao mapa
                DTOVacina vacina = vacinaMap.get(vacinaId);
                if (vacina == null) {
                    // Cria uma nova vacina
                    vacina = new DTOVacina(
                            vacinaId,
                            resultado.getString("vacina"),
                            resultado.getString("descricao"),
                            resultado.getInt("limite_aplicacao"),
                            resultado.getString("publico_alvo"),
                            new ArrayList<>()
                    );
                    vacinaMap.put(vacinaId, vacina);
                    vacinas.add(vacina);
                }

                // Adiciona a dose à vacina
                if (resultado.getInt("dose_id") != 0) { // Verifica se há uma dose associada
                    DTODose dose = new DTODose(
                            resultado.getInt("dose_id"),
                            resultado.getString("dose"),
                            resultado.getInt("idade_recomendada_aplicacao")
                    );
                    vacina.getDoses().add(dose);
                }
            }
        }
        return vacinas;
    }

    // Consultar vacinas por faixa etária
    public static List<Vacina> consultarPorFaixaEtaria(String faixaEtaria) throws SQLException {
        List<Vacina> vacinas = new ArrayList<>();
        String sql = "SELECT * FROM vacina WHERE publico_alvo = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, faixaEtaria);
            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {
                Vacina vacina = new Vacina(
                        resultado.getInt("id"),
                        resultado.getString("vacina"),
                        resultado.getString("descricao"),
                        resultado.getInt("limite_aplicacao"),
                        resultado.getString("publico_alvo")
                );
                vacinas.add(vacina);
            }
        }
        return vacinas;
    }

    // Consultar vacinas recomendadas acima de uma idade
    public static List<Vacina> consultarPorIdadeMaior(int meses) throws SQLException {
        List<Vacina> vacinas = new ArrayList<>();
        String sql = "SELECT * FROM vacina WHERE limite_aplicacao IS NULL OR limite_aplicacao > ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, meses);
            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {
                Vacina vacina = new Vacina(
                        resultado.getInt("id"),
                        resultado.getString("vacina"),
                        resultado.getString("descricao"),
                        resultado.getInt("limite_aplicacao"),
                        resultado.getString("publico_alvo")
                );
                vacinas.add(vacina);
            }

            if (vacinas.isEmpty()) {
                throw new SQLException("Nenhuma vacina encontrada para idade maior que " + meses + " meses.");
            }
        }
        return vacinas;
    }

    // Consultar vacinas não aplicáveis para um paciente
    public static List<Vacina> consultarNaoAplicaveis(int idPaciente) throws SQLException {
        List<Vacina> vacinas = new ArrayList<>();
        String sql = "SELECT v.* FROM vacina v " +
                "WHERE v.limite_aplicacao IS NOT NULL AND v.limite_aplicacao < " +
                "(SELECT TIMESTAMPDIFF(MONTH, p.data_nascimento, CURDATE()) FROM paciente p WHERE p.id = ?)";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idPaciente);
            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {
                Vacina vacina = new Vacina(
                        resultado.getInt("id"),
                        resultado.getString("vacina"),
                        resultado.getString("descricao"),
                        resultado.getInt("limite_aplicacao"),
                        resultado.getString("publico_alvo")
                );
                vacinas.add(vacina);
            }

            if (vacinas.isEmpty()) {
                System.out.println("Nenhuma vacina não aplicável encontrada para o paciente com ID " + idPaciente);
            }
        }
        return vacinas;
    }


}