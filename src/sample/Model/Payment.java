package sample.Model;

public class Payment {
    private Double amount;
    private String date;
    private int userId;
    private int idPayment;
    private String description;
    private String userName;
    private int from;

    public Payment(Double amount, String date, int userId, String description) {
        this.amount = amount;
        this.date = date;
        this.userId = userId;
        this.description = description;
    }

    public Payment(Double amount, String date, int userId, int idPayment, String description) {
        this.amount = amount;
        this.date = date;
        this.userId = userId;
        this.idPayment = idPayment;
        this.description = description;
    }

    public Payment(Double amount, String date, int userId, int idPayment, String description, int from) {
        this.amount = amount;
        this.date = date;
        this.userId = userId;
        this.idPayment = idPayment;
        this.description = description;
        this.from = from;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "amount=" + amount +
                ", date='" + date + '\'' +
                ", userId=" + userId +
                ", idPayment=" + idPayment +
                ", description='" + description + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(int idPayment) {
        this.idPayment = idPayment;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
