package controllers;

import classes.Materia;
import dao.MateriaProfessorDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;
import org.neo4j.driver.exceptions.Neo4jException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MateriaProfessorController {
    private final MateriaProfessorDAO materiaProfessorDAO;
    private final ProfessorController professorController;
    private final MateriaController materiaController;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MateriaProfessorController(Driver driver) {
        this.materiaProfessorDAO = new MateriaProfessorDAO(driver);
        this.professorController = new ProfessorController(driver);
        this.materiaController  = new MateriaController(driver);
    }

    public void createOrUpdateMateriaProfessor() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Vincular Matéria a Professor (ou atualizar período):");

        try {
            materiaController.findAllMaterias();
            System.out.print("\nCódigo da matéria: ");
            String codigoMateria = input.nextLine();

            professorController.findAllProfessores();
            System.out.print("\nMatrícula do professor: ");
            Integer matriculaProfessor = Helpers.getIntInput(input);

            LocalDate inicioPeriodo;
            LocalDate fimPeriodo;
            while (true) {
                try {
                    System.out.print("Início do período (dd/mm/aaaa): ");
                    inicioPeriodo = LocalDate.parse(input.nextLine(), formatter);
                    System.out.print("Fim do período (dd/mm/aaaa): ");
                    fimPeriodo = LocalDate.parse(input.nextLine(), formatter);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
                }
            }

            materiaProfessorDAO.createLecionaRelationship(matriculaProfessor, codigoMateria, inicioPeriodo, fimPeriodo);
            System.out.println("\nRelação criada/atualizada com sucesso!");

        } catch (Neo4jException e) {
            System.err.println("\nErro ao criar o vínculo: " + e.getMessage());
            System.err.println("Verifique se a matrícula e o código da matéria existem.");
        }
    }

    public void deleteMateriaProfessor() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Desvincular Matéria de Professor");

        try {
            System.out.print("Código da matéria: ");
            String codigoMateria = input.nextLine();
            System.out.print("Matrícula do professor: ");
            Integer matriculaProfessor = Helpers.getIntInput(input);

            materiaProfessorDAO.deleteLecionaRelationship(matriculaProfessor, codigoMateria);
            System.out.println("\nRelação removida com sucesso!");
        } catch (Neo4jException e) {
            System.err.println("\nErro ao remover o vínculo: " + e.getMessage());
        }
    }

    public void findMateriasOfProfessor() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Relatório: Buscar matérias de um professor");

        try {
            professorController.findAllProfessores();
            System.out.print("\nMatrícula do professor: ");
            Integer matriculaProfessor = Helpers.getIntInput(input);

            List<Materia> materias = materiaProfessorDAO.findMateriasOfProfessor(matriculaProfessor);

            if (materias.isEmpty()) {
                System.out.println("\nNenhuma matéria encontrada para este professor.");
                return;
            }

            System.out.println("\nMatérias lecionadas pelo professor " + matriculaProfessor + ":");
            for (Materia materia : materias) {
                System.out.println("  - " + materia.getNomeMateria() + " (" + materia.getCodigoMateria().trim() + ")");
            }

        } catch (Neo4jException e) {
            System.err.println("\nErro ao buscar as matérias: " + e.getMessage());
        }
    }
}
