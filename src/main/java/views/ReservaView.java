package views;

import helpers.Helpers;
import controllers.ReservaController;
import org.neo4j.driver.Driver;

import java.sql.SQLException;
import java.util.Scanner;

public class ReservaView {

    public static void menuReserva(Driver driver, Scanner input) throws SQLException {
        ReservaController reservaController = new ReservaController(driver);

        boolean rodando = true;
        while (rodando) {
            printMenuReserva();
            int opcao = Helpers.getIntInput("", input);

            switch (opcao) {
                case 1:
                    reservaController.createReserva();
                    break;
                case 2:
                    reservaController.updateReserva();
                    break;
                case 3:
                    reservaController.deleteReserva();
                    break;
                case 4:
                    reservaController.findReservaById();
                    break;
                case 5:
                    reservaController.findAllReservas();
                    break;
                case 6:
                    reservaController.findReservasByBloco();
                    break;
                case 7:
                    reservaController.findReservasByPeriodo();
                    break;
                case 8:
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

    private static void printMenuReserva() {
        System.out.println("\n=========== Salas ============");
        System.out.println("1. Cadastrar reserva");
        System.out.println("2. Alterar reserva");
        System.out.println("3. Excluir reserva");
        System.out.println("4. Buscar reserva");
        System.out.println("5. Listar reservas");
        System.out.println("6. Relatório de reservas por bloco");
        System.out.println("7. Relatório de reservas por período");
        System.out.println("8. Voltar ao menu principal");
        System.out.println("==============================");
    }
}
