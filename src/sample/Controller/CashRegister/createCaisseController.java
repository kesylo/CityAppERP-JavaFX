package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Global.CashRegisterGlobal;
import sample.Controller.DialogController;
import sample.Global.Global;
import sample.Database.DBHandler;
import sample.Model.Caisse;

import java.net.URL;

public class createCaisseController{

    //region UI
    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private ImageView photo;

    @FXML
    private JFXButton btnCreate;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblNumCaisse;

    @FXML
    private Label lblMontant;

    @FXML
    private Label lblUser;
    //endregion

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnCancel);
    }

    private DBHandler db = new DBHandler();
    private double amountNewCaisse;
    private DialogController<String> wd = null;
    private URL toCaisseDashboard = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");

    @FXML
    void initialize() {

        // get last caisse amount
        /*amountNewCaisse = db.getLastAmountCaisse();*/
        if (CashRegisterGlobal.getNberOfCaisses() > 0){
            amountNewCaisse = CashRegisterGlobal.getNewCaisse().getMontant();
        }

        // init fields
        fillUiElmts();

        btnCreate.setOnAction(event -> createCaisse());

        btnCancel.setOnAction(event -> Global.navigateTo(toCaisseDashboard,"Caisse"));
    }
    /*----------------------------------------------------------------------------------------------------------*/

    private void createCaisse() {
        // insert caisse in DB
        Caisse caisse = new Caisse();

        if (CashRegisterGlobal.getNberOfCaissesWithSameDate() == 0){
            // there is no shift at that date, create first
            caisse.setNumeroShift(1);
        }else if (CashRegisterGlobal.getNberOfCaissesWithSameDate() == 1){
            // there 1 shift at that date, create second
            caisse.setNumeroShift(2);
        }
        else if (CashRegisterGlobal.getNberOfCaissesWithSameDate() == 2){
            // there 2 shift at that date, create third
            caisse.setNumeroShift(3);
        }

        configureCaisse(caisse);

        // in new thread
        sendDataToDB(caisse);
        // show success message
        congrats();
        // go to dashboard
        Global.navigateTo(toCaisseDashboard,"Caisse");

    }

    private void sendDataToDB(Caisse caisse) {
        Platform.runLater(() ->{
            wd = new DialogController<>(btnCancel.getScene().getWindow(), "Chargement...");

            wd.exec("123", inputParam -> {
                db.createCaisse(caisse);

                return 1;
            });

        });

    }

    private void configureCaisse (Caisse caisse){

        caisse.setDate(Global.getSystemDateYMD());
        caisse.setMontant(amountNewCaisse);
        caisse.setRemarque("");
        caisse.setClosed(1);
        caisse.setHasError(CashRegisterGlobal.getNewCaisse().getHasError());
        caisse.setError_amount(CashRegisterGlobal.getErrorAmount());
        caisse.setNumeroCaisse(CashRegisterGlobal.getAvailableCaisseNumber());
        caisse.setIdEmployes(Global.getConnectedUser().getId());
    }

    private void fillUiElmts() {
        // set profile photo
        Global.setProfileIcon(photo);
        // set user profile
        Global.setUserProfile(lblConnectedUser, btnLogOut);
        lblDate.setText(Global.getSystemDateYMD());
        lblNumCaisse.setText(CashRegisterGlobal.getAvailableCaisseNumber());
        lblMontant.setText(Global.formatDouble(amountNewCaisse) + " €");
        lblUser.setText(Global.getConnectedUser().getFirstName() + " " + Global.getConnectedUser().getLastName());
    }

    private void congrats(){
        Global.showInfoMessage("La caisse a été créee avec succès !",
                "Vous trouverez plus de détails sur le dashboard.");
    }
}
