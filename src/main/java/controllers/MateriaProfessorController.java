package controllers;

import classes.CompositeKey;
import classes.Materia;
import dao.MateriaProfessorDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MateriaProfessorController {
    private final MateriaProfessorDAO materiaProfessorDAO;

    public MateriaProfessorController(Connection connection) {
        this.materiaProfessorDAO = new MateriaProfessorDAO(connection);
    }

    public void createMateria() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Cadastro de Materia");
        System.out.print("Código da materia: ");
        String codMateria = input.nextLine();
        System.out.print("Nome da materia: ");
        String nomeMateria = input.nextLine();
        System.out.print("Carga horária: ");
        int cargaHoraria = Integer.parseInt(input.nextLine());

        Materia materia = new Materia();
        materia.setCodigoMateria(codMateria);
        materia.setNomeMateria(nomeMateria);
        materia.setCargaHoraria(cargaHoraria);

        materiaDAO.create(materia);
        System.out.println("\nMateria criada com sucesso!");
    }

    public void updateMateria() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de materia");
        System.out.print("Código da materia: ");
        String codMateria = input.nextLine();
        System.out.print("Novo nome da materia: ");
        String nomeMateria = input.nextLine();
        System.out.print("Novo carga horaria: ");
        int cargaHoraria = Integer.parseInt(input.nextLine());

        Materia materia = new Materia();
        materia.setCodigoMateria(codMateria);
        materia.setNomeMateria(nomeMateria);
        materia.setCargaHoraria(cargaHoraria);

        materiaDAO.update(materia);
        System.out.println("\nMateria atualizada com sucesso!");
    }

    public void deleteMateria() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Remoção de materia");
        System.out.print("Código da materia: ");
        String codMateria = input.next();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_materia", codMateria);

        materiaDAO.delete(key);
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
            System.out.println("\n----------------------------------");
            System.out.println("Código da materia: " + materia.getCodigoMateria());
            System.out.println("Nome: " + materia.getNomeMateria());
            System.out.println("Carga horária: " + materia.getCargaHoraria());
        }
    }
}
