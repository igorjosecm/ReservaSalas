package classes;

import java.time.LocalDate;

public class Professor {
    private Integer matriculaProfessor;
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private String email;


    public Professor() {
        this.setMatriculaProfessor(0);
        this.setNomeCompleto("");
        this.setDataNascimento(LocalDate.MIN);
        this.setEmail("");
    }

    public Integer getMatriculaProfessor() {
        return matriculaProfessor;
    }

    public void setMatriculaProfessor(Integer matriculaProfessor) {
        this.matriculaProfessor = matriculaProfessor;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
