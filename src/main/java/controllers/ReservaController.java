package controllers;

import classes.Reserva;
import classes.ReservaDetalhada;
import dao.ReservaDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;
import org.neo4j.driver.exceptions.Neo4jException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class ReservaController {
    private final ReservaDAO reservaDAO;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
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

            System.out.print("Data de início da reserva (dd/mm/aaaa): ");
            LocalDate dataInicio = Helpers.getLocalDateInput(input);
            System.out.print("Horário de início da reserva (hh:mm): ");
            LocalTime horaInicio = Helpers.getLocalTimeInput(input);
            LocalDateTime inicioReserva = LocalDateTime.of(dataInicio, horaInicio);

            System.out.print("Data de fim da reserva (dd/mm/aaaa): ");
            LocalDate dataFim = Helpers.getLocalDateInput(input);
            System.out.print("Horário de fim da reserva (hh:mm): ");
            LocalTime horaFim = Helpers.getLocalTimeInput(input);
            LocalDateTime fimReserva = LocalDateTime.of(dataFim, horaFim);

            if (fimReserva.isBefore(inicioReserva)) {
                System.out.println("\nERRO: A data/hora de fim não pode ser anterior à data/hora de início.");
                return;
            }

            // O método de verificação de conflito precisa ser adaptado para LocalDateTime
            // if (reservaDAO.existeConflitoReserva(codigoSala, inicioReserva, fimReserva)) { ... }

            Reserva reserva = new Reserva();
            reserva.setIdReserva(idCounter.getAndIncrement());
            reserva.setCodigoSala(codigoSala);
            reserva.setMatriculaProfessor(matriculaProfessor);
            reserva.setCodigoMateria(codMateria);
            reserva.setInicioReserva(inicioReserva);
            reserva.setFimReserva(fimReserva);

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
            if (reservaDAO.findById(idReserva) == null) {
                System.out.println("\nErro: Reserva com o ID informado não foi encontrada.");
                return;
            }
            reservaDAO.delete(idReserva);
            System.out.println("\nReserva excluída com sucesso!");
        } catch (Neo4jException e) {
            System.err.println("\nErro ao excluir a reserva: " + e.getMessage());
        }
    }

    public void findReservaById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca detalhada de reserva");
        System.out.print("ID da reserva: ");
        Integer idReserva = Helpers.getIntInput(input);

        try {
            ReservaDetalhada detalhes = reservaDAO.findReservaDetalhadaById(idReserva);
            if (detalhes != null) {
                System.out.println("\nReserva encontrada:");
                printInfoReservaDetalhada(detalhes);
            } else {
                System.out.println("\nReserva não encontrada.");
            }
        } catch (Neo4jException e) {
            System.err.println("\nErro ao buscar a reserva: " + e.getMessage());
        }
    }

    public void findAllReservas() {
        System.out.println("\nListando todas as reservas:");
        try {
            List<Reserva> reservas = reservaDAO.findAll();
            if (reservas.isEmpty()) {
                System.out.println("\nNenhuma reserva encontrada.");
                return;
            }
            for (Reserva reserva : reservas) {
                System.out.println("------------------------------");
                printInfoReserva(reserva);
            }
        } catch (Neo4jException e) {
            System.err.println("\nErro ao listar as reservas: " + e.getMessage());
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
                System.out.println("\nErro: Reserva não encontrada.");
                return;
            }

            System.out.println("\nDigite os novos dados da reserva. Pressione ENTER para manter o valor atual.");

            // Coleta e combina a nova data/hora de início
            System.out.print("Nova data de início [" + reserva.getInicioReserva().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "]: ");
            LocalDate novaDataInicio = Helpers.getLocalDateInput(input);
            System.out.print("Novo horário de início [" + reserva.getInicioReserva().toLocalTime() + "]: ");
            LocalTime novaHoraInicio = Helpers.getLocalTimeInput(input);

            // Coleta e combina a nova data/hora de fim
            System.out.print("Nova data de fim [" + reserva.getFimReserva().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "]: ");
            LocalDate novaDataFim = Helpers.getLocalDateInput(input);
            System.out.print("Novo horário de fim [" + reserva.getFimReserva().toLocalTime() + "]: ");
            LocalTime novaHoraFim = Helpers.getLocalTimeInput(input);

            // Atualiza os campos apenas se um novo valor foi fornecido
            LocalDateTime inicioReservaAtualizado = LocalDateTime.of(
                    novaDataInicio != null ? novaDataInicio : reserva.getInicioReserva().toLocalDate(),
                    novaHoraInicio != null ? novaHoraInicio : reserva.getInicioReserva().toLocalTime()
            );

            LocalDateTime fimReservaAtualizado = LocalDateTime.of(
                    novaDataFim != null ? novaDataFim : reserva.getFimReserva().toLocalDate(),
                    novaHoraFim != null ? novaHoraFim : reserva.getFimReserva().toLocalTime()
            );

            reserva.setInicioReserva(inicioReservaAtualizado);
            reserva.setFimReserva(fimReservaAtualizado);

            // O update no GenericDAO atualiza todas as propriedades do nó
            reservaDAO.update(reserva);
            System.out.println("\nReserva atualizada com sucesso!");

        } catch (Neo4jException e) {
            System.err.println("\nErro ao atualizar a reserva: " + e.getMessage());
        }
    }

    public void findAllReservasByBloco() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Relatório: Buscar reservas por bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        try {
            List<Reserva> reservas = reservaDAO.findAllReservasByBloco(codigoBloco);
            if (reservas.isEmpty()) {
                System.out.println("\nNenhuma reserva encontrada para o bloco " + codigoBloco);
                return;
            }
            System.out.println("\nReservas encontradas no bloco " + codigoBloco + ":");
            for (Reserva reserva : reservas) {
                System.out.println("------------------------------");
                // Para mais detalhes, seria necessário chamar findReservaDetalhadaById
                printInfoReserva(reserva);
            }
        } catch (Neo4jException e) {
            System.err.println("\nErro ao buscar reservas: " + e.getMessage());
        }
    }

    public void findReservasByPeriodo() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Relatório: Buscar reservas por período");
        try {
            System.out.print("Data de início (dd/mm/aaaa): ");
            LocalDate dataInicio = Helpers.getLocalDateInput(input);
            System.out.print("Horário de início (hh:mm): ");
            LocalTime horaInicio = Helpers.getLocalTimeInput(input);
            LocalDateTime inicioPeriodo = LocalDateTime.of(dataInicio, horaInicio);

            System.out.print("Data de fim (dd/mm/aaaa): ");
            LocalDate dataFim = Helpers.getLocalDateInput(input);
            System.out.print("Horário de fim (hh:mm): ");
            LocalTime horaFim = Helpers.getLocalTimeInput(input);
            LocalDateTime fimPeriodo = LocalDateTime.of(dataFim, horaFim);

            List<Reserva> reservas = reservaDAO.findAllReservasByPeriodo(inicioPeriodo, fimPeriodo);
            if (reservas.isEmpty()) {
                System.out.println("\nNenhuma reserva encontrada no período especificado.");
                return;
            }
            System.out.println("\nReservas encontradas:");
            for (Reserva reserva : reservas) {
                System.out.println("------------------------------");
                printInfoReserva(reserva);
            }
        } catch (Neo4jException e) {
            System.err.println("\nErro ao buscar reservas por período: " + e.getMessage());
        }
    }

    private void printInfoReserva(Reserva reserva) {
        System.out.println("ID da reserva: " + reserva.getIdReserva());
        System.out.println("Início da reserva: " + dateTimeFormatter.format(reserva.getInicioReserva()));
        System.out.println("Fim da reserva: " + dateTimeFormatter.format(reserva.getFimReserva()));
    }

    private void printInfoReservaDetalhada(ReservaDetalhada detalhes) {
        System.out.println("ID da reserva: " + detalhes.getIdReserva());
        System.out.println("Sala: " + (detalhes.getNomeSala() != null ? detalhes.getNomeSala() : "Não especificada"));
        System.out.println("Professor: " + (detalhes.getNomeProfessor() != null ? detalhes.getNomeProfessor() : "Não especificado"));
        System.out.println("Matéria: " + (detalhes.getNomeMateria() != null ? detalhes.getNomeMateria() : "Não especificada"));
        System.out.println("Início da reserva: " + dateTimeFormatter.format(detalhes.getInicioReserva()));
        System.out.println("Fim da reserva: " + dateTimeFormatter.format(detalhes.getFimReserva()));
    }
}
