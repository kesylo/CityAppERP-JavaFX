package sample.Global;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.Caisse;
import sample.Model.CaisseIncExp;
import sample.Model.Cash;

public final class CashRegisterGlobal {

    //region Caisse App

    private static Caisse currentCaisse = new Caisse();
    private static Caisse previewCaisse = new Caisse();
    private static Caisse beforeCurrentCaisse = new Caisse();
    private static int nberOfCaisses;
    private static double computedSoldeCaisse;
    private static Caisse newCaisse = new Caisse();
    private static Cash caisseCash = null;
    private static double countCashResult = 0.0;
    private static double errorAmount = 0.0;
    private static CaisseIncExp incExpError = new CaisseIncExp();
    private static ObservableList<Caisse> caisseList = FXCollections.observableArrayList();
    private static int nberOfCaissesWithSameDate;
    private static String availableCaisseNumber;
    private static int errorOnClose;

    //endregion

    //region Getters and Setters

    public static CaisseIncExp getIncExpError() {
        return incExpError;
    }

    public static void setIncExpError(CaisseIncExp incExpError) {
        CashRegisterGlobal.incExpError = incExpError;
    }

    public static Caisse getNewCaisse() {
        return newCaisse;
    }

    public static void setNewCaisse(Caisse newCaisse) {
        CashRegisterGlobal.newCaisse = newCaisse;
    }

    public static int getErrorOnClose() {
        return errorOnClose;
    }

    public static void setErrorOnClose(int errorOnClose) {
        CashRegisterGlobal.errorOnClose = errorOnClose;
    }

    public static double getErrorAmount() {
        return errorAmount;
    }

    public static void setErrorAmount(double errorAmount) {
        CashRegisterGlobal.errorAmount = errorAmount;
    }

    public static int getNberOfCaissesWithSameDate() {
        return nberOfCaissesWithSameDate;
    }

    public static void setNberOfCaissesWithSameDate(int nberOfCaissesWithSameDate) {
        CashRegisterGlobal.nberOfCaissesWithSameDate = nberOfCaissesWithSameDate;
    }

    public static String getAvailableCaisseNumber() {
        return availableCaisseNumber;
    }

    public static void setAvailableCaisseNumber(String availableCaisseNumber) {
        CashRegisterGlobal.availableCaisseNumber = availableCaisseNumber;
    }

    public static ObservableList<Caisse> getCaisseList() {
        return caisseList;
    }

    public static void setCaisseList(ObservableList<Caisse> caisseList) {
        CashRegisterGlobal.caisseList = caisseList;
    }

    public static Caisse getPreviewCaisse() {
        return previewCaisse;
    }

    public static void setPreviewCaisse(Caisse previewCaisse) {
        CashRegisterGlobal.previewCaisse = previewCaisse;
    }

    public static double getCountCashResult() {
        return countCashResult;
    }

    public static void setCountCashResult(double countCashResult) {
        CashRegisterGlobal.countCashResult = countCashResult;
    }

    public static Cash getCaisseCash() {
        return caisseCash;
    }

    public static double getComputedSoldeCaisse() {
        return computedSoldeCaisse;
    }

    public static void setComputedSoldeCaisse(double computedSoldeCaisse) {
        CashRegisterGlobal.computedSoldeCaisse = computedSoldeCaisse;
    }

    public static void setCaisseCash(Cash caisseCash) {
        CashRegisterGlobal.caisseCash = caisseCash;
    }

    public static int getNberOfCaisses() {
        return nberOfCaisses;
    }

    public static void setNberOfCaisses(int nberOfCaisses) {
        CashRegisterGlobal.nberOfCaisses = nberOfCaisses;
    }

    public static Caisse getBeforeCurrentCaisse() {
        return beforeCurrentCaisse;
    }

    public static void setBeforeCurrentCaisse(Caisse beforeCurrentCaisse) {
        CashRegisterGlobal.beforeCurrentCaisse = beforeCurrentCaisse;
    }

    public static Caisse getCurrentCaisse() {
        return currentCaisse;
    }

    public static void setCurrentCaisse(Caisse currentCaisse) {
        CashRegisterGlobal.currentCaisse = currentCaisse;
    }

    //endregion
}
