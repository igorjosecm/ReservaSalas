package controllers;

import classes.MateriaProfessor;
import dao.MateriaProfessorDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MateriaProfessorController {
    private final MateriaProfessorDAO materiaProfessorDAO;

    public MateriaProfessorController(Connection connection) {
        this.materiaProfessorDAO = new MateriaProfessorDAO(connection);
    }

    public void createMateriaProfessor(int matriculaProfessor) throws SQLException {
        Scanner input = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("\n- Relacionar matéria a professor: ");
        System.out.print("Código da materia: ");
        String codMateria = input.nextLine();

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

        MateriaProfessor materiaProfessor = new MateriaProfessor();
        materiaProfessor.setCodigoMateria(codMateria);
        materiaProfessor.setMatriculaProfessor(matriculaProfessor);
        materiaProfessor.setInicioPeriodo(inicioPeriodo);
        materiaProfessor.setFimPeriodo(fimPeriodo);

        materiaProfessorDAO.create(materiaProfessor);
        System.out.println("\nMatéria adicionada com sucesso!");
    }

    /*
    public void deleteMateria() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Remoção de materia");
        System.out.print("Código da materia: ");
        String codMateria = input.next();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_materia", codMateria);

        materiaProfessorDAO.delete(key);
        System.out.println("\nMateria removida com sucesso!");
    }

    public void findMateriaById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de materia por ID");
        System.out.print("Código da materia: ");
        String codMateria = input.next();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_materia", codMateria);

        Materia materia = materiaDAO.findById(key);
        if (materia != null) {
            System.out.println("\nMateria encontrada:");
            System.out.println("Nome: " + materia.getNomeMateria());
            System.out.println("Carga horária: " + materia.getCargaHoraria());
        } else {
            System.out.println("\nMateria não encontrada.");
        }
    }

    public void findAllMaterias() throws SQLException {
        System.out.println("\nListando todas as materias:");
        List<Materia> materias = materiaDAO.findAll();

        if (materias.isEmpty()) {
            System.out.println("\nNenhuma materia encontrada.");
            return;
        }

        for (Materia materia : materias) {
            System.out.println("------------------------------");
            System.out.println("Código da materia: " + materia.getCodigoMateria());
            System.out.println("Nome: " + materia.getNomeMateria());
            System.out.println("Carga horária: " + materia.getCargaHoraria());
        }
    }
    */
}
