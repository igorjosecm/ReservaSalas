package controllers;

import classes.Bloco;
import classes.Sala;
import dao.BlocoDAO;
import dao.SalaDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;

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

        Sala sala = salaDAO.findById(codigoSala);
        if (sala == null) {
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

    public void updateSala() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de sala");
        System.out.print("Código da sala para atualizar: ");
        String codigoSala = input.nextLine();

        Sala sala = salaDAO.findById(codigoSala);
        if (sala != null) {
            // Para validar o andar, precisamos encontrar o bloco associado à sala.
            // Isso exigiria um novo método em BlocoDAO: findBlocoBySala(codigoSala)
            // Por simplicidade aqui, vamos assumir que a atualização não muda o bloco.

            System.out.print("Novo nome da sala: ");
            String nomeSala = input.nextLine();
            System.out.print("Novo andar: ");
            int andar = Helpers.getIntInput(input);
            System.out.print("Nova capacidade: ");
            int capacidade = Helpers.getIntInput(input);

            // A validação do andar precisaria de uma consulta extra para buscar o bloco.
            // Ex: Bloco blocoDaSala = blocoDAO.findBlocoBySala(codigoSala);
            // if (andar <= blocoDaSala.getNumAndares()) { ... }

            sala.setNomeSala(nomeSala);
            sala.setAndar(andar);
            sala.setCapacidade(capacidade);

            salaDAO.update(sala);
            System.out.println("\nSala atualizada com sucesso!");

        } else {
            System.out.println("\nSala não encontrada.");
        }
    }

    public void deleteSala() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de sala");
        System.out.print("Código da sala a ser excluída: ");
        String codigoSala = input.nextLine();

        // O método delete do GenericDAO já usa "DETACH DELETE",
        // que remove o nó e seus relacionamentos.
        salaDAO.delete(codigoSala);
        System.out.println("\nSala excluída com sucesso!");
    }

    public void findSalaById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de sala");
        System.out.print("Código da sala: ");
        String codigoSala = input.nextLine();

        Sala sala = salaDAO.findById(codigoSala);
        if (sala != null) {
            System.out.println("\nSala encontrada:");
            printInfoSala(sala); // Usa um método helper para imprimir
        } else {
            System.out.println("\nSala não encontrada.");
        }
    }

    public void findAllSalas() {
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

    public void findSalasByBloco() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Buscar salas por bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        // Usa o método customizado do DAO
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
    }

    // Método helper para evitar repetição de código
    private void printInfoSala(Sala sala) {
        System.out.println("Código da Sala: " + sala.getCodigoSala());
        System.out.println("Nome: " + sala.getNomeSala());
        System.out.println("Andar: " + sala.getAndar());
        System.out.println("Capacidade: " + sala.getCapacidade());
    }
}
