package controllers;

import classes.Professor;
import dao.ProfessorDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ProfessorController {
    private final ProfessorDAO professorDAO;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProfessorController(Driver driver) {
        this.professorDAO = new ProfessorDAO(driver);
    }

    public void createProfessor() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Cadastro de professor");
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = Helpers.getIntInput(input);

        Professor professor = professorDAO.findById(matriculaProfessor);
        if (professor == null) {
            System.out.print("Nome completo: ");
            String nomeCompleto = input.nextLine();
            System.out.print("Data de nascimento (dd/mm/yyyy): ");
            LocalDate dataNascimento = Helpers.getLocalDateInput(input);

            if (dataNascimento.isBefore(LocalDate.now())) {
                System.out.print("Email: ");
                String email = input.nextLine();

                Professor newProfessor = new Professor();
                newProfessor.setMatriculaProfessor(matriculaProfessor);
                newProfessor.setNomeCompleto(nomeCompleto);
                newProfessor.setDataNascimento(dataNascimento);
                newProfessor.setEmail(email);

                professorDAO.create(newProfessor);
                System.out.println("\nProfessor registrado com sucesso!");
            } else {
                System.out.println("\nInforme uma data de nascimento válida!");
            }
        } else {
            System.out.println("\nEsse professor já existe! Informe uma matrícula diferente.");
        }
    }

    public void updateProfessor() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de professor");
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = Helpers.getIntInput(input);

        Professor professor = professorDAO.findById(matriculaProfessor);
        if (professor != null) {
            System.out.print("Novo nome completo: ");
            String nomeCompleto = input.nextLine();
            System.out.print("Nova data de nascimento (dd/mm/yyyy): ");
            LocalDate dataNascimento = Helpers.getLocalDateInput(input);

            if (dataNascimento.isBefore(LocalDate.now())) {
                System.out.print("Novo email: ");
                String email = input.nextLine();

                professor.setNomeCompleto(nomeCompleto);
                professor.setDataNascimento(dataNascimento);
                professor.setEmail(email);

                professorDAO.update(professor);
                System.out.println("\nProfessor atualizado com sucesso!");
            } else {
                System.out.println("\nInforme uma data de nascimento válida!");
            }
        } else {
            System.out.println("\nProfessor não encontrado.");
        }
    }

    public void deleteProfessor() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de professor");
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = Helpers.getIntInput(input);

        Professor professor = professorDAO.findById(matriculaProfessor);
        if (professor != null) {
            // O GenericDAO usa DETACH DELETE, que removeria o professor e qualquer
            // relacionamento :LECIONA ou :REALIZADA_POR conectado a ele.
            professorDAO.delete(matriculaProfessor);
            System.out.println("\nProfessor excluído com sucesso!");
        } else {
            System.out.println("\nProfessor não encontrado.");
        }
    }

    public void findProfessorById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de professor");
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = Helpers.getIntInput(input);

        Professor professor = professorDAO.findById(matriculaProfessor);
        if (professor != null) {
            System.out.println("\nProfessor encontrado:");
            printInfoProfessor(professor);
        } else {
            System.out.println("\nProfessor não encontrado.");
        }
    }

    public void findAllProfessores() {
        System.out.println("\nListando todos os professores:");
        List<Professor> professores = professorDAO.findAll();

        if (professores.isEmpty()) {
            System.out.println("\nNenhum professor encontrado.");
            return;
        }

        for (Professor professor : professores) {
            System.out.println("------------------------------");
            printInfoProfessor(professor);
        }
    }

    private void printInfoProfessor(Professor professor) {
        System.out.println("Matrícula do professor: " + professor.getMatriculaProfessor());
        System.out.println("Nome completo: " + professor.getNomeCompleto());
        // Formata a data para exibição
        if (professor.getDataNascimento() != null) {
            System.out.println("Data nascimento: " + formatter.format(professor.getDataNascimento()));
        }
        System.out.println("Email: " + professor.getEmail());
    }
}
