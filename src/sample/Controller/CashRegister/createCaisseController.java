package sample.Controller.CashRegister;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sun.glass.ui.Window;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Controller.BasicSetup;
import sample.Controller.Global;
import sample.Database.DBHandler;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class createCaisseController implements BasicSetup {
    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXTextField txtAmountLastDay;

    @FXML
    private JFXTextField txtAmountCurrentDay;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnCreate;


    @FXML
    void initialize() {
        // set user profile
        setUserProfile();

        setCurrentDate();
        txtAmountLastDay.setText(getLastCaisseAmount().toString());
        datePicker.setEditable(false);

        btnCreate.setOnAction(event -> {
            //createCaisse();
            // go to next window
            goToWindow("/sample/View/CashRegister/addCash.fxml");

        });

        btnCancel.setOnAction(event -> {
            goToWindow("/sample/View/CashRegister/caisseDashboard.fxml");
        });
    }





    /*----------------------------------------------------------------------------------------------------------*/

    /*private void createCaisse() {
        DBHandler db = new DBHandler();
        Caisse caisse = new Caisse();

        // create sql date
        caisse.setDate(Date.valueOf(LocalDate.now()));
        caisse.setMontant(Float.parseFloat(txtAmountLastDay.getText()));
        caisse.setRemarque("");

        String strDate = "" + caisse.getDate() + "";

        // check if caisse already exists
        if (!db.checkIfDateExists(strDate)){
            // create the caisse
            db.createCaisse(caisse);

            // get id of last entered entry
            //Global.setCurrentCaisseId(db.getCaisseIdByDate(strDate));
        }else {
            Global.showErrorMessage (
                    "City App ERP",
                    "Erreur lors de la création d'une caisse",
                    "La date que vous avez entré existe déja. Vous ne pouvez pas créer 2 caisses dans une journée."
            );
        }
    }
*/

    private Float getLastCaisseAmount() {
        DBHandler db = new DBHandler();
        Float amount = 0f;
        ResultSet rs = db.getLastAmountCaisse();
        try {
            while (rs.next()) {
                amount = rs.getFloat("J_prec");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    private void setCurrentDate() {
        LocalDate maxDate = LocalDate.now();
        // set current date on DP
        datePicker.setValue(maxDate);
        // limit DP max choice to today's date

        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate));
                    }
                });
    }


    private void goToWindow(String windowPath) {
        // navigate to new screen
        btnCreate.getScene().getWindow().hide();

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
