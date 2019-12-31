package sample.Model;

public class Report {
    private String minutes;
    private String serviceDate;


    public Report(String minutes, String serviceDate) {
        this.minutes = minutes;
        this.serviceDate = serviceDate;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }
}
