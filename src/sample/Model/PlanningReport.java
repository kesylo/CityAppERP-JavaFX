package sample.Model;

public class PlanningReport {
    private String serviceDate;
    private String startTime;
    private String endTime;
    private double totalTime;

    public PlanningReport(String serviceDate, String startTime, String endTime, double totalTime) {
        this.serviceDate = serviceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }
}
