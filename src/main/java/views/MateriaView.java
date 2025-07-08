package views;

import helpers.Helpers;
import controllers.MateriaController;
import controllers.MateriaProfessorController;
import org.neo4j.driver.Driver;

import java.sql.SQLException;
import java.util.Scanner;

public class MateriaView {

    public static void menuMateria(Driver driver, Scanner input) throws SQLException {
        MateriaController materiaController = new MateriaController(driver);
        MateriaProfessorController materiaProfessorController = new MateriaProfessorController(driver);

        boolean rodando = true;
        while (rodando) {
            printMenuMateria();
            int opcao = Helpers.getIntInput("", input);

            switch (opcao) {
                case 1:
                    materiaController.createMateria();
                    break;
                case 2:
                    materiaController.updateMateria();
                    break;
                case 3:
                    materiaController.deleteMateria();
                    break;
                case 4:
                    materiaController.findMateriaById();
                    break;
                case 5:
                    materiaController.findAllMaterias();
                    break;
                case 6:
                    materiaProfessorController.createMateriaProfessor();
                    break;
                case 7:
                    materiaProfessorController.deleteMateriaProfessor();
                    break;
                case 8:
                    materiaProfessorController.findMateriaProfessorById();
                    break;
                case 9:
                    materiaProfessorController.findAllMateriasProfessores();
                    break;
                case 10:
                    materiaProfessorController.findMateriasOfProfessor();
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

    private static void printMenuMateria() {
        System.out.println("\n=========== Matérias ============");
        System.out.println("1. Cadastrar matéria");
        System.out.println("2. Alterar matéria");
        System.out.println("3. Excluir matéria");
        System.out.println("4. Buscar matéria");
        System.out.println("5. Listar matérias");
        System.out.println("6. Cadastrar relação matéria e professor");
        System.out.println("7. Excluir relação matéria e professor");
        System.out.println("8. Buscar relação matéria e professor");
        System.out.println("9. Listar relações matéria e professor");
        System.out.println("10. Relatório de matérias que há relação com o professor");
        System.out.println("0. Voltar ao menu principal");
        System.out.println("==============================");
    }
}
