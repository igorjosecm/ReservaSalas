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
        System.out.print("Código do bloco: ");
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
    }

    public void deleteBloco() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de bloco");
        System.out.println("\nAtenção! Ao excluir um bloco, as salas conectadas a ele também serão excluídas.");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        // A lógica do GenericDAO.delete() já usa DETACH DELETE,
        // o que removeria o bloco e qualquer sala conectada a ele.
        // CUIDADO: Isso pode causar exclusões em cascata.
        blocoDAO.delete(codigoBloco);
        System.out.println("\nBloco e salas conectadas excluídos com sucesso!");
    }

    public void findBlocoById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        Bloco bloco = blocoDAO.findById(codigoBloco);
        if (bloco != null) {
            System.out.println("\nBloco encontrado:");
            printInfoBloco(bloco);
        } else {
            System.out.println("\nBloco não encontrado.");
        }
    }

    public void findAllBlocos() {
        System.out.println("\nListando todos os blocos:");
        List<Bloco> blocos = blocoDAO.findAll();

        if (blocos.isEmpty()) {
            System.out.println("\nNenhum bloco encontrado.");
            return;
        }

        for (Bloco bloco : blocos) {
            System.out.println("------------------------------");
            printInfoBloco(bloco);
        }
    }

    private void printInfoBloco(Bloco bloco) {
        System.out.println("Código do Bloco: " + bloco.getCodigoBloco());
        System.out.println("Nome: " + bloco.getNomeBloco());
        System.out.println("Número de andares: " + bloco.getNumAndares());
    }
}
