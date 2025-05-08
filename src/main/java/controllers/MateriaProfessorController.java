package controllers;

import classes.CompositeKey;
import classes.MateriaProfessor;
import classes.Professor;
import dao.MateriaProfessorDAO;
import dao.ProfessorDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MateriaProfessorController {
    private final MateriaProfessorDAO materiaProfessorDAO;
    private final ProfessorDAO professorDAO;
    private final ProfessorController professorController;
    private final MateriaController materiaController;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MateriaProfessorController(Connection connection) {
        this.materiaProfessorDAO = new MateriaProfessorDAO(connection);
        this.professorDAO = new ProfessorDAO(connection);
        this.professorController = new ProfessorController(connection);
        this.materiaController  = new MateriaController(connection);
    }

    public void createMateriaProfessor() throws SQLException {
        Scanner input = new Scanner(System.in);
        findAllMateriasProfessores();

        System.out.println("\n- Relacionar matéria a professor: ");
        materiaController.findAllMaterias();
        System.out.println("Código da materia: ");
        String codigoMateria = input.nextLine();
        professorController.findAllProfessores();
        System.out.println("Matrícula do professor: ");
        Integer matriculaProfessor = input.nextInt();

        LocalDate inicioPeriodo;
        LocalDate fimPeriodo;

        while (true) {
            try {
                System.out.print("Início do período (dd/mm/aaaa): ");
                inicioPeriodo = LocalDate.parse(input.nextLine(), formatter);

                System.out.print("Fim do período (dd/mm/aaaa): ");
                fimPeriodo = LocalDate.parse(input.nextLine(), formatter);

                if (inicioPeriodo.isAfter(fimPeriodo)) {
                    System.out.println("A data inicial não pode ser após a data final. Tente novamente.");
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
            }
        }

        CompositeKey compositeKey = new CompositeKey();
        compositeKey.addKey("matricula_professor", matriculaProfessor);
        compositeKey.addKey("codigo_materia", codigoMateria);

        MateriaProfessor materiaProfessor = materiaProfessorDAO.findById(compositeKey);
        if (materiaProfessor == null) {
            MateriaProfessor newMateriaProfessor = new MateriaProfessor();
            newMateriaProfessor.setCodigoMateria(codigoMateria);
            newMateriaProfessor.setMatriculaProfessor(matriculaProfessor);
            newMateriaProfessor.setInicioPeriodo(inicioPeriodo);
            newMateriaProfessor.setFimPeriodo(fimPeriodo);

            materiaProfessorDAO.create(newMateriaProfessor);
            System.out.println("\nRelação adicionada com sucesso!");
        } else {
            System.out.println("\nEssa matéria já foi relacionada a esse professor, " +
                    "altere o período para atualizá-la.");
        }
    }

    public void deleteMateriaProfessor() throws SQLException {
        Scanner input = new Scanner(System.in);
        findAllMateriasProfessores();
        System.out.println("\n- Remoção de relação de materia com professor");
        materiaController.findAllMaterias();
        System.out.println("Código da materia: ");
        String codigoMateria = input.nextLine();
        professorController.findAllProfessores();
        System.out.println("Matrícula do professor: ");
        Integer matriculaProfessor = input.nextInt();

        CompositeKey compositeKey = new CompositeKey();
        compositeKey.addKey("codigo_materia", codigoMateria);
        compositeKey.addKey("matricula_professor",matriculaProfessor);

        MateriaProfessor materiaProfessor = materiaProfessorDAO.findById(compositeKey);
        if (materiaProfessor != null) {
            materiaProfessorDAO.delete(compositeKey);
            System.out.println("\nRelação removida com sucesso!");
        } else {
            System.out.println("\nRelação não encontrada.");
        }
    }

    public void findMateriaProfessorById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de relação por matéria e matrícula");
        materiaController.findAllMaterias();
        System.out.println("Código da materia: ");
        String codigoMateria = input.nextLine();
        professorController.findAllProfessores();
        System.out.println("Matrícula do professor: ");
        Integer matriculaProfessor = input.nextInt();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_materia", codigoMateria);
        key.addKey("matricula_professor",matriculaProfessor);

        MateriaProfessor materiaProfessor = materiaProfessorDAO.findById(key);
        if (materiaProfessor != null) {
            Professor professor = professorDAO.findById(matriculaProfessor);

            System.out.println("\nRelação encontrada:");
            System.out.println("Professor: " + professor.getNomeCompleto());
            System.out.println("Matéria: " + materiaProfessor.getCodigoMateria());
            System.out.println("Início período: " + materiaProfessor.getInicioPeriodo());
            System.out.println("Fim período: " + materiaProfessor.getFimPeriodo());
        } else {
            System.out.println("\nRelação não encontrada.");
        }
    }

    public void findAllMateriasProfessores() throws SQLException {
        System.out.println("\nListando todas as materias relacionadas aos professores:");
        List<MateriaProfessor> materiasProfessores = materiaProfessorDAO.findAll();

        if (materiasProfessores.isEmpty()) {
            System.out.println("\nNenhuma relação encontrada.");
            return;
        }

        for (MateriaProfessor materiaProfessor : materiasProfessores) {
            System.out.println("------------------------------");
            System.out.println("Código da materia: " + materiaProfessor.getCodigoMateria());
            System.out.println("Matrícula do professor: " + materiaProfessor.getMatriculaProfessor());
            System.out.println("Início período: " + materiaProfessor.getInicioPeriodo());
            System.out.println("Fim período: " + materiaProfessor.getFimPeriodo());
        }
    }
}
