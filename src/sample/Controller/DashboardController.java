package sample.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Database.DBHandler;
import sample.Global.Global;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    //region UI
    @FXML
    private Label lblConnectedUser;

    @FXML
    private Label lblWelcome;

    @FXML
    private ImageView photo;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private JFXButton btnhide;

    @FXML
    private Button btnNavToCaisse;

    @FXML
    private Button btnNavToContracts;

    @FXML
    private Button btnNavToSettings;

    @FXML
    private Button btnNavToCollaborators;

    @FXML
    private Button btnNavToPlanning;

    @FXML
    private Button btnNavToReports;
    //endregion

    private DialogController<String> wd = null;
    private DBHandler dbHandler = new DBHandler();

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnhide);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // reset some values
        Global.navFrom = "";

        Global.setUserProfile(lblConnectedUser, btnLogOut);

        // set welcome text
        setWelcomeText();

        // set profile photo
        Global.setProfileIcon(photo);

        // load server settings
        getPlanningServerAddress();


        btnNavToCaisse.setOnAction(event -> {
            if (Global.getConnectedUser().getRole() > 2 ){
                URL toCaisse = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
                Global.navigateTo(toCaisse, "Caisse");
            }else {
                Global.showErrorMessage("Erreur de droits.",
                        "Vous n'avez pas le droit d'accéder à ce menu. Veuillez contacter l'administrateur.");
            }
        });

        btnNavToCollaborators.setOnAction(event -> {
            if (Global.getConnectedUser().getRole() > 3 ){
                URL toUsers = getClass().getResource("/sample/View/Collaborators/usersDashboard.fxml");
                Global.navigateTo(toUsers, "Collaborateurs");
            }else {
                Global.showErrorMessage("Erreur de droits.",
                        "Vous n'avez pas le droit d'accéder à ce menu. Veuillez contacter l'administrateur.");
            }
        });

        btnNavToPlanning.setOnAction(event -> {
            if (Global.getPlanningServerAddress() != null){
                if (!Global.openInBrowser(Global.getPlanningServerAddress())){
                    Global.showErrorMessage("Erreur", "Un problème est survenu lors de l'ouverture de l'application.");
                }
            }else {
                Global.showErrorMessage("Impossible de recupérer les informations du serveur",
                        "Veuillez vous reconnecter !");
            }
        });

        btnNavToContracts.setOnAction(event -> {
            if (Global.getConnectedUser().getRole() > 4 ){
                URL toContracts = getClass().getResource("/sample/View/Contracts/contractsDashboard.fxml");
                Global.navigateTo(toContracts, "Contrats");
            }else {
                Global.showErrorMessage("Erreur de droits.",
                        "Vous n'avez pas le droit d'accéder à ce menu. Veuillez contacter l'administrateur.");
            }
        });

        btnNavToSettings.setOnAction(event -> {
            if (Global.getConnectedUser().getRole() > 4 ){
                URL toSettings = getClass().getResource("/sample/View/Settings/settingsDashboard.fxml");
                Global.navigateTo(toSettings, "Settings");
            }else {
                Global.showErrorMessage("Erreur de droits.",
                        "Vous n'avez pas le droit d'accéder à ce menu. Veuillez contacter l'administrateur.");
            }
        });

        btnNavToReports.setOnAction(event -> {
            if (Global.getConnectedUser().getRole() > 1 ){
                URL toSettings = getClass().getResource("/sample/View/Reports/reportDashboard.fxml");
                Global.navigateTo(toSettings, "Rapports");
            }else {
                Global.showErrorMessage("Erreur de droits.",
                        "Vous n'avez pas le droit d'accéder à ce menu. Veuillez contacter l'administrateur.");
            }
        });

    }

    /*--------------------------------------------------------------------------------------*/

    private void getPlanningServerAddress(){

        final String[] address = {""};

        Platform.runLater(() ->{
            wd = new DialogController<>(btnhide.getScene().getWindow(), "Obtention du serveur...");

            wd.exec("123", inputParam -> {

                ResultSet row = dbHandler.getPlanningServerAdress();

                try {
                    while (row.next()) {
                        address[0] = row.getString("planningServerAddress");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() ->{

                    Global.setPlanningServerAddress(address[0]);

                });

                return 1;
            });
        });
    }

    private void setWelcomeText() {
        if (Global.getConnectedUser().getSex().equals("Male")){
            lblWelcome.setText("Bienvenu, " + Global.getConnectedUser().getFirstName() + " !");
        }else {
            lblWelcome.setText("Bienvenue, " + Global.getConnectedUser().getFirstName() + " !");
        }
    }
}
