package sample.Model;

import java.util.Date;

public class Caisse {

    private int id;

    private Date date;

    private float j_prec;

    private String remarque;

    public Caisse() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getJ_prec() {
        return j_prec;
    }

    public void setJ_prec(float j_prec) {
        this.j_prec = j_prec;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
