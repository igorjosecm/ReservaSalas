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
                System.out.print(prompt.isEmpty() ? "Selecione a opção desejada: " : prompt);
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Por favor, informe um número inteiro.");
            }
        }
    }

    public static int getIntInput(Scanner input) {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Por favor, informe um número inteiro.");
            }
        }
    }

    /**
     * Obtém um LocalDate do usuário, sempre exigindo uma entrada válida.
     * @param input O objeto Scanner.
     * @return O LocalDate inserido.
     */
    public static LocalDate getLocalDateInput(Scanner input) {
        return getLocalDateInput(input, false);
    }

    /**
     * Obtém um LocalDate do usuário.
     * @param input O objeto Scanner.
     * @param allowEmpty Se true, permite que o usuário pressione ENTER para retornar null.
     * @return O LocalDate inserido ou null se a entrada for vazia e permitida.
     */
    public static LocalDate getLocalDateInput(Scanner input, boolean allowEmpty) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            String userInput = input.nextLine();
            if (allowEmpty && userInput.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(userInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use dd/mm/aaaa ou deixe em branco se aplicável.");
            }
        }
    }

    /**
     * Obtém um LocalTime do usuário, sempre exigindo uma entrada válida.
     * @param input O objeto Scanner.
     * @return O LocalTime inserido.
     */
    public static LocalTime getLocalTimeInput(Scanner input) {
        return getLocalTimeInput(input, false);
    }

    /**
     * Obtém um LocalTime do usuário.
     * @param input O objeto Scanner.
     * @param allowEmpty Se true, permite que o usuário pressione ENTER para retornar null.
     * @return O LocalTime inserido ou null se a entrada for vazia e permitida.
     */
    public static LocalTime getLocalTimeInput(Scanner input, boolean allowEmpty) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        while (true) {
            String userInput = input.nextLine();
            if (allowEmpty && userInput.isEmpty()) {
                return null;
            }
            try {
                return LocalTime.parse(userInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora inválido. Use hh:mm ou deixe em branco se aplicável.");
            }
        }
    }

}
