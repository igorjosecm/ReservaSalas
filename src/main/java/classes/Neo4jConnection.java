package classes;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jConnection {
    private static Driver driver;

    public static Driver getDriver() {
        if (driver == null) {
            String uri = "neo4j://127.0.0.1:7687";
            String user = "neo4j";
            String password = "igor_bernardi";

            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
            System.out.println("Sucesso na conexão com o servidor Neo4J!");
        }
        return driver;
    }

    public static void close() {
        if (driver != null) {
            driver.close();
        }
    }
}
