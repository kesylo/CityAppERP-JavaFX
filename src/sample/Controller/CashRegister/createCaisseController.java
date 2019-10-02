package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Controller.Global;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Model.Caisse;

import java.net.URL;

public class createCaisseController{

    //region UI
    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnCreate;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblMontant;

    @FXML
    private Label lblNumeroShift;

    @FXML
    private Label lblUser;
    //endregion

    private DBHandler db = new DBHandler();
    private Double amountLastCaisse = 0.0;
    private DialogController wd = null;
    private URL toCaisseDashboard = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");

    @FXML
    void initialize() {

        // get last caisse amount
        /*amountLastCaisse = db.getLastAmountCaisse();*/
        if (Global.getNberOfCaisses() > 0){
            amountLastCaisse = Global.getCurrentCaisse().getMontant();
        }

        // init fields
        fillUiElmts();

        btnCreate.setOnAction(event -> {
            createCaisse();
        });

        btnCancel.setOnAction(event -> {
            Global.closeAndGoToWindow(toCaisseDashboard,"Caisse");
        });
    }
    /*----------------------------------------------------------------------------------------------------------*/

    private void createCaisse() {
        // insert caisse in DB
        Caisse caisse = new Caisse();

        if (Global.getNberOfCaissesWithSameDate() == 0){
            // there is no shift at that date, create first
            caisse.setNumeroShift(1);
        }else if (Global.getNberOfCaissesWithSameDate() == 1){
            // there 1 shift at that date, create second
            caisse.setNumeroShift(2);
        }
        else if (Global.getNberOfCaissesWithSameDate() == 2){
            // there 2 shift at that date, create third
            caisse.setNumeroShift(3);
        }

        configureCaisse(caisse);

        // in new thread
        sendDataToDB(caisse);
        // show success message
        congrats();
        // go to dashboard
        Global.closeAndGoToWindow(toCaisseDashboard,"Caisse");

    }

    private void sendDataToDB(Caisse caisse) {
        Platform.runLater(() ->{
            wd = new DialogController(btnCancel.getScene().getWindow(), "Chargement...");

            wd.exec("123", inputParam -> {
                db.createCaisse(caisse);

                return new Integer(1);
            });

        });

    }

    private void configureCaisse (Caisse caisse){

        caisse.setDate(Global.getSystemDate());
        caisse.setMontant(amountLastCaisse);
        caisse.setRemarque("");
        caisse.setClosed(1);
        caisse.setNumeroCaisse(Global.getAvailableCaisseNumber());
        caisse.setIdEmployes(Global.getConnectedUser().getId());
    }

    private void fillUiElmts() {
        lblDate.setText(Global.getSystemDate());
        lblMontant.setText(Global.formatDouble(amountLastCaisse) + " €");
        lblUser.setText(Global.getConnectedUser().getFirstName() + " " + Global.getConnectedUser().getLastName());
    }

    private void congrats(){
        Global.showInfoMessage("La caisse a été créee avec succès !",
                "Vous trouverez plus de détails sur le dashboard.");
    }
}
