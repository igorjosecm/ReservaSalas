import classes.Neo4jConnection;
import org.neo4j.driver.Driver;
import org.neo4j.driver.exceptions.Neo4jException;
import views.MainView;

import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Driver driver = null;
        try {
            driver = Neo4jConnection.getDriver();

            MainView.menuPrincipal(driver, input);

        } catch (Neo4jException e) {
            System.err.println("A aplicação será encerrada.");
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Neo4jConnection.close();
            input.close();
        }
    }
}
