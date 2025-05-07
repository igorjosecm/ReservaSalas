package views;

import controllers.SalaController;
import helpers.Helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class SalaView {
    public static void menuSala(Connection con, Scanner input) throws SQLException {
        SalaController salaController = new SalaController(con);

        boolean rodando = true;
        while (rodando) {
            printMenuSala();
            int opcao = Helpers.getIntInput("", input);

            switch (opcao) {
                case 1:
                    salaController.createSala();
                    break;
                case 2:
                    salaController.updateSala();
                    break;
                case 3:
                    salaController.deleteSala();
                    break;
                case 4:
                    salaController.findSalaById();
                    break;
                case 5:
                    salaController.findAllSalas();
                    break;
                case 6:
                    salaController.findSalasByBloco();
                    break;
                case 7:
                    rodando = false;
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }

            if (rodando) {
                System.out.println("\nPressione ENTER para continuar...");
                input.nextLine();
            }
        }
    }

    private static void printMenuSala() {
        System.out.println("\n=========== Salas ============");
        System.out.println("1. Cadastrar sala");
        System.out.println("2. Alterar sala");
        System.out.println("3. Excluir sala");
        System.out.println("4. Buscar sala");
        System.out.println("5. Listar salas");
        System.out.println("6. Relatório de salas por bloco");
        System.out.println("7. Voltar ao menu principal");
        System.out.println("==============================");
    }
}
