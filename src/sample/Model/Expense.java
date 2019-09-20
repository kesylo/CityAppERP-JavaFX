package sample.Model;

import java.sql.Date;

public class Expense {
    private double amount;
    private Date creationDate;
    private String time;
    private String creator;
    private String moreInfos;
    private int shiftId;
    private int idCaisse;
    private String reason;
    private int type;

    private String salaryBeneficial;

    public Expense() {
    }

    public Expense(double amount, Date creationDate, String time, String moreInfos, String reason, int type, String salaryBeneficial) {
        this.amount = amount;
        this.creationDate = creationDate;
        this.time = time;
        this.moreInfos = moreInfos;
        this.reason = reason;
        this.type = type;
        this.salaryBeneficial = salaryBeneficial;
    }

    /*-------------------------------------------------------------------------------------*/

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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMoreInfos() {
        return moreInfos;
    }

    public void setMoreInfos(String moreInfos) {
        this.moreInfos = moreInfos;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
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
}
