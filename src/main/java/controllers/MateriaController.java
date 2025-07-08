package controllers;

import classes.Materia;
import dao.MateriaDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;

import java.util.List;
import java.util.Scanner;

public class MateriaController {
    private final MateriaDAO materiaDAO;

    // Construtor atualizado para receber o Driver do Neo4j
    public MateriaController(Driver driver) {
        this.materiaDAO = new MateriaDAO(driver);
    }

    public void createMateria() {
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

    public void updateMateria() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de matéria");
        System.out.print("Código da matéria: ");
        String codigoMateria = input.nextLine();

        Materia materia = materiaDAO.findById(codigoMateria);
        if (materia != null) {
            System.out.print("Novo nome da matéria: ");
            String nomeMateria = input.nextLine();
            System.out.print("Nova carga horária: ");
            int cargaHoraria = Helpers.getIntInput(input);

            materia.setNomeMateria(nomeMateria);
            materia.setCargaHoraria(cargaHoraria);

            materiaDAO.update(materia);
            System.out.println("\nMatéria atualizada com sucesso!");
        } else {
            System.out.println("\nMatéria não encontrada.");
        }
    }

    public void deleteMateria() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de matéria");
        System.out.print("Código da matéria: ");
        String codigoMateria = input.nextLine();

        Materia materia = materiaDAO.findById(codigoMateria);
        if (materia != null) {
            // O GenericDAO.delete() usa DETACH DELETE, que removeria a matéria
            // e qualquer relacionamento :LECIONA ou :REFERENTE_A conectado a ela.
            materiaDAO.delete(codigoMateria);
            System.out.println("\nMatéria excluída com sucesso!");
        } else {
            System.out.println("\nMatéria não encontrada.");
        }
    }

    public void findMateriaById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de matéria");
        System.out.print("Código da matéria: ");
        String codigoMateria = input.nextLine();

        Materia materia = materiaDAO.findById(codigoMateria);
        if (materia != null) {
            System.out.println("\nMatéria encontrada:");
            printInfoMateria(materia);
        } else {
            System.out.println("\nMatéria não encontrada.");
        }
    }

    public void findAllMaterias() {
        System.out.println("\nListando todas as matérias:");
        List<Materia> materias = materiaDAO.findAll();

        if (materias.isEmpty()) {
            System.out.println("\nNenhuma matéria encontrada.");
            return;
        }

        for (Materia materia : materias) {
            System.out.println("------------------------------");
            printInfoMateria(materia);
        }
    }

    private void printInfoMateria(Materia materia) {
        System.out.println("Código da matéria: " + materia.getCodigoMateria());
        System.out.println("Nome: " + materia.getNomeMateria());
        System.out.println("Carga horária: " + materia.getCargaHoraria());
    }
}
