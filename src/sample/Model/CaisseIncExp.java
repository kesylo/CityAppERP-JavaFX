package sample.Model;

import java.sql.Date;

public class CaisseIncExp {

    //region pojo
    private int idCaisseIncExp;
    private double amount;
    private Date creationDate;
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


    public CaisseIncExp(double amount, Date creationDate, String time, int idUser, String comment, int shiftNumber, int idCaisse, String reason, String clientIndex, int type, String salaryBeneficial) {
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

    //region Getters and setters
    public int getIdCaisseIncExp() {
        return idCaisseIncExp;
    }

    public void setIdCaisseIncExp(int idCaisseIncExp) {
        this.idCaisseIncExp = idCaisseIncExp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(int shiftNumber) {
        this.shiftNumber = shiftNumber;
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

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClientIndex() {
        return clientIndex;
    }

    public void setClientIndex(String clientIndex) {
        this.clientIndex = clientIndex;
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

    public void setSalaryBeneficial(String salaryBeneficial) {
        this.salaryBeneficial = salaryBeneficial;
    }
    //endregion
}
