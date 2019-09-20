package sample.Controller;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.Window;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable, BasicSetup {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private JFXButton btnNavToCaisse;

    @FXML
    private Button btnnnnn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setUserProfile();

        btnNavToCaisse.setOnAction(event -> {
            goToWindow("/sample/View/CashRegister/cashDashboard.fxml");
        });
    }


    @FXML
    void logOut(MouseEvent event) {
        logOut();
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

    private void goToWindow(String windowPath) {
        // navigate to new screen
        //btnNavToCaisse.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(windowPath));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("City Appartements ERP");
        stage.show();

        // animate window
        new FadeIn(root).play();
    }

    @Override
    public void setUserProfile() {
        lblConnectedUser.setText(Global.getConnectedUserName());
        // set disconnect tooltip
        Tooltip.install(btnLogOut, new Tooltip("Déconnexion"));
    }

    public void logOut() {
        // get all windows and close
        List<Window> windows = Window.getWindows();
        for (int i = windows.size() - 1; i >= 0; i--) {
            if (windows.get(i).getTitle() == "City Appartements ERP") {
                windows.get(i).close();
            }
        }

        // load login scène
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/View/login.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("City Appartements ERP");
        stage.show();

        // navigate to new screen
        btnNavToCaisse.getScene().getWindow().hide();
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
