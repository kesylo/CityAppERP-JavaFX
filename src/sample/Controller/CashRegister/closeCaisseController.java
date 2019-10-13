package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import sample.Controller.DialogController;
import sample.Controller.Global.CashRegisterGlobal;
import sample.Controller.Global.Global;
import sample.Database.DBHandler;

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
            CashRegisterGlobal.setCountCashResult(0.0);

            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.navigateTo(navPath,"Caisse");
        });

        btnCloseCaisse.setOnAction(event -> {

            if (CashRegisterGlobal.getCaisseCash() != null){
                // close only if its open
                if (CashRegisterGlobal.getCurrentCaisse().getClosed() == 1){

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
        //CashRegisterGlobal.setComputedSoldeCaisse(0.0);
        // this is used to make sure count was used. so reset if this windows is opened
        /*CashRegisterGlobal.setCaisseCash(null);
        CashRegisterGlobal.setErrorAmount(0.0);
        CashRegisterGlobal.setCountCashResult(0.0);
        CashRegisterGlobal.setNewCaisse(new Caisse());
        CashRegisterGlobal.setIncExpError(null);*/


        wd = null;
        dbHandler = new DBHandler();
    }

    private void toogleCountBtn() {
        if (CashRegisterGlobal.getCaisseCash() == null){
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
                CashRegisterGlobal.getCurrentCaisse().setError_amount(caisseFinalAmount);
                CashRegisterGlobal.getCurrentCaisse().setRemarque("Caisse avec solde négatif de " + caisseFinalAmount);
                CashRegisterGlobal.getCurrentCaisse().setHasError(1);

                db.updateCaisseStatus(0.0,
                        0,
                        Global.getSystemDateTime(),
                        CashRegisterGlobal.getCurrentCaisse(),
                        CashRegisterGlobal.getErrorAmount());

            } else {
                // no error found
                db.updateCaisseStatus(caisseFinalAmount,
                        0,
                        Global.getSystemDateTime(),
                        CashRegisterGlobal.getCurrentCaisse(),
                        CashRegisterGlobal.getErrorAmount());
                // add cash caisse
                db.addCaisseCash(CashRegisterGlobal.getCaisseCash());
                // if caisse has error, set error IncExp
                if (CashRegisterGlobal.getCurrentCaisse().getHasError() == 1){
                    db.addIncORExp(CashRegisterGlobal.getIncExpError());
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
                CashRegisterGlobal.setComputedSoldeCaisse(caisseFinalAmount);
                CashRegisterGlobal.getCurrentCaisse().setMontant(caisseFinalAmount);




                Platform.runLater(() ->{

                    lblDate.setText(CashRegisterGlobal.getCurrentCaisse().getDate());
                    lblShiftNum.setText(CashRegisterGlobal.getCurrentCaisse().getNumeroShift() + "");
                    lblComments.setText(CashRegisterGlobal.getCurrentCaisse().getRemarque());
                    lblEtat.setText("Ouverte");
                    lblTotalIncome.setText(Global.formatDouble(totalIncome) + " €");
                    lblTotalExpenses.setText(Global.formatDouble(totalExpense) + " €");
                    lblTotalCaisse.setText(Global.formatDouble(caisseFinalAmount) + " €");

                    // set count cash result
                    lblTotalCountAmount.setText(Global.roundDouble(CashRegisterGlobal.getCountCashResult()) + " €");

                });

                return 1;
            });
        });
    }

    private double computeFinalCaisseAmount(){
        // return balance
        double balance;
        if (CashRegisterGlobal.getNberOfCaisses() > 1 ){

            balance = CashRegisterGlobal.getBeforeCurrentCaisse().getMontant()
                    + totalIncome
                    - totalExpense
                    + CashRegisterGlobal.getCurrentCaisse().getError_amount();

        } else {
            balance = 0.0 + totalIncome - totalExpense + CashRegisterGlobal.getErrorAmount();
        }

        // if caisse already has error before close, compute balance differently
        if (CashRegisterGlobal.getCurrentCaisse().getHasError() == 1 && CashRegisterGlobal.getErrorOnClose() > 0){
            balance = CashRegisterGlobal.getCountCashResult();
        }

        // round up result
        balance = Global.roundDouble(balance);

        return balance;
    }

    private double computeExpense() {
        double totalExpense = 0.0;
        ResultSet row = dbHandler.getIncomeExpense(CashRegisterGlobal.getCurrentCaisse().getId(),
                CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
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
        ResultSet row = dbHandler.getIncomeExpense(CashRegisterGlobal.getCurrentCaisse().getId(),
                CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
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
