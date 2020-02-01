package sample.Model;

public class Report {

    private int idReport;
    private Double reportAmount;
    private String date;
    private int userID;
    private int status;


    public Report(int idReport, Double reportAmount, String date, int userID, int status) {
        this.idReport = idReport;
        this.reportAmount = reportAmount;
        this.date = date;
        this.userID = userID;
        this.status = status;
    }

    public int getIdReport() {
        return idReport;
    }

    public void setIdReport(int idReport) {
        this.idReport = idReport;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getReportAmount() {
        return reportAmount;
    }

    public void setReportAmount(Double reportAmount) {
        this.reportAmount = reportAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
