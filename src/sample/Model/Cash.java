package sample.Model;

public class Cash {

    private int idCaisse;
    private int numShift;
    private Double lessThanFiftyCents;
    private int fiftyCents;
    private int oneEuro;
    private int twoEuros;
    private int fiveEuros;
    private int tenEuros;
    private int twentyEuros;
    private int fiftyEuros;
    private int onehundredeuros;
    private int twoHundredEuros;

    public Cash(int idCaisse, int numShift, Double lessThanFiftyCents, int fiftyCents, int oneEuro, int twoEuros, int fiveEuros, int tenEuros, int twentyEuros, int fiftyEuros, int onehundredeuros, int twoHundredEuros) {
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

    public void setNumShift(int numShift) {
        this.numShift = numShift;
    }

    public Double getLessThanFiftyCents() {
        return lessThanFiftyCents;
    }

    public void setLessThanFiftyCents(Double lessThanFiftyCents) {
        this.lessThanFiftyCents = lessThanFiftyCents;
    }

    public int getFiftyCents() {
        return fiftyCents;
    }

    public void setFiftyCents(int fiftyCents) {
        this.fiftyCents = fiftyCents;
    }

    public int getOneEuro() {
        return oneEuro;
    }

    public void setOneEuro(int oneEuro) {
        this.oneEuro = oneEuro;
    }

    public int getTwoEuros() {
        return twoEuros;
    }

    public void setTwoEuros(int twoEuros) {
        this.twoEuros = twoEuros;
    }

    public int getFiveEuros() {
        return fiveEuros;
    }

    public void setFiveEuros(int fiveEuros) {
        this.fiveEuros = fiveEuros;
    }

    public int getTenEuros() {
        return tenEuros;
    }

    public void setTenEuros(int tenEuros) {
        this.tenEuros = tenEuros;
    }

    public int getTwentyEuros() {
        return twentyEuros;
    }

    public void setTwentyEuros(int twentyEuros) {
        this.twentyEuros = twentyEuros;
    }

    public int getFiftyEuros() {
        return fiftyEuros;
    }

    public void setFiftyEuros(int fiftyEuros) {
        this.fiftyEuros = fiftyEuros;
    }

    public int getOnehundredeuros() {
        return onehundredeuros;
    }

    public void setOnehundredeuros(int onehundredeuros) {
        this.onehundredeuros = onehundredeuros;
    }

    public int getTwoHundredEuros() {
        return twoHundredEuros;
    }

    public void setTwoHundredEuros(int twoHundredEuros) {
        this.twoHundredEuros = twoHundredEuros;
    }
}
