package views;

import helpers.Helpers;
import controllers.BlocoController;
import org.neo4j.driver.Driver;

import java.util.Scanner;

public class BlocoView {
    public static void menuBloco(Driver driver, Scanner input) {
        BlocoController blocoController = new BlocoController(driver);

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
                case 0:
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
        System.out.println("0. Voltar ao menu principal");
        System.out.println("==============================");
    }
}
