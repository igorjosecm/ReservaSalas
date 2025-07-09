package classes;

import java.time.LocalDate;

// Esta é uma classe "View" ou "DTO" usada para transportar dados de uma consulta complexa.
// Ela não representa um nó no banco de dados.
public class MateriaLecionada extends Materia {
    private LocalDate inicioPeriodo;
    private LocalDate fimPeriodo;

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
