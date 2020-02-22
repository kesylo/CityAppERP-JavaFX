package sample.Model;

import sample.Global.Global;

public class PlanningReport {
    private String serviceDate;
    private String userName;
    private String startTime;
    private String endTime;
    private long totalTime;

    public PlanningReport(String serviceDate, String startTime, String endTime, long totalTime, String userName) {
        this.serviceDate = serviceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.userName = userName;
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

    public String getTotalTime() {
        return Global.millisToTime(totalTime);
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
