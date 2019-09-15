package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Controller.BasicSetup;
import sample.Controller.Global;
import sample.Database.DBHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CashRegisterController implements BasicSetup {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> listViewCaisse;

    @FXML
    private JFXButton btnFillCaisse;

    @FXML
    private JFXButton btnDetailCaisse;

    @FXML
    private Label lblConnectedUser;

    private DBHandler dbHandler;

    @FXML
    void logOut(MouseEvent event) {
        logOut();
    }

    @FXML
    void initialize() {

        // init DB access
        dbHandler = new DBHandler();

        // set user profile
        setUserProfile();

        // add dates to listview
        addDatesToListView();

        btnFillCaisse.setOnAction(event -> {
            goToWindow("/sample/View/CashRegister/addNewCaisse.fxml");
        });


        btnDetailCaisse.setOnAction(event -> {
            // Show details for specific date
            showDetailsByDate();

        });
    }


    /*----------------------------------------------------------------------------------------------*/


    private void showDetailsByDate() {
        // get selected item in listview
        String item = listViewCaisse.getSelectionModel().getSelectedItem();
        // get id for the selected date
        int id = dbHandler.getCaisseIdByDate(item);

        // get info for that id

        // open new windows with those infos
        goToWindow("/sample/View/CashRegister/addCash.fxml", true);
    }

    private void addDatesToListView() {
        ResultSet caisseRow = dbHandler.getAllFromCaisse();
        // create list
        ObservableList<String> data = FXCollections.observableArrayList();

        try {
            while (caisseRow.next()) {
                // add row element to data object
                // the 0 is to have a reverse list
                data.add(0, caisseRow.getString("date"));
                // add data object to listview as data source
                listViewCaisse.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // select first line by default
        listViewCaisse.getSelectionModel().select(0);
    }

    private void goToWindow(String windowPath) {
        // navigate to new screen
        btnFillCaisse.getScene().getWindow().hide();

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


    private void goToWindow(String windowPath, boolean disabled) {
        // navigate to new screen
        btnFillCaisse.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(windowPath));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // disable buttons on details mode
        if (disabled) {
            addCashController cash = loader.getController();
            cash.disableButtons();
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

    @Override
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
        btnFillCaisse.getScene().getWindow().hide();
    }
}
