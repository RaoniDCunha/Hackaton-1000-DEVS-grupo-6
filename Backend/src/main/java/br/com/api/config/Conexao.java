package br.com.api.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/api"; //Alterar para o nome do seu banco de dados
    private static final String USUARIO = "root"; // Altere para seu usuário
    private static final String SENHA = "12345678";  // Altere para sua senha

    public static Connection getConexao() throws Exception {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

}