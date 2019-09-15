package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.Window;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Controller.BasicSetup;
import sample.Controller.Global;

import java.io.IOException;
import java.util.List;

public class addCashController implements BasicSetup {
    @FXML
    private Label lblPageTitle;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private TableView<?> tableCaisseDetails;

    @FXML
    private TableView<?> tableCashCaisse;

    @FXML
    private JFXButton btnAddCash;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private JFXButton btneditCash;

    @FXML
    private JFXButton btnDeleteCash;

    @FXML
    private JFXButton btnCancel;


    @FXML
    void initialize() {

        // set user profile
        setUserProfile();

        btnCancel.setOnAction(event -> {
            goToWindow("/sample/View/CashRegister/cashRegister.fxml");
        });

    }

    /*------------------------------------------------------------------------------------------------*/

    public void signOut() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/View/CashRegister/cashRegister.fxml"));
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
        btnCancel.getScene().getWindow().hide();

    }

    private void goToWindow(String windowPath) {
        // navigate to new screen
        btnCancel.getScene().getWindow().hide();

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
    }


    public void disableButtons() {
        btnAddCash.setDisable(true);
        btnDeleteCash.setDisable(true);
        btneditCash.setDisable(true);
    }


    @Override
    public void setUserProfile() {
        lblConnectedUser.setText(Global.getConnectedUserName());
        // set disconnect tooltip
        Tooltip.install(btnLogOut, new Tooltip("Déconnexion"));
    }

    @FXML
    void logOut(MouseEvent event) {
        logOut();
    }

    @Override
    public void logOut() {
        // get all windows and close
        List<Window> windows = Window.getWindows();
        for (int i = windows.size() - 1; i >= 0; i--) {
            if (windows.get(i).getTitle() == "City Appartements ERP") {
                windows.get(i).close();
            }
        }

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
        btnCancel.getScene().getWindow().hide();
    }

}
