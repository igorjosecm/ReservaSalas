package classes;

import java.time.LocalDate;

public class MateriaProfessor {
    private Integer matriculaProfessor;
    private String codigoMateria;
    private LocalDate inicioPeriodo;
    private LocalDate fimPeriodo;

    public MateriaProfessor() {
        this.setMatriculaProfessor(0);
        this.setCodigoMateria("");
        this.setInicioPeriodo(LocalDate.now());
        this.setFimPeriodo(LocalDate.now());
    }

    public Integer getMatriculaProfessor() {
        return matriculaProfessor;
    }

    public void setMatriculaProfessor(Integer matriculaProfessor) {
        this.matriculaProfessor = matriculaProfessor;
    }

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public LocalDate getInicioPeriodo() {
        return inicioPeriodo;
    }

    public void setInicioPeriodo(LocalDate inicioPeriodo) {
        this.inicioPeriodo = inicioPeriodo;
    }

    public LocalDate getFimPeriodo() {
        return fimPeriodo;
    }

    public void setFimPeriodo(LocalDate fimPeriodo) {
        this.fimPeriodo = fimPeriodo;
    }
}
