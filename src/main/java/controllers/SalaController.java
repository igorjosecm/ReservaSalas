package controllers;

import dao.SalaDAO;
import classes.Sala;
import classes.CompositeKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SalaController {
    private final SalaDAO salaDAO;

    public SalaController(Connection connection) {
        this.salaDAO = new SalaDAO(connection);
    }

    public void createSala() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Cadastro de Sala");
        System.out.print("Código da sala: ");
        String codSala = input.nextLine();
        System.out.print("Código do bloco: ");
        String codBloco = input.nextLine();
        System.out.print("Nome da sala: ");
        String nomeSala = input.nextLine();
        System.out.print("Andar: ");
        int andar = Integer.parseInt(input.nextLine());
        System.out.print("Capacidade: ");
        int capacidade = Integer.parseInt(input.nextLine());;

        Sala sala = new Sala();
        sala.setCodigoSala(codSala);
        sala.setCodigoBloco(codBloco);
        sala.setNomeSala(nomeSala);
        sala.setAndar(andar);
        sala.setCapacidade(capacidade);

        salaDAO.create(sala);
        System.out.println("\nSala criada com sucesso!");
    }

    public void updateSala() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de sala");
        System.out.print("Código da sala: ");
        String codSala = input.nextLine();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_sala", codSala);

        Sala sala = salaDAO.findById(key);
        if (sala != null) {
            System.out.print("Novo nome da sala: ");
            String nomeSala = input.nextLine();
            System.out.print("Novo andar: ");
            int andar = Integer.parseInt(input.nextLine());
            System.out.print("Nova capacidade: ");
            int capacidade = Integer.parseInt(input.nextLine());

            sala.setNomeSala(nomeSala);
            sala.setAndar(andar);
            sala.setCapacidade(capacidade);

            salaDAO.update(sala);
            System.out.println("\nSala atualizada com sucesso!");
        } else {
            System.out.println("\nSala não encontrada.");
        }
    }

    public void deleteSala() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de sala");
        System.out.print("Código da sala: ");
        String codSala = input.nextLine();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_sala", codSala);

        salaDAO.delete(key);
        System.out.println("\nSala excluída com sucesso!");
    }

    public void findSalaById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de sala");
        System.out.print("Código da sala: ");
        String codSala = input.nextLine();

        CompositeKey key = new CompositeKey();
        key.addKey("codigo_sala", codSala);

        Sala sala = salaDAO.findById(key);
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
            System.out.println("Código da Sala: " + sala.getCodigoSala());
            System.out.println("Código do Bloco: " + sala.getCodigoBloco());
            System.out.println("Nome: " + sala.getNomeSala());
            System.out.println("Andar: " + sala.getAndar());
            System.out.println("Capacidade: " + sala.getCapacidade());
        }
    }
}
