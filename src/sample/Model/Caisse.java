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

    // extra
    private int dayNbr;
    private String employeeName;
    private double incomes;
    private double expenses;

    public Caisse() {
    }

    public Caisse(int id, String date, double montant, String remarque, int numeroShift, int closed, int idEmployes, String closedDate, String numeroCaisse, int hasError, double error_amount, int dayNbr, String employeeName, double incomes, double expenses) {
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
        this.dayNbr = dayNbr;
        this.employeeName = employeeName;
        this.incomes = incomes;
        this.expenses = expenses;
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


    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public int getDayNbr() {
        return dayNbr;
    }

    public void setDayNbr(int dayNbr) {
        this.dayNbr = dayNbr;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public double getIncomes() {
        return incomes;
    }

    public void setIncomes(double incomes) {
        this.incomes = incomes;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

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
