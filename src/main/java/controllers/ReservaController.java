package controllers;

import classes.Reserva;
import classes.ReservaDetalhada;
import dao.ReservaDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;
import org.neo4j.driver.exceptions.Neo4jException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class ReservaController {
    private final ReservaDAO reservaDAO;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Simples contador em memória para gerar IDs únicos para a reserva.
    // Em um sistema real, isso poderia ser um nó contador no Neo4j.
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public ReservaController(Driver driver) {
        this.reservaDAO = new ReservaDAO(driver);
    }

    public void createReserva() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Nova Reserva de Sala");

        try {
            System.out.print("Código da sala: ");
            String codigoSala = input.nextLine();
            System.out.print("Matrícula do professor: ");
            Integer matriculaProfessor = Helpers.getIntInput(input);
            System.out.print("Código da matéria: ");
            String codMateria = input.nextLine();

            LocalDate dataInicio;
            LocalDate dataFim;
            while (true) {
                try {
                    System.out.print("Data de início da reserva (dd/mm/aaaa): ");
                    dataInicio = LocalDate.parse(input.nextLine(), formatter);
                    System.out.print("Data de fim da reserva (dd/mm/aaaa): ");
                    dataFim = LocalDate.parse(input.nextLine(), formatter);
                    if (dataInicio.isAfter(dataFim)) {
                        System.out.println("A data inicial não pode ser após a data final. Tente novamente.");
                    } else {
                        break;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
                }
            }

            System.out.print("Horário de início da reserva (hh:mm): ");
            LocalTime horaInicio = Helpers.getLocalTimeInput(input);
            System.out.print("Horário de fim da reserva (hh:mm): ");
            LocalTime horaFim = Helpers.getLocalTimeInput(input);

            if (reservaDAO.existeConflitoReserva(codigoSala, dataInicio, dataFim, horaInicio, horaFim)) {
                System.out.println("\nERRO: Já existe uma reserva conflitante neste período para a sala informada.");
                return;
            }

            Reserva reserva = new Reserva();
            reserva.setIdReserva(idCounter.getAndIncrement()); // Gera um novo ID
            reserva.setCodigoSala(codigoSala);
            reserva.setMatriculaProfessor(matriculaProfessor);
            reserva.setCodigoMateria(codMateria);
            reserva.setDataInicio(dataInicio);
            reserva.setDataFim(dataFim);
            reserva.setHoraInicio(horaInicio);
            reserva.setHoraFim(horaFim);

            reservaDAO.create(reserva);
            System.out.println("\nReserva registrada com sucesso!");

        } catch (Neo4jException e) {
            System.err.println("\nErro ao registrar a reserva: " + e.getMessage());
            System.err.println("Verifique se os códigos de sala, professor e matéria são válidos.");
        }
    }

    public void deleteReserva() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de reserva");
        System.out.print("ID da reserva a ser excluída: ");
        Integer idReserva = Helpers.getIntInput(input);

        try {
            reservaDAO.delete(idReserva);
            System.out.println("\nReserva excluída com sucesso!");
        } catch (Neo4jException e) {
            System.err.println("\nErro ao excluir a reserva: " + e.getMessage());
        }
    }

    public void findAllReservasByBloco() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Buscar reservas por bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        try {
            List<Reserva> reservas = reservaDAO.findAllReservasByBloco(codigoBloco);
            if (reservas.isEmpty()) {
                System.out.println("\nNenhuma reserva encontrada para o bloco " + codigoBloco);
                return;
            }
            System.out.println("\nReservas encontradas:");
            for (Reserva reserva : reservas) {
                System.out.println("------------------------------");
                printInfoReserva(reserva);
            }
        } catch (Neo4jException e) {
            System.err.println("\nErro ao buscar reservas: " + e.getMessage());
        }
    }

    public void updateReserva() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de reserva de sala");
        System.out.print("ID da reserva a ser atualizada: ");
        Integer idReserva = Helpers.getIntInput(input);

        try {
            Reserva reserva = reservaDAO.findById(idReserva);
            if (reserva == null) {
                System.out.println("Reserva não encontrada.");
                return;
            }

            System.out.println("Digite os novos dados da reserva (deixe em branco para não alterar):");
            // A lógica de update pode ser complexa. Aqui, atualizamos apenas as datas/horas.
            // Atualizar os relacionamentos (mudar sala, professor) exigiria lógica adicional.

            System.out.print("Nova data de início (dd/mm/aaaa): ");
            LocalDate novaDataInicio = Helpers.getLocalDateInput(input); // Adaptar getLocalDateInput para aceitar nulo
            System.out.print("Nova data de fim (dd/mm/aaaa): ");
            LocalDate novaDataFim = Helpers.getLocalDateInput(input);
            System.out.print("Novo horário de início (hh:mm): ");
            LocalTime novaHoraInicio = Helpers.getLocalTimeInput(input); // Adaptar getLocalTimeInput para aceitar nulo
            System.out.print("Novo horário de fim (hh:mm): ");
            LocalTime novaHoraFim = Helpers.getLocalTimeInput(input);

            reserva.setDataInicio(novaDataInicio != null ? novaDataInicio : reserva.getDataInicio());
            reserva.setDataFim(novaDataFim != null ? novaDataFim : reserva.getDataFim());
            reserva.setHoraInicio(novaHoraInicio != null ? novaHoraInicio : reserva.getHoraInicio());
            reserva.setHoraFim(novaHoraFim != null ? novaHoraFim : reserva.getHoraFim());

            reservaDAO.update(reserva);
            System.out.println("\nReserva atualizada com sucesso!");

        } catch (Neo4jException e) {
            System.err.println("\nErro ao atualizar a reserva: " + e.getMessage());
        }
    }

    public void findReservaById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de reserva");
        System.out.print("ID da reserva: ");
        Integer idReserva = Helpers.getIntInput(input);

        try {
            Reserva reserva = reservaDAO.findById(idReserva);
            if (reserva != null) {
                System.out.println("\nReserva encontrada:");
                printInfoReserva(reserva);
            } else {
                System.out.println("\nReserva não encontrada.");
            }
        } catch (Neo4jException e) {
            System.err.println("\nErro ao buscar a reserva: " + e.getMessage());
        }
    }

    public void findAllReservas() {
        System.out.println("\nListando todas as reservas (simplificado):");
        try {
            List<Reserva> reservas = reservaDAO.findAll();
            if (reservas.isEmpty()) {
                System.out.println("\nNenhuma reserva encontrada.");
                return;
            }
            for (Reserva reserva : reservas) {
                System.out.println("------------------------------");
                // Para detalhes completos, seria necessário iterar e chamar findReservaDetalhadaById para cada um
                System.out.println("ID da Reserva: " + reserva.getIdReserva());
                System.out.println("Período: " + formatter.format(reserva.getDataInicio()) + " a " + formatter.format(reserva.getDataFim()));
            }
        } catch (Neo4jException e) {
            System.err.println("\nErro ao listar as reservas: " + e.getMessage());
        }
    }

    public void findReservasByPeriodo() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Buscar reservas por período");
        try {
            System.out.print("Data de início (dd/mm/aaaa): ");
            LocalDate dataInicio = Helpers.getLocalDateInput(input);
            System.out.print("Data de fim (dd/mm/aaaa): ");
            LocalDate dataFim = Helpers.getLocalDateInput(input);
            System.out.print("Horário de início (hh:mm): ");
            LocalTime horaInicio = Helpers.getLocalTimeInput(input);
            System.out.print("Horário de fim (hh:mm): ");
            LocalTime horaFim = Helpers.getLocalTimeInput(input);

            List<Reserva> reservas = reservaDAO.findAllReservasByPeriodo(dataInicio, dataFim, horaInicio, horaFim);
            if (reservas.isEmpty()) {
                System.out.println("\nNenhuma reserva encontrada no período especificado.");
                return;
            }
            System.out.println("\nReservas encontradas:");
            for (Reserva reserva : reservas) {
                System.out.println("------------------------------");
                System.out.println("ID da Reserva: " + reserva.getIdReserva());
            }
        } catch (Neo4jException e) {
            System.err.println("\nErro ao buscar reservas por período: " + e.getMessage());
        }
    }

    private void printInfoReserva(Reserva reserva) {
        System.out.println("ID da reserva: " + reserva.getIdReserva());
        System.out.println("Data de início: " + formatter.format(reserva.getDataInicio()));
        System.out.println("Data de fim: " + formatter.format(reserva.getDataFim()));
        System.out.println("Horário de início: " + timeFormatter.format(reserva.getHoraInicio()));
        System.out.println("Horário de fim: " + timeFormatter.format(reserva.getHoraFim()));
    }

    private void printInfoReservaDetalhada(ReservaDetalhada detalhes) {
        System.out.println("ID da reserva: " + detalhes.getIdReserva());
        System.out.println("Sala: " + (detalhes.getNomeSala() != null ? detalhes.getNomeSala() : "Não especificada"));
        System.out.println("Professor: " + (detalhes.getNomeProfessor() != null ? detalhes.getNomeProfessor() : "Não especificado"));
        System.out.println("Matéria: " + (detalhes.getNomeMateria() != null ? detalhes.getNomeMateria() : "Não especificada"));
        System.out.println("Data de início: " + formatter.format(detalhes.getDataInicio()));
        System.out.println("Data de fim: " + formatter.format(detalhes.getDataFim()));
        System.out.println("Horário de início: " + timeFormatter.format(detalhes.getHoraInicio()));
        System.out.println("Horário de fim: " + timeFormatter.format(detalhes.getHoraFim()));
    }
}
