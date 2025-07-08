package controllers;

import helpers.Helpers;
import classes.Materia;
import dao.MateriaDAO;
import org.neo4j.driver.Driver;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MateriaController {
    private final MateriaDAO materiaDAO;

    public MateriaController(Driver driver) {
        this.materiaDAO = new MateriaDAO(driver);
    }

    public void createMateria() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Cadastro de matéria");
        System.out.print("Código da matéria: ");
        String codigoMateria = input.nextLine();

        Materia materia = materiaDAO.findById(codigoMateria);
        if (materia == null) {
            System.out.print("Nome da matéria: ");
            String nomeMateria = input.nextLine();
            System.out.print("Carga horária: ");
            int cargaHoraria = Helpers.getIntInput(input);

            Materia newMateria = new Materia();
            newMateria.setCodigoMateria(codigoMateria);
            newMateria.setNomeMateria(nomeMateria);
            newMateria.setCargaHoraria(cargaHoraria);

            materiaDAO.create(newMateria);
            System.out.println("\nMateria criada com sucesso!");
        } else {
            System.out.println("\nEssa matéria já existe! Informe um código diferente.");
        }
    }

    public void updateMateria() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de matéria");
        System.out.print("Código da matéria: ");
        String codigoMateria = input.nextLine();

        Materia materia = materiaDAO.findById(codigoMateria);
        if (materia != null) {
            System.out.print("Novo nome da matéria: ");
            String nomeMateria = input.nextLine();
            System.out.print("Novo carga horária: ");
            int cargaHoraria = Helpers.getIntInput(input);

            materia.setNomeMateria(nomeMateria);
            materia.setCargaHoraria(cargaHoraria);

            materiaDAO.update(materia);
            System.out.println("\nMatéria atualizada com sucesso!");
        } else {
            System.out.println("\nMatéria não encontrada.");
        }
    }

    public void deleteMateria() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de matéria");
        System.out.print("Código da materia: ");
        String codigoMateria = input.nextLine();

        Materia materia = materiaDAO.findById(codigoMateria);
        if (materia != null) {
            materiaDAO.delete(codigoMateria);
            System.out.println("\nMatéria excluída com sucesso!");
        } else {
            System.out.println("\nMatéria não encontrada.");
        }
    }

    public void findMateriaById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de matéria");
        System.out.print("Código da matéria: ");
        String codigoMateria = input.nextLine();

        Materia materia = materiaDAO.findById(codigoMateria);
        if (materia != null) {
            System.out.println("\nMatéria encontrada:");
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
