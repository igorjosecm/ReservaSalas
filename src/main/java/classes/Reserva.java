package classes;
import java.time.LocalDateTime;

public class Reserva {
    private Integer idReserva;
    private String codigoSala;
    private Integer matriculaProfessor;
    private String codigoMateria;
    private LocalDateTime inicioReserva;
    private LocalDateTime fimReserva;

    public Reserva() {
        this.setIdReserva(0);
        this.setCodigoSala("");
        this.setMatriculaProfessor(0);
        this.setCodigoMateria("");
        this.setInicioReserva(LocalDateTime.now());
        this.setFimReserva(LocalDateTime.now());
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public String getCodigoSala() {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala) {
        this.codigoSala = codigoSala;
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

    public LocalDateTime getInicioReserva() {
        return inicioReserva;
    }

    public void setInicioReserva(LocalDateTime inicioReserva) {
        this.inicioReserva = inicioReserva;
    }

    public LocalDateTime getFimReserva() {
        return fimReserva;
    }

    public void setFimReserva(LocalDateTime fimReserva) {
        this.fimReserva = fimReserva;
    }
}
