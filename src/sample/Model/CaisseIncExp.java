package sample.Model;

public class CaisseIncExp {

    //region pojo
    private double amount;
    private String creationDate;
    private String time;
    private int idUser;
    private String comment;
    private int shiftNumber;
    private int idCaisse;
    private String reason;
    private String clientIndex;
    private int type;
    private String salaryBeneficial;
    //endregion


    @Override
    public String toString() {
        return "CaisseIncExp{" +
                ", amount=" + amount +
                ", creationDate=" + creationDate +
                ", time='" + time + '\'' +
                ", idUser=" + idUser +
                ", comment='" + comment + '\'' +
                ", shiftNumber=" + shiftNumber +
                ", idCaisse=" + idCaisse +
                ", reason='" + reason + '\'' +
                ", clientIndex='" + clientIndex + '\'' +
                ", type=" + type +
                ", salaryBeneficial='" + salaryBeneficial + '\'' +
                '}';
    }

    public CaisseIncExp(double amount, String creationDate, String time, int idUser, String comment, int shiftNumber, int idCaisse, String reason, String clientIndex, int type, String salaryBeneficial) {
        this.amount = amount;
        this.creationDate = creationDate;
        this.time = time;
        this.idUser = idUser;
        this.comment = comment;
        this.shiftNumber = shiftNumber;
        this.idCaisse = idCaisse;
        this.reason = reason;
        this.clientIndex = clientIndex;
        this.type = type;
        this.salaryBeneficial = salaryBeneficial;
    }

    public double getAmount() {
        return amount;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getComment() {
        return comment;
    }

    public int getShiftNumber() {
        return shiftNumber;
    }

    public int getIdCaisse() {
        return idCaisse;
    }

    public void setIdCaisse(int idCaisse) {
        this.idCaisse = idCaisse;
    }

    public String getReason() {
        return reason;
    }

    public String getClientIndex() {
        return clientIndex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSalaryBeneficial() {
        return salaryBeneficial;
    }

    //endregion
}
