package helpers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Helpers {
    public static int getIntInput(String prompt, Scanner input) {
        while (true) {
            try {
                System.out.print(prompt.isEmpty() ? "Selecione a opção desejada: " : prompt );
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Por favor informe um número inteiro.");
            }
        }
    }

    public static int getIntInput(Scanner input) {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Por favor informe um número inteiro.");
            }
        }
    }

    public static LocalDate getLocalDateInput(Scanner input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                String localDateInput = input.nextLine();
                return LocalDate.parse(localDateInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
            }
        }
    }

    public static LocalTime getLocalTimeInput(Scanner input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        while (true) {
            try {
                String localDateInput = input.nextLine();
                return LocalTime.parse(localDateInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora inválido. Use hh:mm.");
            }
        }
    }
}
