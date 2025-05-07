package controllers;

import classes.Bloco;
import classes.CompositeKey;
import dao.BlocoDAO;

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
        String codBloco = input.nextLine();
        System.out.print("Nome do bloco: ");
        String nomeBloco = input.nextLine();
        System.out.print("Número de andares: ");
        int numAndares = Integer.parseInt(input.nextLine());

        Bloco bloco = new Bloco();
        bloco.setCodigoBloco(codBloco);
        bloco.setNomeBloco(nomeBloco);
        bloco.setNumAndares(numAndares);

        blocoDAO.create(bloco);
        System.out.println("\nBloco criado com sucesso!");
    }

    public void updateBloco() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de bloco");
        System.out.print("Código do bloco: ");
        String codBloco = input.nextLine();
        System.out.print("Nome do bloco: ");
        String nomeBloco = input.nextLine();
        System.out.print("Número de andares: ");
        int numAndares = Integer.parseInt(input.nextLine());

        Bloco bloco = new Bloco();
        bloco.setCodigoBloco(codBloco);
        bloco.setNomeBloco(nomeBloco);
        bloco.setNumAndares(numAndares);

        blocoDAO.update(bloco);
        System.out.println("\nBloco atualizado com sucesso!");
    }

    public void deleteBloco() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Remoção de bloco");
        System.out.print("Código do bloco: ");
        String codBloco = input.next();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_bloco", codBloco);

        blocoDAO.delete(key);
        System.out.println("\nBloco removido com sucesso!");
    }

    public void findBlocoById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de bloco por ID");
        System.out.print("Código do bloco: ");
        String codBloco = input.next();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_bloco", codBloco);

        Bloco bloco = blocoDAO.findById(key);
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
            System.out.println("\n----------------------------------");
            System.out.println("Código do Bloco: " + bloco.getCodigoBloco());
            System.out.println("Nome: " + bloco.getNomeBloco());
            System.out.println("Número de andares: " + bloco.getNumAndares());
        }
    }
}
