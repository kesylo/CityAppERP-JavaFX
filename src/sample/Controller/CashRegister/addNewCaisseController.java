package sample.Controller.CashRegister;

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

public class addNewCaisseController implements BasicSetup {
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
            createCaisse();
        });

        btnCancel.setOnAction(event -> {
            goToWindow("/sample/View/CashRegister/cashRegister.fxml");
        });
    }





    /*----------------------------------------------------------------------------------------------------------*/

    private void createCaisse() {
        DBHandler db = new DBHandler();
        db.createCaisse(LocalDate.now().toString(), getLastCaisseAmount());

        // go to next window
        goToWindow("/sample/View/CashRegister/addCash.fxml");
    }


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
    }

    @Override
    public void setUserProfile() {
        lblConnectedUser.setText(Global.getConnectedUserName());
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
