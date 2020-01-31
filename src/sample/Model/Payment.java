package sample.Model;

public class Payment {
    private Double amount;
    private String date;
    private int userId;
    private String description;

    public Payment(Double amount, String date, int userId, String description) {
        this.amount = amount;
        this.date = date;
        this.userId = userId;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "amount=" + amount +
                ", date=" + date +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                '}';
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

    public void setDescription(String description) {
        this.description = description;
    }
}
