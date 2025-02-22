package br.com.api.dao;

import br.com.api.model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

import java.util.ArrayList;

public class DAOPaciente {
    //Atributo utilizado para receber a conexao criada no metodo main
    public static Connection conexao = null;

    //Realiza a insercao dos dados no banco de dados
    //Entrada: Tipo Paciente. Recebe o objeto Paciente
    //Retorno: Tipo int. Retorna o Id da chave primaria criado no banco de dados
    public static int inserir(Paciente paciente) throws SQLException{
        //Define a consulta sql
        String sql = "INSERT INTO paciente (nome, cpf, sexo, data_nascimento) VALUES (?, ?, ?, ?)";

        //Statement.RETURN_GENERATED_KEYS parametro que diz que o banco de dados deve retornar o id da chave primaria criada
        try(PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            //Adiciona os valores de nome, cfp e etc no lugar das ? da string sql
            comando.setString(1, paciente.getNome());
            comando.setString(2, paciente.getCpf());
            comando.setString(3, paciente.getSexo());

            // Converte LocalDate para java.sql.Date
            Date localDate = paciente.getData_nascimento();

            comando.setDate(4, localDate);

            //Envia o sql para o banco de dados
            comando.executeUpdate();

            //Obtém o resultado retornado do banco de dados
            //getGenerateKeys ira retornar o id da chave primaria que o banco de dados criou
            try (ResultSet idGerado = comando.getGeneratedKeys()) {

                //Verifica se o banco de dados retornou um id
                if (idGerado.next()) {
                    //retorna o id gerado no banco de dados
                    return idGerado.getInt(1);
                }
            }
        }

        //Caso o fluxo de execucao chegue ate este ponto é porque ocorreu algum erro
        //Gera uma excecao de negocio dizendo que nenhum id foi gerado
        throw new SQLException("Erro ao inserir paciente: nenhum ID gerado.");
    }

    //Realiza a consulta de todos os pacientes cadastrados na tabela
    //Entrada: Nenhum
    //Retorno: Tipo ArrayList<Paciente>. Retorna uma lista de objetos Pacientes
    public static ArrayList<Paciente> consultarTodosPacientes() throws SQLException {
        ArrayList<Paciente> lista = new ArrayList<Paciente>();

        // Certifique-se de incluir o campo "id" na consulta SQL
        String sql = "SELECT id, nome, cpf, sexo, data_nascimento FROM paciente";

        try (Statement comando = conexao.createStatement();
             ResultSet resultado = comando.executeQuery(sql)) {

            while (resultado.next()) {
                // Use o construtor que inclui o ID
                Paciente novoPaciente = new Paciente(
                        resultado.getInt("id"), // Adiciona o ID aqui
                        resultado.getString("nome"),
                        resultado.getString("cpf"),
                        resultado.getString("sexo"),
                        resultado.getDate("data_nascimento")
                );

                lista.add(novoPaciente);
            }
        }

        return lista;
    }

    //Realiza a consulta de um paciente pelo ID
    //Entrada: Tipo int. ID do paciente a ser pesquisado
    //Retorno: Tipo Paciente. Retorna o objeto Paciente
    public static Paciente consultarPorID(int id) throws SQLException{
        //Inicia o objeto pessoa como null
        Paciente pessoa = null;

        //Define o sql da consulta
        String sql = "SELECT * FROM paciente WHERE id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {//Cria o comando para receber sql dinamico
            //Substitui a ? pelo codigo do paciente
            comando.setInt(1, id);

            //Executa o comando SQL
            ResultSet resultado = comando.executeQuery();

            //Verifica se tem algum resultado retornado pelo banco
            if (resultado.next()) {
                //Cria o objeto pessoa com os dados retornados do banco
                pessoa = new Paciente(
                        resultado.getInt("id"),
                        resultado.getString("nome"),
                        resultado.getString("cpf"),
                        resultado.getString("sexo"),

                        //Aqui estamos convertendo o LocalDate para Date
                        resultado.getDate("data_nascimento")
                );
            }

            //Retorna o objeto pessoa
            return pessoa;
        }
    }

    //Exclui um paciente pelo ID
    //Entrada: Tipo int. ID do paciente a ser excluido
    //Retorno: Tipo int. Retorna a quatidade de linhas excluidas
    public static int excluirPorID(int id) throws SQLException{
        //Define o sql de exclusao
        String sql = "DELETE FROM paciente WHERE id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) { //Cria a conexao com o sql a ser preparado
            //Substitui a ? pelo id do paciente
            comando.setInt(1, id);

            //Executa a consulta e armazena o resultado da quantidade de linhas excluidas na variavel
            int qtdeLinhasExcluidas = comando.executeUpdate();

            return qtdeLinhasExcluidas;
        } catch (Exception e) {
            throw e;
        }
    }

    //Atualiza um paciente pelo ID
    //Entrada: Tipo Paciente. Ojbeto paciente a ser atualizado
    //Retorno: Tipo int. Retorna a quatidade de linhas excluidas
    public static int atualizarPaciente(Paciente paciente) throws SQLException{
        //Define o sql
        String sql = "UPDATE paciente SET nome = ?, cpf = ?, sexo = ?, data_nascimento = ? WHERE id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) { //cria a conexao para receber o sql dinamico
            //Substitui as ? pelos valores de nome, cpf, sexo, dataNascimento e id
            comando.setString(1, paciente.getNome());
            comando.setString(2, paciente.getCpf());
            comando.setString(3, paciente.getSexo());


            // Converte LocalDate para java.sql.Date
            Date localDate = paciente.getData_nascimento();
            comando.setDate(4, localDate);

            comando.setInt(5, paciente.getId());

            //Executa a atualizacao e armazena o retorno do banco com a quantidade de linhas atualizadas
            int qtdeLinhasAlteradas = comando.executeUpdate();

            //Retorna a quantidade de linhas atualizadas
            return qtdeLinhasAlteradas;
        }
    }

    public static boolean pacienteExiste(int idPaciente) throws SQLException {
        // Define a consulta SQL para verificar se o paciente existe
        String sql = "SELECT COUNT(*) AS total FROM paciente WHERE id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            // Define o parâmetro da consulta (ID do paciente)
            comando.setInt(1, idPaciente);

            // Executa a consulta
            try (ResultSet resultado = comando.executeQuery()) {
                // Verifica se há algum resultado
                if (resultado.next()) {
                    // Se o total for maior que 0, o paciente existe
                    return resultado.getInt("total") > 0;
                }
            }
        }

        // Se não houver resultados, o paciente não existe
        return false;
    }
}

