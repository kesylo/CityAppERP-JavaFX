package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Controller.Global;
import sample.Database.DBHandler;
import sample.Model.Caisse;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;

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
    private URL toCaisseDashboard = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");

    @FXML
    void initialize() {

        // get last caisse amount
        amountLastCaisse = db.getLastAmountCaisse();
        // init fields
        fillUiElmts();

        btnCreate.setOnAction(event -> {
            createCaisse();
        });

        btnCancel.setOnAction(event -> {
            Global.goToWindow(toCaisseDashboard, btnCreate,"Caisse", true);
        });
    }
    /*----------------------------------------------------------------------------------------------------------*/

    private void createCaisse() {
        // insert caisse in DB
        Caisse caisse = new Caisse();
        int caisseWithSameDate = db.getNbrCaisseWithSameDate(LocalDate.now().toString());

        if (caisseWithSameDate == 0){
            configureCaisse(caisse);
            // there is no shift at that date
            caisse.setNumeroShift(1);
            // create caisse
            db.createCaisse(caisse);
            // show succès message
            congrats();
            // go to dashboard
            Global.goToWindow(toCaisseDashboard, btnCreate,"Caisse", true);
        } else if (caisseWithSameDate == 1){
            configureCaisse(caisse);
            // there is 1 shift already
            caisse.setNumeroShift(2);
            db.createCaisse(caisse);
            congrats();
            Global.goToWindow(toCaisseDashboard, btnCreate,"Caisse", true);
        } else if (caisseWithSameDate == 2){
            configureCaisse(caisse);
            // there are 2 caisses already, create last
            caisse.setNumeroShift(3);
            db.createCaisse(caisse);
            congrats();
            Global.goToWindow(toCaisseDashboard, btnCreate,"Caisse", true);
        } else {
            // we can't create another caisse for this date
            Global.showErrorMessage("Erreur lors de la création de la caisse. Il existe deja 3 caisses pour la date d'aujourd'hui.",
                    "Chaque journée à droit à maximum 3 shifts.");
            Global.goToWindow(toCaisseDashboard, btnCreate,"Caisse", true);
        }
    }

    private void configureCaisse (Caisse caisse){

        caisse.setDate(Date.valueOf(LocalDate.now()));
        caisse.setMontant(amountLastCaisse);
        caisse.setRemarque("");
        caisse.setClosed(1);
        caisse.setIdEmployes(Global.getConnectedUser().getId());
    }

    private void fillUiElmts() {
        lblDate.setText(LocalDate.now().toString());
        lblMontant.setText(amountLastCaisse + " €");
        lblUser.setText(Global.getConnectedUser().getFirstName() + " " + Global.getConnectedUser().getLastName());
    }

    private void congrats(){
        Global.showInfoMessage("La caisse a été créee avec succès !",
                "Vous trouverez plus de détails sur le dashboard.");
    }
}
