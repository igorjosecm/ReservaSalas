package views;

import controllers.BlocoController;
import helpers.Helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class BlocoView {
    public static void menuBloco(Connection con, Scanner input) throws SQLException {
        BlocoController blocoController = new BlocoController(con);

        boolean rodando = true;
        while (rodando) {
            printMenuBloco();
            int opcao = Helpers.getIntInput("", input);

            switch (opcao) {
                case 1:
                    blocoController.createBloco();
                    break;
                case 2:
                    blocoController.updateBloco();
                    break;
                case 3:
                    blocoController.deleteBloco();
                    break;
                case 4:
                    blocoController.findBlocoById();
                    break;
                case 5:
                    blocoController.findAllBlocos();
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
                input.nextLine();
            }
        }
    }

    private static void printMenuBloco() {
        System.out.println("\n=========== Blocos ===========");
        System.out.println("1. Cadastrar bloco");
        System.out.println("2. Alterar bloco");
        System.out.println("3. Excluir bloco");
        System.out.println("4. Buscar bloco");
        System.out.println("5. Listar blocos");
        System.out.println("6. Voltar ao menu principal");
        System.out.println("==============================");
    }
}
