package sample.Model;

public class CustomReport {
    private int idCustomReport;
    private Double reportAmount;
    private String date;
    private String userName;
    private String status;

    public int getIdCustomReport() {
        return idCustomReport;
    }

    public void setIdCustomReport(int idCustomReport) {
        this.idCustomReport = idCustomReport;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
