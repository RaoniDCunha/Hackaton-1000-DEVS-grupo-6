package br.com.api.dao;

import br.com.api.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

public class DAOUsuario {
    //atributo utilizado para receber a conexao criada no metodo main
    public static Connection conexao = null;
 
    //realiza a insercao dos dados no banco de dados
    //Entrada: Tipo Usuario. Recebe o objeto Usuario 
    //Retorno: Tipo int. Retorna o Id da chave primaria criado no banco de dados
    public static int inserir(Usuario usuario) throws SQLException{
        //Define a consulta sql 
        String sql = "INSERT INTO usuario (nome, email) VALUES (?, ?)";

        //Statement.RETURN_GENERATED_KEYS parametro que diz que o banco de dados deve retornar o id da chave primaria criada
        try(PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //adiciona os valores de nome e mail no lugar das ? da string sql
            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getEmail());

            //envia o sql para o banco de dados
            comando.executeUpdate();

            //obtem o resultado retornado do banco de dados
            //getGenerateKeys ira retornar o id da chave primaria que o banco de dados criou
            try (ResultSet idGerado = comando.getGeneratedKeys()) {
                //verifica se o banco de dados retornou um id
                if (idGerado.next()) { 
                    //retorna o id gerado no banco de dados
                    return idGerado.getInt(1);
                }
            }
        }
        
        //Caso o fluxo de execucao chegue ate este ponto é porque ocorreu algum erro
        //Gera uma excecao de negocio dizendo que nenhum id foi gerado
        throw new SQLException("Erro ao inserir usuário: nenhum ID gerado.");
    }

    //realiza a consulta de todos os usuarios cadastrados na tabela
    //Entrada: Nenhum
    //Retorno: Tipo ArrayList<Usuario>. Retorna uma lista de objetos Usuarios
    public static ArrayList<Usuario> consultarTodosUsuarios() throws SQLException{
        //cria o array list pra receber os dados dos usuarios que retornarao do banco de dados
        ArrayList<Usuario> lista = new ArrayList<Usuario>();

        //define o sql de consulta
        String sql = "SELECT * FROM usuario";

        try (   Statement comando = conexao.createStatement(); //cria o comando
                ResultSet resultado = comando.executeQuery(sql); //executa a consulta
            ) {

            //para cada registro retornado do banco de dados
            while(resultado.next()){
                
                //cria um novo objeto usuario
                Usuario novoUsuario = new Usuario(
                    resultado.getInt("id"),
                    resultado.getString("nome"), 
                    resultado.getString("email")
                );

                //adiciona o objeto usuario no array list
                lista.add(novoUsuario);
            }
        } 
        
        //retorna o array list de objetos usuarios
        return lista;
    }

    //realiza a consulta de um usuario pelo ID
    //Entrada: Tipo int. ID do usuario a ser pesquisado
    //Retorno: Tipo Usuario. Retorna o objeto Usuario
    public static Usuario consultarPorID(int id) throws SQLException{
        //inicia o objeto pessoa como null
        Usuario pessoa = null;

        //define o sql da consulta
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (
            PreparedStatement comando = conexao.prepareStatement(sql) //cria o comando para receber sql dinamico
        ) {
            //substitui a ? pelo codigo do usuario
            comando.setInt(1, id);

            //executa o comando sql
            ResultSet resultado = comando.executeQuery();

            //verifica se tem algum resultado retornado pelo banco
            if (resultado.next()) {
                //cria o objeto pessoa com os dados retornados do banco
                pessoa = new Usuario(
                    resultado.getInt("id"),
                    resultado.getString("nome"),
                    resultado.getString("email") 
                );                 
            } 
                
            //retorna o objeto pessoa
            return pessoa;
        }
    }

    //exclui um usuario pelo ID
    //Entrada: Tipo int. ID do usuario a ser excluido
    //Retorno: Tipo int. Retorna a quatidade de linhas excluidas
    public static int excluirPorID(int id) throws SQLException{
        //define o sql de exclusao
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (
            PreparedStatement comando = conexao.prepareStatement(sql) //cria a conexao com o sql a ser preparado
        ) {
            //substitui a ? pelo id do usuario
            comando.setInt(1, id);

            //executa a consulta e armazena o resultado da quantidade de linhas excluidas na variavel 
            int qtdeLinhasExcluidas = comando.executeUpdate();

            return qtdeLinhasExcluidas;
        } catch (Exception e) {
            throw e;
        } 
    }

    //atualiza um usuario pelo ID
    //Entrada: Tipo Usuario. Ojbeto usuario a ser atualizado
    //Retorno: Tipo int. Retorna a quatidade de linhas excluidas
    public static int atualizarUsuario(Usuario usuario) throws SQLException{
        //define o sql 
        String sql = "UPDATE usuario SET nome = ?, email = ? WHERE id = ?";

        try (
            PreparedStatement comando = conexao.prepareStatement(sql) //cria a conexao para receber o sql dinamico
        ) {
            //substitui as ? pelos valores de nome, email e id
            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getEmail());
            comando.setInt(3, usuario.getId());

            //executa a atualizacao e armazena o retorno do banco com a quantidade de linhas atualizadas
            int qtdeLinhasAlteradas = comando.executeUpdate();

            //retorna a quantidade de linhas atualizadas
            return qtdeLinhasAlteradas;
        }
    }
}
