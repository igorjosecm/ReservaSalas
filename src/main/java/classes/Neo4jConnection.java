package classes;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.exceptions.AuthenticationException;
import org.neo4j.driver.exceptions.ServiceUnavailableException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Neo4jConnection {
    private static Driver driver;

    /**
     * Obtém a instância do driver Neo4j.
     * Na primeira chamada, cria o driver e verifica a conectividade com o banco.
     * @return A instância do Driver.
     * @throws ServiceUnavailableException se a conexão com o banco não puder ser estabelecida.
     */
    public static Driver getDriver() {
        if (driver == null) {
            try {
                Properties props = new Properties();
                try (InputStream input = Neo4jConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
                    if (input == null) {
                        System.err.println("Erro: Não foi possível encontrar o arquivo 'config.properties'.");
                        System.err.println("Certifique-se de que o arquivo está na pasta 'src/main/resources'.");
                        return null;
                    }
                    props.load(input);
                }

                String uri = props.getProperty("db.uri");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
                driver.verifyConnectivity();
                System.out.println("Sucesso na conexão e verificação com o servidor Neo4J!");

            } catch (IOException e) {
                System.err.println("Erro: Falha ao ler o arquivo 'config.properties'.");
                return null;
            } catch (ServiceUnavailableException | AuthenticationException e) {
                System.err.println("\nErro: Não foi possível conectar ao banco de dados Neo4j.");
                System.err.println("Por favor, verifique se o banco de dados está em execução e as credenciais no 'config.properties' estão corretas.");
                return null;
            }
        }
        return driver;
    }

    /**
     * Fecha a instância do driver se ela estiver aberta.
     */
    public static void close() {
        if (driver != null) {
            driver.close();
            driver = null;
        }
    }
}
