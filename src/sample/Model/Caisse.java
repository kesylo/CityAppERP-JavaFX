package sample.Model;


import java.sql.Date;

public class Caisse {

    private int id;

    private Date date;

    private Double montant;

    private String remarque;

    private int numeroShift;

    private int closed;

    private int idEmployes;

    private String closedDate;

    public Caisse() {
    }

    public Caisse(int id, Date date, Double montant, int numeroShift, String remarque, int closed, int idEmployes, String closedDate) {
        this.id = id;
        this.date = date;
        this.montant = montant;
        this.numeroShift = numeroShift;
        this.remarque = remarque;
        this.closed = closed;
        this.idEmployes = idEmployes;
        this.closedDate = closedDate;
    }

    public int getIdEmployes() {
        return idEmployes;
    }

    public void setIdEmployes(int idEmployes) {
        this.idEmployes = idEmployes;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public int getNumeroShift() {
        return numeroShift;
    }

    public void setNumeroShift(int numeroShift) {
        this.numeroShift = numeroShift;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }
}
