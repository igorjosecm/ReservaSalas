package classes;

// Esta é uma classe "View" ou "DTO" usada para transportar dados de uma consulta complexa.
// Ela não representa um nó no banco de dados.
public class ReservaDetalhada extends Reserva {
    private String nomeSala;
    private String nomeProfessor;
    private String nomeMateria;

    // Getters e Setters para os novos campos
    public String getNomeSala() {
        return nomeSala;
    }

    public void setNomeSala(String nomeSala) {
        this.nomeSala = nomeSala;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public String getNomeMateria() {
        return nomeMateria;
    }

    public void setNomeMateria(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }
}
