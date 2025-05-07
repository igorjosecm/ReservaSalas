package controllers;

import classes.CompositeKey;
import classes.Professor;
import dao.ProfessorDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ProfessorController {
    private final ProfessorDAO professorDAO;

    public ProfessorController(Connection connection) {
        this.professorDAO = new ProfessorDAO(connection);
    }

    public void createProfessor() throws SQLException {
        Scanner input = new Scanner(System.in);
        findAllProfessores();
        System.out.println("\n- Cadastro de professor");
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = input.nextInt();
        System.out.print("Nome completo: ");
        String nomeCompleto = input.nextLine();
        System.out.print("Data de nascimento: ");
        LocalDate dataNascimento = LocalDate.parse(input.nextLine());
        System.out.print("Email: ");
        String email = input.nextLine();

        Professor professor = new Professor();
        professor.setMatriculaProfessor(matriculaProfessor);
        professor.setNomeCompleto(nomeCompleto);
        professor.setDataNascimento(dataNascimento);
        professor.setEmail(email);

        professorDAO.create(professor);
        System.out.println("\nProfessor registrado com sucesso!");
    }

    public void updateProfessor() throws SQLException {
        Scanner input = new Scanner(System.in);
        findAllProfessores();
        System.out.println("\n- Atualização de professor");
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = input.nextInt();
        System.out.print("Nome completo: ");
        String nomeCompleto = input.nextLine();
        System.out.print("Data de nascimento: ");
        LocalDate dataNascimento = LocalDate.parse(input.nextLine());
        System.out.print("Email: ");
        String email = input.nextLine();

        Professor professor = new Professor();
        professor.setMatriculaProfessor(matriculaProfessor);
        professor.setNomeCompleto(nomeCompleto);
        professor.setDataNascimento(dataNascimento);
        professor.setEmail(email);

        professorDAO.update(professor);
        System.out.println("\nProfessor atualizado com sucesso!");
    }

    public void deleteProfessor() throws SQLException {
        Scanner input = new Scanner(System.in);
        findAllProfessores();
        System.out.println("\n- Exclusão de professor");
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = input.nextInt();

        CompositeKey key = new CompositeKey();
        key.addKey("matricula_professor", matriculaProfessor);

        professorDAO.delete(key);
        System.out.println("\nProfessor excluído com sucesso!");
    }

    public void findProfessorById() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\n- Busca de professor");
        System.out.print("Matrícula do professor: ");
        Integer matriculaProfessor = input.nextInt();

        CompositeKey key = new CompositeKey();
        key.addKey("matricula_professor", matriculaProfessor);

        Professor professor = professorDAO.findById(key);
        if (professor != null) {
            System.out.println("\nProfessor encontrado:");
            System.out.println("Nome completo: " + professor.getNomeCompleto());
            System.out.println("Data nascimento: " + professor.getDataNascimento());
            System.out.println("Email: " + professor.getEmail());
        } else {
            System.out.println("\nProfessor não encontrado.");
        }
    }

    public void findAllProfessores() throws SQLException {
        System.out.println("\nListando todos os professores:");
        List<Professor> professores = professorDAO.findAll();

        if (professores.isEmpty()) {
            System.out.println("\nNenhum professor encontrado.");
            return;
        }

        for (Professor professor : professores) {
            System.out.println("------------------------------");
            System.out.println("Matrícula do professor: " + professor.getMatriculaProfessor());
            System.out.println("Nome completo: " + professor.getNomeCompleto());
            System.out.println("Data nascimento: " + professor.getDataNascimento());
            System.out.println("Email: " + professor.getEmail());
        }
    }
}
