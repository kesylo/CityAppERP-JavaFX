package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class addCashController {

    @FXML
    private Label lblPageTitle;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXTextArea txtObservation;

    @FXML
    private JFXTextField txtAmountLastDay;

    @FXML
    private TableView<?> tableCaisseDetails;

    @FXML
    private TableView<?> tableCashCaisse;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnSave;

    @FXML
    void initialize() {

        btnCancel.setOnAction(event -> {
            signOut();
        });

    }

    /*------------------------------------------------------------------------------------------------*/

    public void signOut() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/view/cashRegister.fxml"));
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

    public void setPageTitle(String text) {
        lblPageTitle.setText(text);
    }

    public void disableUIElements() {
        btnSave.setDisable(true);
        txtObservation.setEditable(false);
        datePicker.setEditable(false);
        txtAmountLastDay.setEditable(false);

    }

}
