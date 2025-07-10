package controllers;

import classes.Materia;
import dao.MateriaDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.List;
import java.util.Scanner;

public class MateriaController {
    private final MateriaDAO materiaDAO;

    public MateriaController(Driver driver) {
        this.materiaDAO = new MateriaDAO(driver);
    }

    public void createMateria() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Cadastro de matéria");
        System.out.print("Código da matéria: ");
        String codigoMateria = input.nextLine();

        try {
            if (materiaDAO.findById(codigoMateria) != null) {
                System.out.println("\nErro: Uma matéria com este código já existe.");
                return;
            }

            System.out.print("Nome da matéria: ");
            String nomeMateria = input.nextLine();
            System.out.print("Carga horária: ");
            int cargaHoraria = Helpers.getIntInput(input);

            Materia newMateria = new Materia();
            newMateria.setCodigoMateria(codigoMateria);
            newMateria.setNomeMateria(nomeMateria);
            newMateria.setCargaHoraria(cargaHoraria);

            materiaDAO.create(newMateria);
            System.out.println("\nMatéria criada com sucesso!");

        } catch (ClientException e) {
            System.err.println("\nErro ao criar matéria: O código da matéria já está em uso. " + e.getMessage());
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao criar a matéria: " + e.getMessage());
        }
    }

    public void updateMateria() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de matéria");

        try {
            findAllMaterias();

            System.out.print("\nCódigo da matéria a ser atualizada: ");
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
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao atualizar a matéria: " + e.getMessage());
        }
    }

    public void deleteMateria() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Desativar Matéria");
        System.out.println("Atenção! Esta ação tornará a matéria inativa, preservando todo o seu histórico.");

        try {
            findAllMaterias();
            System.out.print("\nDigite o código da matéria que deseja desativar: ");
            String codigoMateria = input.nextLine();

            if (materiaDAO.findById(codigoMateria) == null) {
                System.out.println("\nErro: Matéria não encontrada ou já está inativa.");
                return;
            }

            materiaDAO.deactivateMateria(codigoMateria);

            System.out.println("\nMatéria desativada com sucesso. Ela não aparecerá mais nas listagens, mas seu histórico foi preservado.");
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao desativar a matéria: " + e.getMessage());
        }
    }

    public void findMateriaById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de matéria");
        System.out.print("Código da matéria: ");
        String codigoMateria = input.nextLine();

        try {
            Materia materia = materiaDAO.findById(codigoMateria);
            if (materia != null) {
                System.out.println("\nMatéria encontrada:");
                printInfoMateria(materia);
            } else {
                System.out.println("\nMatéria não encontrada.");
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao buscar a matéria: " + e.getMessage());
        }
    }

    public void findAllMaterias() {
        System.out.println("\nListando todas as matérias:");
        try {
            List<Materia> materias = materiaDAO.findAll();

            if (materias.isEmpty()) {
                System.out.println("\nNenhuma matéria encontrada.");
                return;
            }

            for (Materia materia : materias) {
                System.out.println("------------------------------");
                printInfoMateria(materia);
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao listar as matérias: " + e.getMessage());
        }
    }

    private void printInfoMateria(Materia materia) {
        System.out.println("Código da matéria: " + materia.getCodigoMateria());
        System.out.println("Nome: " + materia.getNomeMateria());
        System.out.println("Carga horária: " + materia.getCargaHoraria());
    }
}
