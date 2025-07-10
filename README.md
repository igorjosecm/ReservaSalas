# Sistema de Reserva de Salas com Neo4j

Uma aplicação de console em Java para gerenciamento de reservas de salas, utilizando Neo4j como banco de dados. Um projeto criado para a disciplina de Banco de Dados II - UDESC.

Alunos: Igor José da Costa Mota e Gabriel Zapellaro Bernardi


-----

## Pré-requisitos

* **Java JDK 17** ou superior
* **Apache Maven**
* **Neo4j Desktop**

## Como Executar

#### Siga estes 5 passos para configurar e rodar o projeto:

### 1\. Clone o Repositório

```bash
git clone https://github.com/igorjosecm/ReservaSalas.git
cd ReservaSalas
```

### 2\. Configure o Banco de Dados Neo4j

1.  No **Neo4j Desktop**, crie um novo banco de dados local.
2.  Defina uma **senha** para o usuário `neo4j` e anote-a.
3.  Inicie o banco de dados clicando em **"Start"**.

### 3\. Crie o Arquivo de Conexão

A aplicação precisa de um arquivo de configuração para se conectar ao banco.

1.  Dentro do projeto, crie a seguinte estrutura de pastas: `src/main/resources/`.

2.  Dentro da pasta `resources`, crie um arquivo chamado `config.properties`.

3.  Cole o seguinte conteúdo no arquivo, substituindo `SUA_SENHA_AQUI` pela senha que você criou:

    ```properties
    # Credenciais do Banco de Dados Neo4j
    db.uri=neo4j://localhost:7687
    db.user=neo4j
    db.password=SUA_SENHA_AQUI
    ```

### 4\. Importe os Dados

Utilize o script de backup (`reserva_salas_backup.cypher`) localizado na raiz do projeto, para popular o banco de dados:

1.  Abra o **Neo4j Browser** no seu banco de dados em execução.
2.  Cole o conteúdo do seu script Cypher e execute-o.

### 5\. Compile e Rode a Aplicação

1.  Abra um terminal na raiz do projeto.
2.  Compile a aplicação com o Maven:
    ```bash
    mvn clean install
    ```
3.  Execute a aplicação pela sua IDE (rodando a classe `Main.java`) ou pelo terminal:
    ```bash
    java -jar target/ReservaSalas-1.0-SNAPSHOT.jar
    ```

A aplicação irá iniciar, verificar a conexão com o banco de dados e, se tudo estiver correto, o menu principal será exibido.