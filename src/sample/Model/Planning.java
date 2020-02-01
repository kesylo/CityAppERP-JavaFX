package sample.Model;

public class Planning implements Comparable{
    private String prestationDate;
    private String startTime;
    private String endTime;
    private int idUser;
    private String week;
    private String status;
    private String callRedirect;

    public Planning() {
    }

    public Planning(String prestationDate, String startTime, String endTime) {
        this.prestationDate = prestationDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getPrestationDate() {
        return prestationDate;
    }

    public void setPrestationDate(String prestationDate) {
        this.prestationDate = prestationDate;
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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCallRedirect() {
        return callRedirect;
    }

    public void setCallRedirect(String callRedirect) {
        this.callRedirect = callRedirect;
    }

    @Override
    public int compareTo(Object o) {
        return this.getPrestationDate().compareTo(((Planning) o).getPrestationDate());
    }
}
