package sample.Model;

public class Cash {

    private int row;
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

    public Cash() {
    }

    public Cash(Double lessThanFiftyCents, int fiftyCents, int oneEuro, int twoEuros, int fiveEuros, int tenEuros, int twentyEuros, int fiftyEuros, int oneHundrdEuros, int twoHundredEuros) {
        this.lessThanFiftyCents = lessThanFiftyCents;
        this.fiftyCents = fiftyCents;
        this.oneEuro = oneEuro;
        this.twoEuros = twoEuros;
        this.fiveEuros = fiveEuros;
        this.tenEuros = tenEuros;
        this.twentyEuros = twentyEuros;
        this.fiftyEuros = fiftyEuros;
        this.onehundredeuros = oneHundrdEuros;
        this.twoHundredEuros = twoHundredEuros;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
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

    public int getOneHundredEuros() {
        return onehundredeuros;
    }

    public void setOneHundredEuros(int oneHundrdEuros) {
        this.onehundredeuros = oneHundrdEuros;
    }

    public int getTwoHundredEuros() {
        return twoHundredEuros;
    }

    public void setTwoHundredEuros(int twoHundredEuros) {
        this.twoHundredEuros = twoHundredEuros;
    }
}
