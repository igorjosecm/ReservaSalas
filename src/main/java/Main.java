import classes.Neo4jConnection;
import org.neo4j.driver.Driver;
import views.MainView;

import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // Tenta obter o driver. O próprio método getDriver() já imprime o erro detalhado.
        Driver driver = Neo4jConnection.getDriver();

        // Verifica se a conexão foi bem-sucedida antes de continuar.
        if (driver != null) {
            try {
                // O menu principal só é chamado se a conexão for bem-sucedida.
                MainView.menuPrincipal(driver, input);
            }
            catch (Exception e) {
                // Captura qualquer erro inesperado que possa ocorrer *durante* a execução do menu.
                System.err.println("Ocorreu um erro inesperado durante a execução da aplicação: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Garante que a conexão seja fechada ao final
                System.out.println("\nFechando conexão com o banco de dados.");
                Neo4jConnection.close();
            }
        }

        // A mensagem de encerramento é exibida em todos os casos (sucesso ou falha inicial).
        System.out.println("Aplicação encerrada.");
        input.close();
    }
}
