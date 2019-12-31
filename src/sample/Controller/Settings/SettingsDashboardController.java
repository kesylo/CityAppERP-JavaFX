package sample.Controller.Settings;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Global.Global;
import java.net.URL;

public class SettingsDashboardController {
    //region UI

    @FXML
    private Label lblConnectedUser;

    @FXML
    private JFXButton btnRetour;

    @FXML
    private JFXButton btnhide;

    @FXML
    private Button btnNatChangeSvrAddress;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;

    @FXML
    private Button btnNavToPermissions;

    @FXML
    private Button btnNavToPlanning;
    //endregion

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnhide);
    }

    @FXML
    void initialize() {

        Global.setUserProfile(lblConnectedUser, btnLogOut);

        // set profile photo
        Global.setProfileIcon(photo);

        btnNavToPermissions.setOnAction(event -> {
            URL toEditSettings = getClass().getResource("/sample/View/Settings/editPermissions.fxml");
            Global.navigateModal(toEditSettings, "Droits");
        });

        btnRetour.setOnAction(event -> {
            URL url = getClass().getResource("/sample/View/dashboard.fxml");
            Global.navigateTo(url, "Dashboard");
        });

        btnNatChangeSvrAddress.setOnAction(event -> {
            URL url = getClass().getResource("/sample/View/Settings/editServerAddress.fxml");
            Global.navigateModal(url, "Adresse Serveur");
        });

    }
}
