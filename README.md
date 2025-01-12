# Microsserviço Spring Boot - Gestão de Produtos

Este repositório contém a implementação de um microsserviço desenvolvido em **Spring Boot** para gerenciar uma lista de produtos. Ele foi projetado com foco em **operações CRUD**, integração com **RabbitMQ**, e aderência aos **princípios SOLID**. Além disso, o projeto inclui testes automatizados, integração contínua, e suporte para execução com Docker.

## Funcionalidades
1. **CRUD de Produtos**: API RESTful para gerenciar produtos.
2. **Testes Automatizados**: Testes unitários com JUnit e Mockito.
3. **Mensageria**: Publicação de mensagens em RabbitMQ para novos produtos.
4. **Integração com MySQL:**: Banco de dados com migrações Flyway..
5. **Escalabilidade**: Configuração para rodar na AWS (ECS ou Lambda).
6. **Adesão a SOLID**: Código refatorado para boas práticas de design.

---

## Exercícios Implementados

### Exercício 1: CRUD de Produtos
- API RESTful com operações de criação, leitura, atualização e exclusão de produtos.
  
### Exercício 2: Testes e CI/CD
- Testes unitários utilizando **JUnit** e **Mockito**.
- Pipeline de integração contínua configurado com **GitHub Actions**.

### Exercício 3: Mensageria e AWS
- Publicação de mensagens em **RabbitMQ** ao criar produtos.
- Consumidor para ler mensagens da fila e registrar logs.
- Configuração para execução em **AWS ECS** ou **AWS Lambda**.

### Exercício 5: Banco de Dados e Filtros
- Integração com banco de dados **MySQL** utilizando **Flyway**.
- ndpoint para buscar produtos com filtros (nome, categoria, preço).

---

## Requisitos
- **Java 17+**
- **Spring Boot 3+**
- **Docker** e **Docker Compose**
- **MySQL**
- **RabbitMQ**
- Conta na AWS (opcional para deploy em ECS ou Lambda)

---

## Como Rodar o Projeto Localmente com Docker

### Pré-requisitos
- Instale o [Docker](https://www.docker.com/) e o [Docker Compose](https://docs.docker.com/compose/).

### Passos
1. Clone o repositório:
   ```bash
   git clone https://github.com/mtbassi/exerc5-itau-unibanco.git
   cd exerc5-itau-unibanco
   ```

2. Compile o projeto:
   ```bash
   ./mvnw clean package
   ```

3. Suba os serviços com Docker Compose:
   ```bash
   docker-compose up --build
   ```

4. Acesse a aplicação:
   - **API local**: [`http://localhost:8080`](http://localhost:8080)
   - **RabbitMQ (Admin)**: [`http://localhost:15672`](http://localhost:15672) (usuário: `user`, senha: `password`)

5. **API hospedada na AWS ECS**:
   Você também pode acessar a API através do endereço público hospedado na AWS ECS:
   - **API Pública**: [`http://15.229.13.93:8080`](http://15.229.13.93:8080)

---

## Endpoints da API

### 1. **Listar todos os produtos**
- **URL**: `/v1/produto`
- **Método**: `GET`
- **Descrição**: Retorna uma lista completa de todos os produtos cadastrados.
- **Resposta**:
  - **200 OK**: Lista de produtos.
  - **400 Bad Request**: Parâmetros inválidos.
  - **500 Internal Server Error**: Erro no servidor.
- **Exemplo de resposta**:
  ```json
  [
    {
      "id": 1,
      "nome": "Produto A",
      "preco": 100.0,
      "categoria": "Categoria A"
    }
  ]
  ```

### 2. **Consultar produto por ID**
- **URL**: `/v1/produto/{id}`
- **Método**: `GET`
- **Descrição**: Retorna os detalhes de um produto específico com base no ID.
- **Resposta**:
  - **200 OK**: Produto encontrado.
  - **400 Bad Request**: Parâmetros inválidos.
  - **422 Unprocessable Entity**: Produto não encontrado para o ID fornecido.
  - **500 Internal Server Error**: Erro no servidor.
- **Exemplo de resposta**:
  ```json
  {
    "id": 1,
    "nome": "Produto A",
    "preco": 100.0,
    "categoria": "Categoria A"
  }
  ```
### 3. **Consultar produto por nome, preço e categoria**
- **URL**: `/v1/produto/busca`
- **Método**: `GET`
- **Descrição**: Retorna os detalhes de uma lista de produtos com base no nome, preço e categoria fornecidos. Os parâmetros são opcionais, permitindo consultas mais flexíveis.
- **Parâmetros**:
  - `nome` (opcional): Nome do produto para filtro.
  - `preco` (opcional): Preço do produto para filtro.
  - `categoria` (opcional): Categoria do produto para filtro.
- **Resposta**:
  - **200 OK**: Lista de produtos que atendem aos critérios fornecidos.
  - **400 Bad Request**: Parâmetros inválidos.
  - **422 Unprocessable Entity**: Nenhum produto encontrado para os critérios fornecidos.
  - **500 Internal Server Error**: Erro no servidor.
- **Exemplo de resposta**:
  ```json
  [
    {
      "id": 1,
      "nome": "Produto A",
      "descricao": "Descrição do produto A",
      "preco": 100.0,
      "categoria": "Categoria A"
    },
    {
      "id": 2,
      "nome": "Produto B",
      "descricao": "Descrição do produto B",
      "preco": 150.0,
      "categoria": "Categoria B"
    }
  ]
  ```

### 4. **Cadastrar novo produto**
- **URL**: `/v1/produto`
- **Método**: `POST`
- **Descrição**: Realiza o cadastro de um novo produto utilizando os dados fornecidos no corpo da requisição.
- **Payload (Exemplo)**:
  ```json
  {
    "nome": "Produto B",
    "preco": 150.0,
    "categoria": "Categoria B"
  }
  ```
- **Resposta**:
  - **201 Created**: Produto cadastrado com sucesso.
  - **400 Bad Request**: Parâmetros inválidos.
  - **422 Unprocessable Entity**: Dados inválidos ou ausentes no cadastro.
  - **500 Internal Server Error**: Erro no servidor.

### 5. **Atualizar produto existente**
- **URL**: `/v1/produto/{id}`
- **Método**: `PUT`
- **Descrição**: Atualiza as informações de um produto com base no ID fornecido.
- **Payload (Exemplo)**:
  ```json
  {
    "nome": "Produto A Atualizado",
    "preco": 120.0,
    "categoria": "Categoria A"
  }
  ```
- **Resposta**:
  - **200 OK**: Produto atualizado com sucesso.
  - **400 Bad Request**: Parâmetros inválidos.
  - **422 Unprocessable Entity**: Produto não encontrado para o ID fornecido.
  - **500 Internal Server Error**: Erro no servidor.

### 6. **Deletar produto por ID**
- **URL**: `/v1/produto/{id}`
- **Método**: `DELETE`
- **Descrição**: Remove um produto do sistema utilizando o ID fornecido na requisição.
- **Resposta**:
  - **204 No Content**: Produto deletado com sucesso.
  - **400 Bad Request**: Parâmetros inválidos.
  - **422 Unprocessable Entity**: Produto não encontrado para o ID fornecido.
  - **500 Internal Server Error**: Erro no servidor.

---

## Pipeline de CI/CD
- O projeto possui um pipeline configurado para:
  - Executar testes automatizados a cada push.
  - Gerar relatórios de cobertura de testes.

### Configuração do Pipeline
O arquivo do pipeline está disponível em `.github/workflows/ci.yml`.

## Relatório de Testes

Após a execução da pipeline, um relatório de testes gerado com Allure é publicado automaticamente no GitHub Pages. Você pode acessá-lo em:

- [`https://mtbassi.github.io/exerc5-itau-unibanco/`](https://mtbassi.github.io/exerc5-itau-unibanco/)

---

## Documentação da API
Acesse a documentação completa da API gerada com **Swagger**:
- [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

---

## Contribuição
Sinta-se à vontade para abrir issues e enviar pull requests com melhorias ou correções. 

---

## Licença
Este projeto é licenciado sob a [MIT License](LICENSE).
