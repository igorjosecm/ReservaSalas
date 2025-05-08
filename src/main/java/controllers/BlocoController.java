package controllers;

import classes.Bloco;
import dao.BlocoDAO;
import helpers.Helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BlocoController {
    private final BlocoDAO blocoDAO;

    public BlocoController(Connection connection) {
        this.blocoDAO = new BlocoDAO(connection);
    }

    public void createBloco() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Cadastro de Bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        Bloco bloco = blocoDAO.findById(codigoBloco);
        if (bloco == null) {
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
        } else {
            System.out.println("\nEsse bloco já existe! Informe um código diferente.");
        }
    }

    public void updateBloco() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        Bloco bloco = blocoDAO.findById(codigoBloco);
        if (bloco != null) {
            System.out.print("Nome do bloco: ");
            String nomeBloco = input.nextLine();
            System.out.print("Número de andares: ");
            int numAndares = Helpers.getIntInput(input);

            bloco.setNomeBloco(nomeBloco);
            bloco.setNumAndares(numAndares);

            blocoDAO.update(bloco);
            System.out.println("\nBloco atualizado com sucesso!");
        } else {
            System.out.println("\nBloco não encontrado.");
        }
    }

    public void deleteBloco() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        Bloco bloco = blocoDAO.findById(codigoBloco);
        if (bloco != null) {
            blocoDAO.delete(codigoBloco);
            System.out.println("\nBloco excluído com sucesso!");
        } else {
            System.out.println("\nBloco não encontrado.");
        }
    }

    public void findBlocoById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        Bloco bloco = blocoDAO.findById(codigoBloco);
        if (bloco != null) {
            System.out.println("\nBloco encontrado:");
            System.out.println("Nome: " + bloco.getNomeBloco());
            System.out.println("Número de andares: " + bloco.getNumAndares());
        } else {
            System.out.println("\nBloco não encontrado.");
        }
    }

    public void findAllBlocos() throws SQLException {
        System.out.println("\nListando todas os blocos:");
        List<Bloco> blocos = blocoDAO.findAll();

        if (blocos.isEmpty()) {
            System.out.println("\nNenhum bloco encontrado.");
            return;
        }

        for (Bloco bloco : blocos) {
            System.out.println("------------------------------");
            System.out.println("Código do Bloco: " + bloco.getCodigoBloco());
            System.out.println("Nome: " + bloco.getNomeBloco());
            System.out.println("Número de andares: " + bloco.getNumAndares());
        }
    }
}
