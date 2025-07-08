import classes.Neo4jConnection;
import org.neo4j.driver.Driver;
import views.MainView;

import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        try (Driver driver = Neo4jConnection.getDriver()) {
            MainView.menuPrincipal(driver, input);
        } catch (Exception e) {
            System.err.println("Erro na aplicação: " + e.getMessage());
            e.printStackTrace();
        } finally {
            input.close();
        }
    }
}
