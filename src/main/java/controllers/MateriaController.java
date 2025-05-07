package controllers;

import classes.CompositeKey;
import classes.Materia;
import dao.MateriaDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MateriaController {
    private final MateriaDAO materiaDAO;

    public MateriaController(Connection connection) {
        this.materiaDAO = new MateriaDAO(connection);
    }

    public void createMateria() throws SQLException {
        Scanner input = new Scanner(System.in);
        findAllMaterias();
        System.out.println("\n- Cadastro de matéria");
        System.out.print("Código da matéria: ");
        String codMateria = input.nextLine();
        System.out.print("Nome da matéria: ");
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
        findAllMaterias();
        System.out.println("\n- Atualização de matéria");
        System.out.print("Código da matéria: ");
        String codMateria = input.nextLine();
        System.out.print("Novo nome da matéria: ");
        String nomeMateria = input.nextLine();
        System.out.print("Novo carga horária: ");
        int cargaHoraria = Integer.parseInt(input.nextLine());

        Materia materia = new Materia();
        materia.setCodigoMateria(codMateria);
        materia.setNomeMateria(nomeMateria);
        materia.setCargaHoraria(cargaHoraria);

        materiaDAO.update(materia);
        System.out.println("\nMatéria atualizada com sucesso!");
    }

    public void deleteMateria() throws SQLException {
        Scanner input = new Scanner(System.in);
        findAllMaterias();
        System.out.println("\n- Exclusão de matéria");
        System.out.print("Código da materia: ");
        String codMateria = input.nextLine();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_materia", codMateria);

        materiaDAO.delete(key);
        System.out.println("\nMatéria excluída com sucesso!");
    }

    public void findMateriaById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de matéria");
        System.out.print("Código da matéria: ");
        String codMateria = input.nextLine();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_materia", codMateria);

        Materia materia = materiaDAO.findById(key);
        if (materia != null) {
            System.out.println("\nMateria encontrada:");
            System.out.println("Nome: " + materia.getNomeMateria());
            System.out.println("Carga horária: " + materia.getCargaHoraria());
        } else {
            System.out.println("\nMatéria não encontrada.");
        }
    }

    public void findAllMaterias() throws SQLException {
        System.out.println("\nListando todas as matérias:");
        List<Materia> materias = materiaDAO.findAll();

        if (materias.isEmpty()) {
            System.out.println("\nNenhuma matéria encontrada.");
            return;
        }

        for (Materia materia : materias) {
            System.out.println("------------------------------");
            System.out.println("Código da matéria: " + materia.getCodigoMateria());
            System.out.println("Nome: " + materia.getNomeMateria());
            System.out.println("Carga horária: " + materia.getCargaHoraria());
        }
    }
}
