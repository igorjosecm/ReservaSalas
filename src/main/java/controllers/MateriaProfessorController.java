package controllers;

import classes.MateriaLecionada;
import classes.MateriaProfessorRelacao;
import dao.MateriaDAO;
import dao.MateriaProfessorDAO;
import dao.ProfessorDAO;
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
    private final ProfessorDAO professorDAO;
    private final MateriaDAO materiaDAO;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MateriaProfessorController(Driver driver) {
        this.materiaProfessorDAO = new MateriaProfessorDAO(driver);
        this.professorController = new ProfessorController(driver);
        this.materiaController  = new MateriaController(driver);
        this.professorDAO = new ProfessorDAO(driver);
        this.materiaDAO = new MateriaDAO(driver);
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

    /**
     * Exibe todas as relações existentes para o usuário.
     */
    public void findAllMateriaProfessor() {
        System.out.println("\n- Listando todas as relações Matéria-Professor:");
        try {
            List<MateriaProfessorRelacao> relacoes = materiaProfessorDAO.findAllLecionaRelationships();
            if (relacoes.isEmpty()) {
                System.out.println("Nenhuma relação encontrada.");
                return;
            }
            for (MateriaProfessorRelacao rel : relacoes) {
                System.out.println("------------------------------");
                System.out.println("  Professor: " + rel.getNomeProfessor() + " (Matrícula: " + rel.getMatriculaProfessor() + ")");
                System.out.println("  Matéria: " + rel.getNomeMateria() + " (Código: " + rel.getCodigoMateria().trim() + ")");
                if (rel.getInicioPeriodo() != null && rel.getFimPeriodo() != null) {
                    System.out.println("  Período: " + rel.getInicioPeriodo().format(formatter) +
                            " a " + rel.getFimPeriodo().format(formatter));
                }
            }
        } catch (Neo4jException e) {
            System.err.println("\nErro ao buscar as relações: " + e.getMessage());
        }
    }

    public void deleteMateriaProfessor() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Desvincular Matéria de Professor");

        try {
            findAllMateriaProfessor();

            System.out.print("\nCódigo da matéria: ");
            String codigoMateria = input.nextLine();

            if (materiaDAO.findById(codigoMateria) == null) {
                System.out.println("\nErro: Matéria não encontrada.");
                return;
            }

            System.out.print("Matrícula do professor: ");
            Integer matriculaProfessor = Helpers.getIntInput(input);

            if (professorDAO.findById(matriculaProfessor) == null) {
                System.out.println("\nErro: Professor não encontrado.");
                return;
            }

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

            System.out.print("\nDigite a matrícula do professor: ");
            Integer matriculaProfessor = Helpers.getIntInput(input);

            List<MateriaLecionada> materias = materiaProfessorDAO.findMateriasLecionadasPorProfessor(matriculaProfessor);

            if (materias.isEmpty()) {
                System.out.println("\nNenhuma matéria encontrada para este professor.");
                return;
            }

            System.out.println("\nMatérias lecionadas pelo professor " + matriculaProfessor + ":");
            for (MateriaLecionada materia : materias) {
                System.out.println("------------------------------");
                System.out.println("  Matéria: " + materia.getNomeMateria() + " (" + materia.getCodigoMateria().trim() + ")");

                // Exibe as novas informações de período
                if (materia.getInicioPeriodo() != null && materia.getFimPeriodo() != null) {
                    System.out.println("  Período: " + materia.getInicioPeriodo().format(formatter) +
                            " a " + materia.getFimPeriodo().format(formatter));
                } else {
                    System.out.println("  Período: Não especificado.");
                }
            }

        } catch (Neo4jException e) {
            System.err.println("\nErro ao buscar as matérias: " + e.getMessage());
        }
    }
}
