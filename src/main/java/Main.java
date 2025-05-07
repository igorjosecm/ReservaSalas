import classes.DatabaseConnection;
import classes.Reserva;
import classes.Sala;
import controllers.SalaController;
import dao.SalaDAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
public class Main {
    static List<Sala> salas = new ArrayList<>();
    static List<Reserva> reservas = new ArrayList<>();

    public static void loadRooms() {
        try (Scanner scanner = new Scanner(new File("rooms.txt"))) {
            int roomCount = scanner.nextInt();
            for (int i = 0; i < roomCount; i++) {
                Sala sala = new Sala();
                sala.setCodSala(scanner.next());
                sala.setNomeSala(scanner.next());
                sala.setCapacidade(scanner.nextInt());
                sala.setAndar(scanner.nextInt());
                salas.add(sala);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening rooms file.");
        }
    }


    public static void saveReservations() {
        try (PrintWriter writer = new PrintWriter("reservations.txt")) {
            writer.println(reservas.size());
            for (Reservation r : reservas) {
                writer.printf("%d %d %s %s %d%n", r.id, r.roomId, r.roomName, r.date, r.peopleCount);
            }
        } catch (IOException e) {
            System.out.println("Error opening reservations file.");
        }
    }

    public static void loadReservations() {
        try (Scanner scanner = new Scanner(new File("reservations.txt"))) {
            int reservationCount = scanner.nextInt();
            for (int i = 0; i < reservationCount; i++) {
                Reservation r = new Reservation();
                r.id = scanner.nextInt();
                r.roomId = scanner.nextInt();
                r.roomName = scanner.next();
                r.date = scanner.next();
                r.peopleCount = scanner.nextInt();
                reservas.add(r);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening reservations file.");
        }
    }

    public static void listRooms() {
        System.out.println("Available rooms:");
        for (Room r : salas) {
            System.out.printf("ID: %d, Name: %s, Capacity: %d, Available: %d%n", r.id, r.name, r.capacity, r.available);
        }
    }

    public static void listReservations() {
        System.out.println("All reservations:");
        for (Reservation r : reservas) {
            System.out.printf("Reservation ID: %d, Room ID: %d, Room Name: %s, Date: %s, People Count: %d%n",
                    r.id, r.roomId, r.roomName, r.date, r.peopleCount);
        }
    }

    public static void listRoomReservations(int roomId) {
        System.out.printf("Reservations for Room ID %d:%n", roomId);
        for (Reservation r : reservas) {
            if (r.roomId == roomId) {
                System.out.printf("Reservation ID: %d, Date: %s, People Count: %d%n", r.id, r.date, r.peopleCount);
            }
        }
    }

    public static void makeReservation(Scanner scanner) {
        System.out.print("Enter Room ID: ");
        int roomId = scanner.nextInt();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.next();
        System.out.print("Enter number of people: ");
        int peopleCount = scanner.nextInt();

        Room room = null;
        for (Room r : salas) {
            if (r.id == roomId) {
                room = r;
                break;
            }
        }

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        if (peopleCount > room.capacity) {
            System.out.println("Room capacity exceeded.");
            return;
        }

        for (Reservation r : reservas) {
            if (r.roomId == roomId && r.date.equals(date)) {
                System.out.println("Room already reserved on this date.");
                return;
            }
        }

        Reservation reservation = new Reservation();
        reservation.id = reservas.size() + 1;
        reservation.roomId = roomId;
        reservation.roomName = room.name;
        reservation.date = date;
        reservation.peopleCount = peopleCount;

        reservas.add(reservation);
        System.out.println("Reservation made successfully.");
    }

    public static void cancelReservation(Scanner scanner) {
        System.out.print("Enter Reservation ID to cancel: ");
        int reservationId = scanner.nextInt();

        Reservation target = null;
        for (Reservation r : reservas) {
            if (r.id == reservationId) {
                target = r;
                break;
            }
        }

        if (target == null) {
            System.out.println("Reservation not found.");
            return;
        }

        reservas.remove(target);
        System.out.println("Reservation cancelled successfully.");
    }

    public static void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. List Rooms");
        System.out.println("2. List All Reservations");
        System.out.println("3. List Reservations for a Room");
        System.out.println("4. Make a Reservation");
        System.out.println("5. Cancel a Reservation");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void main(String[] args) {
        loadRooms();
        loadReservations();

        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            printMenu();
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    listRooms();
                    break;
                case 2:
                    listReservations();
                    break;
                case 3:
                    System.out.print("Enter Room ID: ");
                    int roomId = scanner.nextInt();
                    listRoomReservations(roomId);
                    break;
                case 4:
                    makeReservation(scanner);
                    break;
                case 5:
                    cancelReservation(scanner);
                    break;
                case 6:
                    System.out.println("Exiting program. Here are the final reservations:");
                    listReservations();
                    saveReservations();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);

        scanner.close();
    }

}
*/

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            SalaController salaController = new SalaController(conn);

            boolean rodando = true;
            while (rodando) {
                printMenu();
                int choice = getIntInput("Selecione a opção desejada: ");

                switch (choice) {
                    case 1:
                        salaController.createSala();
                        break;
                    case 2:
                        salaController.findSalaById();
                        break;
                    case 3:
                        salaController.updateSala();
                        break;
                    case 4:
                        salaController.deleteSala();
                        break;
                    case 5:
                        salaController.findAllSalas();
                        break;
                    case 6:
                        rodando = false;
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                        break;
                }

                if (rodando) {
                    System.out.println("\nPressione ENTER para continuar...");
                    scanner.nextLine();
                }
            }

            System.out.println("Fechando aplicação. Até mais!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }


//        try (Connection con = DatabaseConnection.getConnection()) {
//            System.out.println("Conexão estabelecida com sucesso!");
//
//
//            // Exemplo de consulta
//            String sql = "SELECT * FROM reserva_salas.sala WHERE capacidade > ?";
//
//            try (PreparedStatement stmt = con.prepareStatement(sql)) {
//                stmt.setInt(1, 30);
//
//                try (ResultSet rs = stmt.executeQuery()) {
//                    while (rs.next()) {
//                        System.out.printf("Sala: %s, Capacidade: %d%n",
//                                rs.getString("nome_sala"),
//                                rs.getInt("capacidade"));
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Erro na conexão com o banco de dados:");
//            e.printStackTrace();
//        } finally {
//            DatabaseConnection.close();
//        }
    }

    private static void printMenu() {
        System.out.println("\n=== Reserva de Salas ===");
        System.out.println("1. Adicionar sala");
        System.out.println("2. Buscar sala");
        System.out.println("3. Alterar sala");
        System.out.println("4. Excluir sala");
        System.out.println("5. Listar salas");
        System.out.println("6. Sair");
        System.out.println("=============================");
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Por favor informe um número inteiro.");
            }
        }
    }
}
