package controllers;

import classes.CompositeKey;
import classes.Reserva;
import classes.Sala;
import dao.ReservaDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;


public class ReservaController {
    private final ReservaDAO reservaDAO;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReservaController(Connection connection) {
        this.reservaDAO = new ReservaDAO(connection);
    }

    public void createReserva() throws SQLException {


        Scanner input = new Scanner(System.in);
        findAllReservas();
        System.out.println("\n- Reserva de sala");
        System.out.println("Código da sala: ");
        String codigoSala = input.next();
        System.out.print("Data início da reserva: ");
        LocalDate dataInicio = LocalDate.parse(input.nextLine());
        System.out.print("Data fim da reserva: ");
        LocalDate dataFim= LocalDate.parse(input.nextLine());
        System.out.print("Horário início da reserva: ");
        LocalTime horaInicio = LocalTime.parse(input.nextLine(), formatter);
        System.out.print("Horário fim da reserva: ");
        LocalTime horaFim = LocalTime.parse(input.nextLine(), formatter);
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = input.nextInt();
        System.out.println("Código da materia: ");
        String codMateria = input.nextLine();

        boolean conflito = reservaDAO.existeConflitoReserva(
                codigoSala,
                dataInicio,
                dataFim,
                horaInicio,
                horaFim
        );

        if (conflito) {
            System.out.println("Já existe uma reserva nesse período.");
            return;
        }

        Reserva reserva = new Reserva();
        reserva.setCodigoSala(codigoSala);
        reserva.setMatriculaProfessor(matriculaProfessor);
        reserva.setCodigoMateria(codMateria);
        reserva.setDataInicio(dataInicio);
        reserva.setDataFim(dataFim);
        reserva.setHoraInicio(horaInicio);
        reserva.setHoraFim(horaFim);


        reservaDAO.create(reserva);
        System.out.println("\nReserva registrada com sucesso!");
    }

    public void updateReserva() throws SQLException {
        Scanner input = new Scanner(System.in);
        findAllReservas();
        System.out.println("\n- Atualização de reserva de sala");
        System.out.print("Código da sala: ");
        String codigoSala = input.next();
        System.out.print("Data início da reserva: ");
        LocalDate dataInicio = LocalDate.parse(input.nextLine());
        System.out.print("Data fim da reserva: ");
        LocalDate dataFim= LocalDate.parse(input.nextLine());
        System.out.print("Horário início da reserva: ");
        LocalTime horaInicio = LocalTime.parse(input.nextLine(), formatter);
        System.out.print("Horário fim da reserva: ");
        LocalTime horaFim = LocalTime.parse(input.nextLine(), formatter);
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = input.nextInt();
        System.out.println("Código da materia: ");
        String codMateria = input.nextLine();


        boolean conflito = reservaDAO.existeConflitoReserva(
                codigoSala,
                dataInicio,
                dataFim,
                horaInicio,
                horaFim
        );

        if (conflito) {
            System.out.println("Já existe uma reserva nesse período.");
            return;
        }

        Reserva reserva = new Reserva();
        reserva.setCodigoSala(codigoSala);
        reserva.setMatriculaProfessor(matriculaProfessor);
        reserva.setCodigoMateria(codMateria);
        reserva.setDataInicio(dataInicio);
        reserva.setDataFim(dataFim);
        reserva.setHoraInicio(horaInicio);
        reserva.setHoraFim(horaFim);

        reservaDAO.update(reserva);
        System.out.println("\nReserva atualizada com sucesso!");
    }

    public void deleteReserva() throws SQLException {
        Scanner input = new Scanner(System.in);
        findAllReservas();
        System.out.println("\n- Exclusão de reserva");
        System.out.print("ID da reserva: ");
        Integer idReserva = input.nextInt();

        CompositeKey key = new CompositeKey();
        key.addKey("id_reserva", idReserva);

        reservaDAO.delete(key);
        System.out.println("\nReserva excluída com sucesso!");
    }

    public void findReservaById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de reserva");
        System.out.print("ID da reserva: ");
        Integer idReserva = input.nextInt();

        CompositeKey key = new CompositeKey();
        key.addKey("id_reserva", idReserva);

        Reserva reserva = reservaDAO.findById(key);
        if (reserva != null) {
            System.out.println("\nReserva encontrada:");
            printInfoReserva(reserva);
        } else {
            System.out.println("\nReserva não encontrada.");
        }
    }

    public void findReservasByBloco() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Buscar reservas por bloco");
        System.out.print("Código do bloco: ");
        String codigoBloco = input.nextLine();

        List<Reserva> reservas = reservaDAO.findAllReservasByBloco(codigoBloco);

        if (reservas.isEmpty()) {
            System.out.println("\nNenhuma reserva encontrada.");
            return;
        }

        for (Reserva reserva : reservas) {
            System.out.println("------------------------------");
            printInfoReserva(reserva);
        }
    }

    public void findReservasByPeriodo() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Buscar reservas por periodo");
        System.out.print("Data início da reserva: ");
        LocalDate dataInicio = LocalDate.parse(input.nextLine());
        System.out.print("Data fim da reserva: ");
        LocalDate dataFim= LocalDate.parse(input.nextLine());
        System.out.print("Horário início da reserva: ");
        LocalTime horaInicio = LocalTime.parse(input.nextLine(), formatter);
        System.out.print("Horário fim da reserva: ");
        LocalTime horaFim = LocalTime.parse(input.nextLine(), formatter);

        List<Reserva> reservas = reservaDAO.findAllReservasByPeriodo(dataInicio ,dataFim ,horaInicio, horaFim);

        if (reservas.isEmpty()) {
            System.out.println("\nNenhuma reserva encontrada.");
            return;
        }

        for (Reserva reserva : reservas) {
            System.out.println("------------------------------");
            printInfoReserva(reserva);
        }
    }

    public void findAllReservas() throws SQLException {
        System.out.println("\nListando todas as reservas:");
        List<Reserva> reservas = reservaDAO.findAll();

        if (reservas.isEmpty()) {
            System.out.println("\nNenhuma reserva encontrada.");
            return;
        }

        for (Reserva reserva : reservas) {
            System.out.println("------------------------------");
            printInfoReserva(reserva);
        }
    }

    private void printInfoReserva(Reserva reserva) {
        System.out.println("ID da reserva: " + reserva.getIdReserva());
        System.out.println("Código da sala: " + reserva.getCodigoSala());
        System.out.println("Matrícula do professor: " + reserva.getMatriculaProfessor());
        System.out.println("Código da matéria: " + reserva.getCodigoMateria());
        System.out.println("Data de início reserva: " + reserva.getDataInicio());
        System.out.println("Data de fim reserva: " + reserva.getDataFim());
        System.out.println("Horário de início reserva: " + reserva.getHoraInicio());
        System.out.println("Horário de fim reserva: " + reserva.getHoraFim());
    }
}
