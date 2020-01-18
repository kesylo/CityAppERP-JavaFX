package sample.Controller.Procedure;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Global.FileBrowserGlobal;
import sample.Global.Global;

import javax.swing.*;
import java.net.URL;

public class ProcedureMainController {
    //region UI
    @FXML
    private Button btnNavToCleaning;

    @FXML
    private Button btnNavToCreditCard;

    @FXML
    private Button btnNavToChecking;

    @FXML
    private Label lblWelcome;

    @FXML
    private JFXButton btnhide;

    @FXML
    private JFXButton btnBack;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;
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

        btnNavToCleaning.setOnAction(event -> {
            // set cleaning folder globally
            String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
            FileBrowserGlobal.setExplorerBasePath(myDocumentsPath);

            FileBrowserGlobal.setCurrentProcedure("Procédures de nettoyage");
            URL url = getClass().getResource("/sample/View/FileBrowser/FileBrowser.fxml");
            Global.navigateNewWindow(url, "Néttoyage");
        });

        btnBack.setOnAction(event -> {
            URL url = getClass().getResource("/sample/View/dashboard.fxml");
            Global.navigateTo(url, "Dashboard");
        });
    }
}
