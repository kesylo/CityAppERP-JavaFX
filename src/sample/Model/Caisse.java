package sample.Model;



public class Caisse {

    private int id;

    private String date;

    private double montant;

    private String remarque;

    private int numeroShift;

    private int closed;

    private int idEmployes;

    private String closedDate;

    private String numeroCaisse;

    private int hasError;

    private double error_amount;

    public Caisse() {
    }

    @Override
    public String toString() {
        return "Caisse{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", montant=" + montant +
                ", remarque='" + remarque + '\'' +
                ", numeroShift=" + numeroShift +
                ", closed=" + closed +
                ", idEmployes=" + idEmployes +
                ", closedDate='" + closedDate + '\'' +
                ", numeroCaisse=" + numeroCaisse +
                '}';
    }

    public Caisse(int id, String date, double montant, String remarque, int numeroShift, int closed, int idEmployes, String closedDate, String numeroCaisse, int hasError, double error_amount) {
        this.id = id;
        this.date = date;
        this.montant = montant;
        this.remarque = remarque;
        this.numeroShift = numeroShift;
        this.closed = closed;
        this.idEmployes = idEmployes;
        this.closedDate = closedDate;
        this.numeroCaisse = numeroCaisse;
        this.hasError = hasError;
        this.error_amount = error_amount;
    }

    //region Getters


    public double getError_amount() {
        return error_amount;
    }

    public void setError_amount(double error_amount) {
        this.error_amount = error_amount;
    }

    public int getHasError() {
        return hasError;
    }

    public void setHasError(int hasError) {
        this.hasError = hasError;
    }

    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    public int getIdEmployes() {
        return idEmployes;
    }

    public void setNumeroCaisse(String numeroCaisse) {
        this.numeroCaisse = numeroCaisse;
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

    public String getDate() {
        return date;
    }

    public int getNumeroShift() {
        return numeroShift;
    }

    public void setNumeroShift(int numeroShift) {
        this.numeroShift = numeroShift;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    //endregion
}
