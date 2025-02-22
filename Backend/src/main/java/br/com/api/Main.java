package br.com.api;

import br.com.api.dao.*;
import spark.Spark;

import java.sql.Connection;

import br.com.api.config.Conexao;

import br.com.api.routes.Rotas;

import spark.Request;
import spark.Response;
import spark.Route;

public class Main {
    
    public static void main(String[] args) {

        try {
            //obtem uma conexao valida com o banco de dados
            Connection conexao = Conexao.getConexao(); 

            //Atribui a conexao criada, no atributo da classe DAOUsuario
            DAOUsuario.conexao = conexao;
            DAODose.conexao = conexao;
            DAOPaciente.conexao = conexao;
            DAOImunizacao.conexao = conexao;
            DAOVacina.conexao = conexao;
            DAOEstatisticas.conexao = conexao;

            //TO DO: atribuir a mesma conexao nas demais classes DAO caso existam

            Spark.port(3000);

            //Habilitar CORS
            //Assista https://www.youtube.com/watch?v=1V1qkh6K8Gg para entender o que é
            Spark.options("/*", new Route() {
                @Override
                public Object handle(Request requisicaoHttp, Response respostaHttp) throws Exception {

                    String accessControlRequestHeaders = requisicaoHttp.headers("Access-Control-Request-Headers");
                    
                    if (accessControlRequestHeaders != null) 
                        respostaHttp.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                    
                    String accessControlRequestMethod = requisicaoHttp.headers("Access-Control-Request-Method");

                    if (accessControlRequestMethod != null) 
                        respostaHttp.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    
                    return "OK";
                }
            });

            //Informando o Browser que é aceito os metodos HTTP OPTIONS, GET, POST, PUT, DELETE para qualquer endereço
            Spark.before(new spark.Filter() {
                @Override
                public void handle(Request requisicaoHttp, Response respostaHttp) throws Exception {
                    respostaHttp.header("Access-Control-Allow-Origin", "*"); // Permite todas as origens
                    respostaHttp.header("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
                    respostaHttp.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
                }
            });



            //executa o metodo para cadastrar as rotas no spark
            Rotas.processarRotas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}