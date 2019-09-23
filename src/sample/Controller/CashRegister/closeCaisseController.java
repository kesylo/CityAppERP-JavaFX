package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Controller.Global;
import sample.Database.DBHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Label lblTotalCaisse;

    @FXML
    private JFXButton btnCount;

    @FXML
    private JFXButton btnCloseCaisse;
    //endregion

    @FXML
    void initialize() {

        loadData();

        btnCount.setOnAction(event -> {
            URL navPath = getClass().getResource("/sample/View/CashRegister/countCashCaisse.fxml");
            Global.goToWindow(navPath, btnCount,"Comptage", true);
        });

        btnCloseCaisse.setOnAction(event -> {
            // close only if its open
            if (Global.getCurrentCaisse().getClosed() == 1){
                closeCaisse();
                Global.showInfoMessage(
                        "Action éffectuée.",
                        "La caisse a été fermée avec succès."
                );
            }
            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.goToWindow(navPath, btnCount,"Caisse", true);
        });
    }


    /*-------------------------------------------------------------------------------------------------------------*/

    private void closeCaisse() {
        DBHandler db = new DBHandler();

        Date date = new Date(); // this object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        db.updateCaisseStatus(caisseFinalAmount,0, formatter.format(date), Global.getCurrentCaisse());
    }

    private void loadData() {
        // compute total income
        totalIncome = computeIncome();

        // compute total expenses
        totalExpense = computeExpense();

        lblDate.setText(Global.getCurrentCaisse().getDate().toString());
        lblShiftNum.setText(Global.getCurrentCaisse().getNumeroShift() + "");
        lblComments.setText(Global.getCurrentCaisse().getRemarque());
        lblEtat.setText("Ouverte");
        lblTotalIncome.setText(totalIncome + " €");
        lblTotalExpenses.setText(totalExpense + " €");
        lblTotalCaisse.setText(caisseFinalAmount + " €");
    }

    private Double computeFinalCaisseAmount(){
        // return balance
        // Last caisse solde + income - expenses
        return Global.getBeforeCurrentCaisse().getMontant() + totalIncome - totalExpense;
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
