package sample.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.database.DBHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CashRegisterController {

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
    void initialize() {

        // init DB access
        dbHandler = new DBHandler();

        // add dates to listview
        addDatesToListView();

        btnFillCaisse.setOnAction(event -> {
            goToFillCaisse();
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
        System.out.println(item);

        ResultSet data = dbHandler.getInfosByDate(item);
    }

    private void addDatesToListView() {
        ResultSet caisseRow = dbHandler.getAllFromCaisse();
        // create list
        ObservableList<String> data = FXCollections.observableArrayList();

        try {
            while (caisseRow.next()) {
                System.out.println(caisseRow.getString("date"));
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

    private void goToFillCaisse() {
        // navigate to new screen
        btnFillCaisse.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/view/addCash.fxml"));
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
        stage.showAndWait();
    }


}
