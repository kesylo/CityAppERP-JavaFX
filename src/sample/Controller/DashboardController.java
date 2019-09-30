package sample.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    //region UI
    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private JFXButton btnNavToCaisse;
    //endregion

    @FXML
    void logOut(MouseEvent event) {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnNavToCaisse);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // reset some values
        Global.navFrom = "";

        Global.setUserProfile(lblConnectedUser, btnLogOut);

        btnNavToCaisse.setOnAction(event -> {
            // nav to caisse when data are fetched
            URL toCaisse = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.closeAndGoToWindow(toCaisse, "Caisse");
        });
    }

    /*--------------------------------------------------------------------------------------*/

    private void manageRoles(int role) {
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
    }

    /*------------------------------------------ ROLES ----------------------------------------------*/
    // Role : 0
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
    }
}
