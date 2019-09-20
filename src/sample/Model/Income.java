package sample.Model;

import java.sql.Date;

public class Income {
    private double amount;
    private Date creationDate;
    private String time;
    private String creator;
    private String moreInfos;
    private int shiftId;
    private int idCaisse;
    private String reason;
    private int type;

    private String indexClient;

    public Income() {
    }

    public Income(int type, double amount, Date creationDate, String time, String moreInfos, String reason, String indexClient) {
        this.type = type;
        this.amount = amount;
        this.creationDate = creationDate;
        this.time = time;
        this.moreInfos = moreInfos;
        this.reason = reason;
        this.indexClient = indexClient;
    }

    /*------------------------------------------------------------------------------------------*/

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getIndexClient() {
        return indexClient;
    }

    public void setIndexClient(String indexClient) {
        this.indexClient = indexClient;
    }
}
