package helpers;

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
}
