package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Controller.Global;
import sample.Database.DBHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class closeCaisseController {

    //region UI
    @FXML
    private Label lblDate;

    DBHandler dbHandler = new DBHandler();

    Double totalIncome = 0.0;

    Double totalExpense = 0.0;

    Double caisseFinalAmount = 0.0;

    @FXML
    private Label lblShiftNum;

    @FXML
    private Label lblComments;

    @FXML
    private Label lblEtat;

    @FXML
    private Label lblTotalIncome;

    @FXML
    private Label lblTotalExpenses;

    @FXML
    private Label lblTotalCountAmount;

    @FXML
    private Label lblTotalCaisse;

    @FXML
    private JFXButton btnCount;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnCloseCaisse;
    //endregion

    @FXML
    void initialize() {

        loadData();

        btnCount.setOnAction(event -> {
            // tell count windows we are coming from close caisse for different behaviour
            Global.navFrom = "CloseCaisse";

            URL navPath = getClass().getResource("/sample/View/CashRegister/countCashCaisse.fxml");
            Global.stayButGoToWindow(navPath,"Comptage");
        });

        btnBack.setOnAction(event -> {
            // reset count cash result
            Global.setCountCashResult(0.0);

            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.closeAndGoToWindow(navPath,"Caisse");
        });

        btnCloseCaisse.setOnAction(event -> {
            // reset count cash result
            Global.setCountCashResult(0.0);

            if (Global.getCaisseCash() != null){
                // close only if its open
                if (Global.getCurrentCaisse().getClosed() == 1){
                    closeCaisse();
                    Global.showInfoMessage(
                            "Action éffectuée.",
                            "La caisse a été fermée avec succès."
                    );
                }
                URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
                Global.closeAndGoToWindow(navPath,"Caisse");
            }
            else {
                Global.showErrorMessage("Erreur lors de la ferméture.",
                        "Veuillez compter la caisse avant de la fermer.");
            }
        });

    }

    /*-------------------------------------------------------------------------------------------------------------*/

    private void closeCaisse() {
        DBHandler db = new DBHandler();
        db.updateCaisseStatus(caisseFinalAmount,0, Global.getSystemDateTime(), Global.getCurrentCaisse());
        db.addCaisseCash(Global.getCaisseCash());
    }

    private void loadData() {
        // compute total income
        totalIncome = computeIncome();

        // compute total expenses
        totalExpense = computeExpense();

        caisseFinalAmount = computeFinalCaisseAmount();
        // set globally
        Global.setComputedSoldeCaisse(caisseFinalAmount);
        Global.getCurrentCaisse().setMontant(caisseFinalAmount);

        lblDate.setText(Global.getCurrentCaisse().getDate().toString());
        lblShiftNum.setText(Global.getCurrentCaisse().getNumeroShift() + "");
        lblComments.setText(Global.getCurrentCaisse().getRemarque());
        lblEtat.setText("Ouverte");
        lblTotalIncome.setText(totalIncome + " €");
        lblTotalExpenses.setText(totalExpense + " €");
        lblTotalCaisse.setText(caisseFinalAmount + " €");

        // set count cash result
        lblTotalCountAmount.setText(Global.getCountCashResult() + " €");
    }

    private Double computeFinalCaisseAmount(){
        // return balance
        // Last caisse solde + income - expenses
        if (Global.getNberOfCaisses() >= 2 ){
            return Global.getBeforeCurrentCaisse().getMontant() + totalIncome - totalExpense;
        }
        return 0.0 + totalIncome - totalExpense;
    }

    private Double computeExpense() {
        Double totalExpense = 0.0;
        ResultSet row = dbHandler.getIncomeExpense(Global.getCurrentCaisse().getId(),
                Global.getCurrentCaisse().getNumeroShift(),
                1);

        try {
            while (row.next()){
                totalExpense += row.getDouble("montant");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return totalExpense;
    }

    private Double computeIncome() {
        Double totalIncome = 0.0;
        ResultSet row = dbHandler.getIncomeExpense(Global.getCurrentCaisse().getId(),
                Global.getCurrentCaisse().getNumeroShift(),
                0);

        try {
            while (row.next()){
                totalIncome += row.getDouble("montant");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return totalIncome;
    }
}
