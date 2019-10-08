package sample.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
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
    private Button btnNavToCollaborators;
    //endregion

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


        btnNavToCaisse.setOnAction(event -> {
            URL toCaisse = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.navigateTo(toCaisse, "Caisse");
        });

        btnNavToCollaborators.setOnAction(event -> {
            URL toUsers = getClass().getResource("/sample/View/Collaborators/usersDashboard.fxml");
            Global.navigateTo(toUsers, "Collaborateurs");
        });


    }

    /*--------------------------------------------------------------------------------------*/

    /*private void manageRoles(int role) {
        switch (role) {
            case 0:
                basicUserRole();
                break;
            case 1:
                mediumUserRole();
                break;
            case 2:
                advancedUserRole();
                break;
            case 3:
                adminUserRole();
                break;
        }
    }*/

    private void setWelcomeText() {
        if (Global.getConnectedUser().getSex().equals("Male")){
            lblWelcome.setText("Bienvenu, " + Global.getConnectedUser().getLastName() + " !");
        }else {
            lblWelcome.setText("Bienvenue, " + Global.getConnectedUser().getLastName() + " !");
        }
    }

    /*------------------------------------------ ROLES ----------------------------------------------*/
/*    // Role : 0
    private void basicUserRole() {
        btnNavToCaisse.setDisable(true);
    }

    // Role : 1
    private void mediumUserRole() {
        btnNavToCaisse.setDisable(true);
    }

    // Role : 2
    private void advancedUserRole() {
        btnNavToCaisse.setDisable(false);
    }

    // Role : 3
    private void adminUserRole() {
        btnNavToCaisse.setDisable(false);
    }*/
}
