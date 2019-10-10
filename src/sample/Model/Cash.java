package sample.Model;

public class Cash {

    private int idCaisse;
    private int numShift;
    private double lessThanFiftyCents;
    private int fiftyCents;
    private int oneEuro;
    private int twoEuros;
    private int fiveEuros;
    private int tenEuros;
    private int twentyEuros;
    private int fiftyEuros;
    private int onehundredeuros;
    private int twoHundredEuros;

    public Cash(int idCaisse, int numShift, double lessThanFiftyCents, int fiftyCents, int oneEuro, int twoEuros, int fiveEuros, int tenEuros, int twentyEuros, int fiftyEuros, int onehundredeuros, int twoHundredEuros) {
        this.idCaisse = idCaisse;
        this.numShift = numShift;
        this.lessThanFiftyCents = lessThanFiftyCents;
        this.fiftyCents = fiftyCents;
        this.oneEuro = oneEuro;
        this.twoEuros = twoEuros;
        this.fiveEuros = fiveEuros;
        this.tenEuros = tenEuros;
        this.twentyEuros = twentyEuros;
        this.fiftyEuros = fiftyEuros;
        this.onehundredeuros = onehundredeuros;
        this.twoHundredEuros = twoHundredEuros;
    }

    public int getIdCaisse() {
        return idCaisse;
    }

    public void setIdCaisse(int idCaisse) {
        this.idCaisse = idCaisse;
    }

    public int getNumShift() {
        return numShift;
    }

    public double getLessThanFiftyCents() {
        return lessThanFiftyCents;
    }

    public int getFiftyCents() {
        return fiftyCents;
    }

    public int getOneEuro() {
        return oneEuro;
    }

    public int getTwoEuros() {
        return twoEuros;
    }

    public int getFiveEuros() {
        return fiveEuros;
    }

    public int getTenEuros() {
        return tenEuros;
    }

    public int getTwentyEuros() {
        return twentyEuros;
    }

    public int getFiftyEuros() {
        return fiftyEuros;
    }

    public int getOnehundredeuros() {
        return onehundredeuros;
    }

    public int getTwoHundredEuros() {
        return twoHundredEuros;
    }

}
