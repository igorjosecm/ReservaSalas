package views;

import helpers.Helpers;
import org.neo4j.driver.Driver;

import java.util.Scanner;

public class MainView {
    static public void menuPrincipal(Driver driver, Scanner input) {
        boolean rodando = true;
        while (rodando) {
            printMenu();
            int opcao = Helpers.getIntInput("", input);

            switch (opcao) {
                case 1:
                    ReservaView.menuReserva(driver, input);
                    break;
                case 2:
                    SalaView.menuSala(driver, input);
                    break;
                case 3:
                    BlocoView.menuBloco(driver, input);
                    break;
                case 4:
                    ProfessorView.menuProfessor(driver, input);
                    break;
                case 5:
                    MateriaView.menuMateria(driver, input);
                    break;
                case 0:
                    rodando = false;
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        }
        System.out.println("\nFechando aplicação. Até mais!");
    }

    private static void printMenu() {
        System.out.println("\n====== Reserva de Salas ======");
        System.out.println("1. Reservar sala");
        System.out.println("2. Cadastro de salas");
        System.out.println("3. Cadastro de blocos");
        System.out.println("4. Matrícula de professores");
        System.out.println("5. Cadastro de matérias");
        System.out.println("0. Sair");
        System.out.println("==============================");
    }
}
