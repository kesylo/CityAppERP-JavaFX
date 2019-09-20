package sample.Controller.CashRegister;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.sun.glass.ui.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Controller.BasicSetup;
import sample.Controller.Global;
import sample.Database.DBHandler;
import sample.Model.Cash;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class addCashController implements BasicSetup {
    @FXML
    private Label lblPageTitle;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private TableView<?> tableCaisseDetails;

    @FXML
    private TableView<Cash> tableCashCaisse;


    @FXML
    private JFXTextArea textAreaCommentaires;

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

        // load data from selected date
        LoadData();

        btnCancel.setOnAction(event -> {
            goToWindow("/sample/View/CashRegister/cashDashboard.fxml");
        });

        btnAddCash.setOnAction(event -> {
            // display cash form
            goToWindow("/sample/View/CashRegister/cashEntryForm.fxml");
        });

    }



    /*------------------------------------------------------------------------------------------------*/

    private void LoadData() {
        DBHandler db = new DBHandler();
        Date date = Global.getCurrentCaisse().getDate();
        int numShift = Global.getCurrentCaisse().getNumeroShift();
        // run query
        ResultSet rs = db.getCaisseDataByNumShiftAndDate(numShift, date);


        // Set table column names
        TableColumn lessThanFiftyCents = new TableColumn("> 0.50 €");
        TableColumn fiftyCents = new TableColumn("0.50 €");
        TableColumn oneEuro = new TableColumn("1 €");
        TableColumn twoEuros = new TableColumn("2 €");
        TableColumn fiveEuros = new TableColumn("5 €");
        TableColumn tenEuros = new TableColumn("10 €");
        TableColumn twentyEuros = new TableColumn("20 €");
        TableColumn fiftyEuros = new TableColumn("50 €");
        TableColumn oneHundrdEuros = new TableColumn("100 €");
        TableColumn twoHundredEuros = new TableColumn("200 €");

        // Create list data
        ObservableList<Cash> data = FXCollections.observableArrayList();

        try {
            while (rs.next()) {
                /*Cash mC = new Cash(
                        rs.getFloat("-1"),
                        rs.getInt("0.50"),
                        rs.getInt("1"),
                        rs.getInt("2"),
                        rs.getInt("5"),
                        rs.getInt("10"),
                        rs.getInt("20"),
                        rs.getInt("50"),
                        rs.getInt("100"),
                        rs.getInt("200")
                );*/

                //data.add(0,mC);
                // fill comment textArea
                textAreaCommentaires.setText(rs.getString("remarque"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void fillCommentary() {
        DBHandler db = new DBHandler();
        // fill everything from the id from caisse object
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
        // animate window
        new FadeIn(root).play();
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
