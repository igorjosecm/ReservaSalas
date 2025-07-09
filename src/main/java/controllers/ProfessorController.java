package controllers;

import classes.Professor;
import dao.ProfessorDAO;
import helpers.Helpers;
import org.neo4j.driver.Driver;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.driver.exceptions.Neo4jException;

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

        try {
            if (professorDAO.findById(matriculaProfessor) != null) {
                System.out.println("\nErro: Um professor com esta matrícula já existe.");
                return;
            }

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
                System.out.println("\nErro de validação: A data de nascimento não pode ser no futuro.");
            }
        } catch (ClientException e) {
            System.err.println("\nErro ao criar professor: A matrícula ou o email já podem estar em uso. " + e.getMessage());
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao criar o professor: " + e.getMessage());
        }
    }

    public void updateProfessor() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Atualização de professor");
        System.out.print("Matrícula do professor a ser atualizado: ");
        Integer matriculaProfessor = Helpers.getIntInput(input);

        try {
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
                    System.out.println("\nErro de validação: A data de nascimento não pode ser no futuro.");
                }
            } else {
                System.out.println("\nProfessor não encontrado.");
            }
        } catch (ClientException e) {
            System.err.println("\nErro ao atualizar professor: O email informado já pode estar em uso. " + e.getMessage());
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao atualizar o professor: " + e.getMessage());
        }
    }

    public void deleteProfessor() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Exclusão de professor");
        System.out.print("Matrícula do professor a ser excluído: ");
        Integer matriculaProfessor = Helpers.getIntInput(input);

        try {
            if (professorDAO.findById(matriculaProfessor) == null) {
                System.out.println("\nErro: Professor não encontrado.");
                return;
            }
            professorDAO.delete(matriculaProfessor);
            System.out.println("\nProfessor e seus vínculos (leciona, reservas) foram excluídos com sucesso!");
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao excluir o professor: " + e.getMessage());
        }
    }

    public void findProfessorById() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de professor");
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = Helpers.getIntInput(input);

        try {
            Professor professor = professorDAO.findById(matriculaProfessor);
            if (professor != null) {
                System.out.println("\nProfessor encontrado:");
                printInfoProfessor(professor);
            } else {
                System.out.println("\nProfessor não encontrado.");
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao buscar o professor: " + e.getMessage());
        }
    }

    public void findAllProfessores() {
        System.out.println("\nListando todos os professores:");
        try {
            List<Professor> professores = professorDAO.findAll();

            if (professores.isEmpty()) {
                System.out.println("\nNenhum professor encontrado.");
                return;
            }

            for (Professor professor : professores) {
                System.out.println("------------------------------");
                printInfoProfessor(professor);
            }
        } catch (Neo4jException e) {
            System.err.println("\nOcorreu um erro no banco de dados ao listar os professores: " + e.getMessage());
        }
    }

    private void printInfoProfessor(Professor professor) {
        System.out.println("Matrícula: " + professor.getMatriculaProfessor());
        System.out.println("Nome completo: " + professor.getNomeCompleto());
        if (professor.getDataNascimento() != null) {
            System.out.println("Data de nascimento: " + formatter.format(professor.getDataNascimento()));
        }
        System.out.println("Email: " + professor.getEmail());
    }
}
