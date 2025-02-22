# Gerenciador de Vacinas - Hackathon 2025 1000devs

<img src="img/1719261715076.webp" alt="Banner do Projeto" width="800" />

Um sistema para gerenciar as vacinas aplicadas aos integrantes de uma família, permitindo o cadastro de pacientes, registro de imunizações, consulta do calendário vacinal por idade recomendada e geração de estatísticas.

---

## 🎯 Objetivo

Desenvolver um software que permita o gerenciamento das vacinas aplicadas aos integrantes de uma família. O sistema deve:
- Facilitar o cadastro de pacientes e vacinas.
- Registrar as imunizações aplicadas.
- Permitir a consulta do calendário vacinal por idade recomendada.
- Gerar estatísticas sobre vacinas aplicadas, atrasadas e próximas doses.

---

## 🛠️ Tecnologias Utilizadas

- **Java**: Linguagem de programação principal.
- **Spark**: Framework para criar APIs RESTful.
- **MySQL**: Banco de dados para persistência dos dados.
- **Maven**: Gerenciador de dependências.
- **Postman**: Ferramenta para testar e validar requisições de APIs.

---

## 🚀 Funcionalidades Principais

- **Cadastro de Pacientes**: Adicionar, alterar, excluir e consultar pacientes.
- **Registro de Imunizações**: Registrar vacinas aplicadas, com detalhes como data, dose, fabricante e local.
- **Consulta de Calendário Vacinal**: Visualizar vacinas recomendadas por faixa etária.
- **Estatísticas**: Obter informações sobre vacinas aplicadas, atrasadas e próximas doses.

---

## 🖥️ Como Baixar e Executar o Projeto

### 📌 Requisitos

Antes de rodar o projeto, certifique-se de ter os seguintes requisitos instalados:
- **Java 11+**
- **Maven 3+**
- **MySQL 8+**
- **Git**

### 📥 1. Clonar o Repositório

```sh
# Clone o repositório
git clone https://github.com/lucaxjordo/gerenciador-vacinas-java-spark.git

# Acesse o diretório do projeto
cd src/main
```

### 📦 2. Configurar o Banco de Dados

1. Instale o MySQL e crie um banco de dados usando o MySQL Workbench chamado api.
2. Execute o script SQL disponível em `scrip_sql_criacao_banco.txt` em um Query no Workbench para criar as tabelas.
3. Configure a conexão no arquivo `src/main/java/br/com/api/config/Conexao.java`, alterando os valores conforme necessário:

```java
public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/api";
    private static final String USUARIO = "<seu_usuario>";
    private static final String SENHA = "<sua_senha>";
}
```

### 🚀 3. Compilar e Executar o Projeto

1. Compile o projeto:

```sh
mvn clean install
```

2. Execute a aplicação:

```sh
mvn exec:java
```

A API estará disponível em: [http://localhost:3000](http://localhost:3000)

---

## 🔗 Endpoints da API

### 🏥 Paciente

| Método   | Rota                      | Descrição                                                 |
| -------- | ------------------------- | --------------------------------------------------------- |
| `POST`   | `/paciente/inserir`       | Adiciona um novo paciente. Retorna ID do paciente criado. |
| `PUT`    | `/paciente/alterar/:id`   | Altera dados de um paciente existente.                    |
| `DELETE` | `/paciente/excluir/:id`   | Exclui um paciente pelo ID.                               |
| `GET`    | `/paciente/consultar`     | Retorna todos os pacientes cadastrados.                   |
| `GET`    | `/paciente/consultar/:id` | Retorna os dados de um paciente específico pelo ID.       |

### 💉 Imunizações

| Método   | Rota                                                           | Descrição                                                           |
| -------- | -------------------------------------------------------------- | ------------------------------------------------------------------- |
| `POST`   | `/imunizacao/inserir`                                          | Adiciona uma nova imunização. Retorna ID da imunização criada.      |
| `PUT`    | `/imunizacao/alterar/:id`                                      | Altera os dados de uma imunização existente.                        |
| `DELETE` | `/imunizacao/excluir/:id`                                      | Exclui uma imunização pelo ID.                                      |
| `DELETE` | `/imunizacao/excluir/paciente/:id`                             | Exclui todas as imunizações de um paciente específico.              |
| `GET`    | `/imunizacao/consultar`                                        | Retorna todas as imunizações registradas.                           |
| `GET`    | `/imunizacao/consultar/:id`                                    | Retorna os dados de uma imunização específica.                      |
| `GET`    | `/imunizacao/consultar/paciente/:id`                           | Retorna todas as imunizações de um paciente.                        |
| `GET`    | `/imunizacao/consultar/paciente/:id/aplicacao/:dt_ini/:dt_fim` | Retorna imunizações de um paciente dentro de um período específico. |

### 💊 Vacinas

| Método | Rota                                             | Descrição                                                                         |
| ------ | ------------------------------------------------ | --------------------------------------------------------------------------------- |
| `GET`  | `/vacinas/consultar`                             | Retorna todas as vacinas cadastradas.                                             |
| `GET`  | `/vacinas/consultar/faixa_etaria/:faixa`         | Retorna vacinas recomendadas para uma faixa etária.                               |
| `GET`  | `/vacinas/consultar/idade_maior/:meses`          | Retorna vacinas recomendadas para idades acima de um determinado número de meses. |
| `GET`  | `/vacinas/consultar/nao_aplicaveis/paciente/:id` | Retorna vacinas que não são mais aplicáveis a um paciente devido à idade.         |

### 📊 Estatísticas

| Método | Rota                                                | Descrição                                                                   |
| ------ | --------------------------------------------------- | --------------------------------------------------------------------------- |
| `GET`  | `/estatisticas/imunizacoes/paciente/:id`            | Retorna a quantidade de vacinas aplicadas em um paciente.                   |
| `GET`  | `/estatisticas/proximas_imunizacoes/paciente/:id`   | Retorna a quantidade de vacinas aplicáveis no próximo mês para um paciente. |
| `GET`  | `/estatisticas/imunizacoes_atrasadas/paciente/:id`  | Retorna a quantidade de vacinas atrasadas de um paciente.                   |
| `GET`  | `/estatisticas/imunizacoes/idade_maior/:meses`      | Retorna a quantidade de vacinas aplicáveis acima de uma determinada idade.  |
| `GET`  | `/estatisticas/vacinas/nao_aplicaveis/paciente/:id` | Retorna a quantidade de vacinas não aplicáveis devido à idade do paciente.  |

---

## 📜 Exemplo de Resposta JSON

### ✅ Cadastro de Paciente

#### **Requisição**

```json
{
  "nome": "Fulano de Tal",
  "cpf": "111.111.111-11",
  "sexo": "M",
  "dataNascimento": "2018-10-10"
}
```

#### **Resposta**

```json
{
  "id": 1,
  "mensagem": "Paciente cadastrado com sucesso."
}
```

### ✅ Consulta de Imunizações por Paciente

#### **Resposta**

```json
[
  {
    "id": 1,
    "paciente": "Fulano de Tal",
    "vacina": "BCG",
    "dose": "Dose Única",
    "dataAplicacao": "2018-10-11",
    "fabricante": "Fiocruz",
    "lote": "0644",
    "local": "Hospital Santa Fé",
    "profissional": "Beltrano de Tal"
  }
]
```


## 👥 Equipe

Este projeto foi desenvolvido pelo **Grupo 6**, composto por:

- **Ariel Pires de Pula**
- **Gabriel de Oliveira Medeiros**
- **Gustavo Farias Tavares**
- **Lucas Vinicius de Oliveira**
- **Raoni Dutra Cunha**
- **Renata Santos Celestino**
- **Vinicius Borges de Araújo**

---

## 📜 Sobre o Programa

Este projeto foi desenvolvido durante o programa **1000Devs**, promovido pela **Mesttra** em parceria com a **Johnson & Johnson** e o **Hospital Israelita Albert Einstein**. 

---

