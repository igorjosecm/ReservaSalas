package controllers;

import classes.Bloco;
import classes.Sala;
import dao.BlocoDAO;
import dao.SalaDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.List;
import java.util.Scanner;

public class SalaController {
    private final SalaDAO salaDAO;
    private final BlocoDAO blocoDAO;

    public SalaController(Driver driver) {
        this.salaDAO = new SalaDAO(driver);
        this.blocoDAO = new BlocoDAO(driver);
    }

    public void createSala() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Cadastro de Sala");
        System.out.print("Código da sala: ");
        String codigoSala = input.nextLine();

        try {
            if (salaDAO.findById(codigoSala) != null) {
                System.out.println("\nErro: Uma sala com este código já existe.");
                return;
            }

            System.out.print("Código do bloco onde a sala se localiza: ");
            String codigoBloco = input.nextLine();

            Bloco bloco = blocoDAO.findById(codigoBloco);
            if (bloco != null) {
                System.out.print("Nome da sala: ");
                String nomeSala = input.nextLine();
                System.out.print("Andar: ");
                int andar = Helpers.getIntInput(input);

                if (andar <= bloco.getNumAndares()) {
                    System.out.print("Capacidade: ");
                    int capacidade = Helpers.getIntInput(input);

                    Sala newSala = new Sala();
                    newSala.setCodigoSala(codigoSala);
                    newSala.setNomeSala(nomeSala);
                    newSala.setAndar(andar);
                    newSala.setCapacidade(capacidade);

                    salaDAO.createAndLinkToBloco(newSala, codigoBloco);
                    System.out.println("\nSala criada com sucesso!");
                } else {
                    System.out.println("\nErro de validação! O bloco " + codigoBloco + " possui " + bloco.getNumAndares() +
                            " andares. Por favor, informe um andar válido.");
                }
            } else {
                System.out.println("\nErro: Bloco não encontrado. Impossível criar a sala.");
            }
        } catch (ClientException e) {
            System.err.println("\nErro ao criar sala: Verifique se o código do bloco é válido. " + e.getMessage());
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao criar a sala: " + e.getMessage());
        }
    }

    public void updateSala() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de sala");

        try {
            findAllSalas();

            System.out.print("\nCódigo da sala para atualizar: ");
            String codigoSala = input.nextLine();

            Sala sala = salaDAO.findById(codigoSala);
            if (sala != null) {
                Bloco blocoDaSala = blocoDAO.findBlocoBySala(codigoSala);
                if (blocoDaSala == null) {
                    System.err.println("Erro crítico: A sala existe mas não está conectada a um bloco.");
                    return;
                }

                System.out.print("Novo nome da sala: ");
                String nomeSala = input.nextLine();
                System.out.print("Novo andar: ");
                int andar = Helpers.getIntInput(input);
                System.out.print("Nova capacidade: ");
                int capacidade = Helpers.getIntInput(input);

                if (andar > blocoDaSala.getNumAndares()) {
                    System.out.println("\nErro de validação! O bloco desta sala possui " + blocoDaSala.getNumAndares() +
                            " andares. Por favor, informe um andar válido.");
                    return;
                }

                sala.setNomeSala(nomeSala);
                sala.setAndar(andar);
                sala.setCapacidade(capacidade);

                salaDAO.update(sala);
                System.out.println("\nSala atualizada com sucesso!");
            } else {
                System.out.println("\nSala não encontrada.");
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao atualizar a sala: " + e.getMessage());
        }
    }

    public void deleteSala() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Desativar Sala");

        try {
            findAllSalas();

            System.out.print("\nCódigo da sala a ser desativada: ");
            String codigoSala = input.nextLine();

            if (salaDAO.findById(codigoSala) == null) {
                System.out.println("\nErro: Sala não encontrada.");
                return;
            }
            salaDAO.deactivateSala(codigoSala);
            System.out.println("\nSala desativada com sucesso! Seu histórico de reservas foi mantido.");
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao desativar a sala: " + e.getMessage());
        }
    }

    public void findSalaById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de sala");
        System.out.print("Código da sala: ");
        String codigoSala = input.nextLine();

        try {
            Sala sala = salaDAO.findById(codigoSala);
            if (sala != null) {
                System.out.println("\nSala encontrada:");
                printInfoSala(sala);
            } else {
                System.out.println("\nSala não encontrada.");
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao buscar a sala: " + e.getMessage());
        }
    }

    public void findAllSalas() {
        System.out.println("\nListando todas as salas:");
        try {
            List<Sala> salas = salaDAO.findAll();
            if (salas.isEmpty()) {
                System.out.println("\nNenhuma sala encontrada.");
                return;
            }
            for (Sala sala : salas) {
                System.out.println("------------------------------");
                printInfoSala(sala);
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao listar as salas: " + e.getMessage());
        }
    }

    public void findSalasByBloco() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Relatório: Buscar salas por bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        try {
            List<Sala> salas = salaDAO.findSalasByBloco(codigoBloco);
            if (salas.isEmpty()) {
                System.out.println("\nNenhuma sala encontrada para o bloco " + codigoBloco);
                return;
            }
            System.out.println("\nSalas encontradas no bloco " + codigoBloco + ":");
            for (Sala sala : salas) {
                System.out.println("------------------------------");
                printInfoSala(sala);
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao buscar as salas: " + e.getMessage());
        }
    }

    private void printInfoSala(Sala sala) {
        System.out.println("Código da Sala: " + sala.getCodigoSala());
        System.out.println("Nome: " + sala.getNomeSala());
        System.out.println("Andar: " + sala.getAndar());
        System.out.println("Capacidade: " + sala.getCapacidade());
    }
}
