import classes.DatabaseConnection;
import controllers.SalaController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            SalaController salaController = new SalaController(conn);

            boolean rodando = true;
            while (rodando) {
                printMenu();
                int choice = getIntInput("Selecione a opção desejada: ");

                switch (choice) {
                    case 1:
                        salaController.createSala();
                        break;
                    case 2:
                        salaController.findSalaById();
                        break;
                    case 3:
                        salaController.updateSala();
                        break;
                    case 4:
                        salaController.deleteSala();
                        break;
                    case 5:
                        salaController.findAllSalas();
                        break;
                    case 6:
                        rodando = false;
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                        break;
                }

                if (rodando) {
                    System.out.println("\nPressione ENTER para continuar...");
                    scanner.nextLine();
                }
            }

            System.out.println("Fechando aplicação. Até mais!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void printMenu() {
        System.out.println("====== Reserva de Salas ======");
        System.out.println("1. Adicionar sala");
        System.out.println("2. Buscar sala");
        System.out.println("3. Alterar sala");
        System.out.println("4. Excluir sala");
        System.out.println("5. Listar salas");
        System.out.println("6. Sair");
        System.out.println("==============================");
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Por favor informe um número inteiro.");
            }
        }
    }
}
