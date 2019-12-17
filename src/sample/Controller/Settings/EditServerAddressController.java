package sample.Controller.Settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.Global;

public class EditServerAddressController {

    //region UI

    @FXML
    private Label lblServerAdd;

    @FXML
    private JFXTextField txtServerAddress;

    @FXML
    private JFXButton btnSave;
    //endregion

    private DBHandler dbHandler;

    @FXML
    void initialize() {


        txtServerAddress.setOnKeyReleased(event -> {
            txtServerAddress.textProperty().addListener((observable, oldValue, newValue) -> {
                lblServerAdd.setText("http://" + newValue);
            });
        });

        btnSave.setOnAction(event -> {
            if (txtServerAddress.getText().isEmpty() || txtServerAddress.getText() == null){
                Global.showErrorMessage("Erreur", "L'adresse du serveur est incorrecte!");
            } else if (txtServerAddress.getText().contains("http")){
                Global.showErrorMessage("Erreur d'adresse", "L'adresse du serveur ne doit pas contenir: http://");
            } else {
                saveNewAddress(lblServerAdd.getText());
                btnSave.getScene().getWindow().hide();
            }
        });
    }

    private void saveNewAddress(String address) {

        dbHandler = new DBHandler();
        DialogController<String> wd = new DialogController<>(btnSave.getScene().getWindow(), "Mise à jour...");

        wd.exec("123", inputParam -> {

            // send to DB
            dbHandler.updateServerAddress(address);

            Platform.runLater(() ->{
                // show notification
                Global.successSystemNotif(
                        "Opération réussie!",
                        "#f7a631");
            });

            return 1;
        });
    }
}
