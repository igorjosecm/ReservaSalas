package classes;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.exceptions.ServiceUnavailableException;

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
            String uri = "neo4j://127.0.0.1:7687";
            String user = "neo4j";
            String password = "igor_bernardi";

            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));

            try {
                driver.verifyConnectivity();
                System.out.println("Sucesso na conexão com o servidor do Neo4J!");
            } catch (ServiceUnavailableException e) {
                System.err.println("\nErro: Não foi possível conectar ao banco de dados Neo4j.");
                System.err.println("Por favor, verifique se o banco de dados está em execução e as credenciais estão corretas.");
                throw e;
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
