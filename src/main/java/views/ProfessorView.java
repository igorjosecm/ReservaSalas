package views;

import helpers.Helpers;
import org.neo4j.driver.Driver;
import controllers.ProfessorController;

import java.util.Scanner;

public class ProfessorView {
    public static void menuProfessor(Driver driver, Scanner input) {
        ProfessorController professorController = new ProfessorController(driver);

        boolean rodando = true;
        while (rodando) {
            printMenuProfessor();
            int opcao = Helpers.getIntInput("", input);

            switch (opcao) {
                case 1:
                    professorController.createProfessor();
                    break;
                case 2:
                    professorController.updateProfessor();
                    break;
                case 3:
                    professorController.deleteProfessor();
                    break;
                case 4:
                    professorController.findProfessorById();
                    break;
                case 5:
                    professorController.findAllProfessores();
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

    private static void printMenuProfessor() {
        System.out.println("\n=========== Salas ============");
        System.out.println("1. Cadastrar professor");
        System.out.println("2. Alterar professor");
        System.out.println("3. Excluir professor");
        System.out.println("4. Buscar professor");
        System.out.println("5. Listar professores");
        System.out.println("0. Voltar ao menu principal");
        System.out.println("==============================");
    }
}
