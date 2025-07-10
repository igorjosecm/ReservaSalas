package controllers;

import classes.Bloco;
import dao.BlocoDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.List;
import java.util.Scanner;

public class BlocoController {
    private final BlocoDAO blocoDAO;

    public BlocoController(Driver driver) {
        this.blocoDAO = new BlocoDAO(driver);
    }

    public void createBloco() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Cadastro de Bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        try {
            if (blocoDAO.findById(codigoBloco) != null) {
                System.out.println("\nErro: Um bloco com este código já existe.");
                return;
            }

            System.out.print("Nome do bloco: ");
            String nomeBloco = input.nextLine();
            System.out.print("Número de andares: ");
            int numAndares = Helpers.getIntInput(input);

            Bloco newBloco = new Bloco();
            newBloco.setCodigoBloco(codigoBloco);
            newBloco.setNomeBloco(nomeBloco);
            newBloco.setNumAndares(numAndares);

            blocoDAO.create(newBloco);
            System.out.println("\nBloco criado com sucesso!");
        } catch (ClientException e) {
            System.err.println("\nErro ao criar o bloco: " + e.getMessage());
            System.err.println("Verifique se o código do bloco já está em uso.");
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro ao comunicar com o banco de dados. Verifique a conexão.");
            e.printStackTrace();
        }
    }

    public void updateBloco() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de bloco");

        try {
            findAllBlocos();

            System.out.print("\nCódigo do bloco: ");
            String codigoBloco = input.nextLine();

            Bloco bloco = blocoDAO.findById(codigoBloco);
            if (bloco != null) {
                System.out.print("Novo nome do bloco: ");
                String nomeBloco = input.nextLine();
                System.out.print("Novo número de andares: ");
                int numAndares = Helpers.getIntInput(input);

                bloco.setNomeBloco(nomeBloco);
                bloco.setNumAndares(numAndares);

                blocoDAO.update(bloco);
                System.out.println("\nBloco atualizado com sucesso!");
            } else {
                System.out.println("\nBloco não encontrado.");
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao atualizar o bloco: " + e.getMessage());
        }
    }

    public void deleteBloco() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Desativar Bloco e suas Salas");
        System.out.println("Atenção! Esta ação tornará o bloco e todas as salas dentro dele inativos.");

        try {
            findAllBlocos();

            System.out.print("\nCódigo do bloco a ser desativado: ");
            String codigoBloco = input.nextLine();

            if (blocoDAO.findById(codigoBloco) == null) {
                System.out.println("\nErro: Bloco não encontrado.");
                return;
            }

            blocoDAO.deactivateBlocoAndSalas(codigoBloco);
            System.out.println("\nBloco e todas as suas salas foram desativados com sucesso!");

        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao desativar o bloco: " + e.getMessage());
        }
    }

    public void findBlocoById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        try {
            Bloco bloco = blocoDAO.findById(codigoBloco);
            if (bloco != null) {
                System.out.println("\nBloco encontrado:");
                printInfoBloco(bloco);
            } else {
                System.out.println("\nBloco não encontrado.");
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao buscar o bloco: " + e.getMessage());
        }
    }

    public void findAllBlocos() {
        System.out.println("\nListando todos os blocos:");
        try {
            List<Bloco> blocos = blocoDAO.findAll();

            if (blocos.isEmpty()) {
                System.out.println("\nNenhum bloco encontrado.");
                return;
            }

            for (Bloco bloco : blocos) {
                System.out.println("------------------------------");
                printInfoBloco(bloco);
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao listar os blocos: " + e.getMessage());
        }
    }

    private void printInfoBloco(Bloco bloco) {
        System.out.println("Código do Bloco: " + bloco.getCodigoBloco());
        System.out.println("Nome: " + bloco.getNomeBloco());
        System.out.println("Número de andares: " + bloco.getNumAndares());
    }
}
