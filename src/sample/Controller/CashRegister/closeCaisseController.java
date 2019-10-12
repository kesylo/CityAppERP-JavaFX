package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import sample.Controller.DialogController;
import sample.Controller.Global;
import sample.Database.DBHandler;
import sample.Model.Caisse;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class closeCaisseController {

    //region UI
    @FXML
    private Label lblDate;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;

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

    private DialogController<String> wd;
    private DBHandler dbHandler;
    private double totalIncome;
    private double totalExpense;
    private double caisseFinalAmount;

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnBack);
    }

    @FXML
    void initialize() {
        initGlobal();

        // set profile photo
        Global.setProfileIcon(photo);

        // toggle count btn if count is done already
        toogleCountBtn();

        // set user profile
        Global.setUserProfile(lblConnectedUser, btnLogOut);

        loadData();

        btnCount.setOnAction(event -> {
            // tell count windows we are coming from close caisse for different behaviour
            Global.navFrom = "CloseCaisse";

            URL navPath = getClass().getResource("/sample/View/CashRegister/countCashCaisse.fxml");
            Global.navigateModal(navPath,"Comptage");
        });

        btnBack.setOnAction(event -> {
            // reset count cash result
            Global.setCountCashResult(0.0);

            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.navigateTo(navPath,"Caisse");
        });

        btnCloseCaisse.setOnAction(event -> {

            if (Global.getCaisseCash() != null){
                // close only if its open
                if (Global.getCurrentCaisse().getClosed() == 1){

                    // close the caisse
                    //Platform.runLater(this::);
                    closeCaisse();

                    // show notif
                    Global.successSystemNotif(
                            "La caisse a été fermée avec succès.",
                            "#f7a631"
                    );

                    // go back to dashboard
                    URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
                    Global.navigateTo(navPath,"Caisse");
                }
            }
            else {
                Global.showErrorMessage("Erreur lors de la ferméture.",
                        "Veuillez compter la caisse avant de la fermer.");
            }
        });
    }





    /*-------------------------------------------------------------------------------------------------------------*/

    private void initGlobal() {
        Global.navFrom = "";
        //Global.setComputedSoldeCaisse(0.0);
        // this is used to make sure count was used. so reset if this windows is opened
        /*Global.setCaisseCash(null);
        Global.setErrorAmount(0.0);
        Global.setCountCashResult(0.0);
        Global.setNewCaisse(new Caisse());
        Global.setIncExpError(null);*/


        wd = null;
        dbHandler = new DBHandler();
    }

    private void toogleCountBtn() {
        if (Global.getCaisseCash() == null){
            // count not done yet, show btn
            btnCount.setDisable(false);
            System.out.println("show");
        } else {
            // count done disable btn
            btnCount.setDisable(true);
            // set hint
            Tooltip.install(btnCount, new Tooltip("Le comptage est déja effectué"));
            System.out.println("not show");
        }
    }

    private void closeCaisse() {
        wd = new DialogController<>(btnBack.getScene().getWindow(), "Fermeture en Cours...");

        wd.exec("123", inputParam -> {

            DBHandler db = new DBHandler();
            // if caisse < 0 put 0 in database and add error
            if (caisseFinalAmount < 0){
                // addError
                Global.getCurrentCaisse().setError_amount(caisseFinalAmount);
                Global.getCurrentCaisse().setRemarque("Caisse avec solde négatif de " + caisseFinalAmount);
                Global.getCurrentCaisse().setHasError(1);

                db.updateCaisseStatus(0.0,
                        0,
                        Global.getSystemDateTime(),
                        Global.getCurrentCaisse(),
                        Global.getErrorAmount());

            } else {
                // no error found
                db.updateCaisseStatus(caisseFinalAmount,
                        0,
                        Global.getSystemDateTime(),
                        Global.getCurrentCaisse(),
                        Global.getErrorAmount());
                // add cash caisse
                db.addCaisseCash(Global.getCaisseCash());
                // if caisse has error, set error IncExp
                if (Global.getCurrentCaisse().getHasError() == 1){
                    db.addIncORExp(Global.getIncExpError());
                }
            }

            return 1;
        });
    }

    private void loadData() {

        Platform.runLater(() ->{
            wd = new DialogController<>(btnBack.getScene().getWindow(), "Chargement...");

            wd.exec("123", inputParam -> {

                // compute total income
                totalIncome = computeIncome();

                // compute total expenses
                totalExpense = computeExpense();

                caisseFinalAmount = computeFinalCaisseAmount();

                // set globally
                Global.setComputedSoldeCaisse(caisseFinalAmount);
                Global.getCurrentCaisse().setMontant(caisseFinalAmount);




                Platform.runLater(() ->{

                    lblDate.setText(Global.getCurrentCaisse().getDate());
                    lblShiftNum.setText(Global.getCurrentCaisse().getNumeroShift() + "");
                    lblComments.setText(Global.getCurrentCaisse().getRemarque());
                    lblEtat.setText("Ouverte");
                    lblTotalIncome.setText(Global.formatDouble(totalIncome) + " €");
                    lblTotalExpenses.setText(Global.formatDouble(totalExpense) + " €");
                    lblTotalCaisse.setText(Global.formatDouble(caisseFinalAmount) + " €");

                    // set count cash result
                    lblTotalCountAmount.setText(Global.roundDouble(Global.getCountCashResult()) + " €");

                });

                return 1;
            });
        });
    }

    private double computeFinalCaisseAmount(){
        // return balance
        double balance;
        if (Global.getNberOfCaisses() > 1 ){

            balance = Global.getBeforeCurrentCaisse().getMontant()
                    + totalIncome
                    - totalExpense
                    + Global.getCurrentCaisse().getError_amount();

        } else {
            balance = 0.0 + totalIncome - totalExpense + Global.getErrorAmount();
        }

        // if caisse already has error before close, compute balance differently
        if (Global.getCurrentCaisse().getHasError() == 1 && Global.getErrorOnClose() > 0){
            balance = Global.getCountCashResult();
        }

        // round up result
        balance = Global.roundDouble(balance);

        return balance;
    }

    private double computeExpense() {
        double totalExpense = 0.0;
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

    private double computeIncome() {
        double totalIncome = 0.0;
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
