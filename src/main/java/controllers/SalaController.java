package controllers;

import classes.Bloco;
import dao.BlocoDAO;
import dao.SalaDAO;
import classes.Sala;
import helpers.Helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SalaController {
    private final SalaDAO salaDAO;
    private final BlocoDAO blocoDAO;

    public SalaController(Connection connection) {
        this.salaDAO = new SalaDAO(connection);
        this.blocoDAO = new BlocoDAO(connection);
    }

    public void createSala() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Cadastro de Sala");
        System.out.print("Código da sala: ");
        String codigoSala = input.nextLine();

        Sala sala = salaDAO.findById(codigoSala);
        if (sala == null) {
            System.out.print("Código do bloco: ");
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
                    newSala.setCodigoBloco(codigoBloco);
                    newSala.setNomeSala(nomeSala);
                    newSala.setAndar(andar);
                    newSala.setCapacidade(capacidade);

                    salaDAO.create(newSala);
                    System.out.println("\nSala criada com sucesso!");
                } else {
                    System.out.println("\nAtenção! O bloco " + codigoBloco + " possui " + bloco.getNumAndares() +
                            " andares, informe um andar válido.");
                }
            } else {
                System.out.println("\nBloco não encontrado, informe um bloco válido.");
            }
        } else {
            System.out.println("\nEssa sala já existe! Informe um código diferente.");
        }
    }

    public void updateSala() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de sala");
        System.out.print("Código da sala: ");
        String codigoSala = input.nextLine();

        Sala sala = salaDAO.findById(codigoSala);
        if (sala != null) {
            Bloco bloco = blocoDAO.findById(sala.getCodigoBloco());
            if (bloco != null) {
                System.out.print("Novo nome da sala: ");
                String nomeSala = input.nextLine();
                System.out.print("Novo andar: ");
                int andar = Helpers.getIntInput(input);

                if (andar <= bloco.getNumAndares()) {
                    System.out.print("Nova capacidade: ");
                    int capacidade = Helpers.getIntInput(input);

                    sala.setNomeSala(nomeSala);
                    sala.setAndar(andar);
                    sala.setCapacidade(capacidade);

                    salaDAO.update(sala);
                    System.out.println("\nSala atualizada com sucesso!");
                } else {
                    System.out.println("\nAtenção! O bloco " + bloco.getCodigoBloco() + " possui " + bloco.getNumAndares() +
                            " andares, informe um andar válido.");
                }
            } else {
                System.out.println("\nBloco não encontrado.");
            }
        } else {
            System.out.println("\nSala não encontrada.");
        }
    }

    public void deleteSala() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de sala");
        System.out.print("Código da sala: ");
        String codigoSala = input.nextLine();

        Sala sala = salaDAO.findById(codigoSala);
        if (sala != null) {
            salaDAO.delete(codigoSala);
            System.out.println("\nSala excluída com sucesso!");
        } else {
            System.out.println("\nSala não encontrada.");
        }
    }

    public void findSalaById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de sala");
        System.out.print("Código da sala: ");
        String codigoSala = input.nextLine();

        Sala sala = salaDAO.findById(codigoSala);
        if (sala != null) {
            System.out.println("\nSala encontrada:");
            System.out.println("Nome: " + sala.getNomeSala());
            System.out.println("Andar: " + sala.getAndar());
            System.out.println("Capacidade: " + sala.getCapacidade());
        } else {
            System.out.println("\nSala não encontrada.");
        }
    }

    public void findAllSalas() throws SQLException {
        System.out.println("\nListando todas as salas:");
        List<Sala> salas = salaDAO.findAll();

        if (salas.isEmpty()) {
            System.out.println("\nNenhuma sala encontrada.");
            return;
        }

        for (Sala sala : salas) {
            System.out.println("------------------------------");
            printInfoSala(sala);
        }
    }

    public void findSalasByBloco() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Buscar salas por bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        List<Sala> salas = salaDAO.findSalasByBloco(codigoBloco);

        if (salas.isEmpty()) {
            System.out.println("\nNenhuma sala encontrada.");
            return;
        }

        for (Sala sala : salas) {
            System.out.println("------------------------------");
            printInfoSala(sala);
        }
    }

    private void printInfoSala(Sala sala) {
        System.out.println("Código da Sala: " + sala.getCodigoSala());
        System.out.println("Código do Bloco: " + sala.getCodigoBloco());
        System.out.println("Nome: " + sala.getNomeSala());
        System.out.println("Andar: " + sala.getAndar());
        System.out.println("Capacidade: " + sala.getCapacidade());
    }
}
