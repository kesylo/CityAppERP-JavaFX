package sample.Controller.CashRegister;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Controller.BasicSetup;
import sample.Controller.Global;
import sample.Database.DBHandler;
import sample.Model.Caisse;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class caisseDashboardController implements BasicSetup {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private TableView<Caisse> tableDateShifts;


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
        fillTable();

        btnFillCaisse.setOnAction(event -> {
            initCaissesInfos();
        });

        btnDetailCaisse.setOnAction(event -> {
           /* Caisse item = tableDateShifts.getSelectionModel().getSelectedItem();
            // store working date globally
            //Caisse curentCaisseDetails = new Caisse(item.getDate(), item.getNumeroShift());
            Global.setCurrentCaisse(curentCaisseDetails);
            if (Global.getRole() == 5){
                goToWindow("/sample/View/CashRegister/addCash.fxml");
            }else {
                goToWindow("/sample/View/CashRegister/addCash.fxml", true);
            }*/


        });
    }

    /*----------------------------------------------------------------------------------------------*/

    private void initCaissesInfos() {
        // get last row in table
        Caisse lastCaisse = tableDateShifts.getItems().get(0);

        // get before last row in table
        Caisse beforeLastCaisse = tableDateShifts.getItems().get(1);

            /*System.out.println(beforeLastCaisse.getDate());
            System.out.println(beforeLastCaisse.getClosed());
            System.out.println(beforeLastCaisse.getId());
            System.out.println(beforeLastCaisse.getNumeroShift());*/

        // make last caisse global
        Global.setCurrentCaisse(lastCaisse);

        // make before last caisse global
        Global.setBeforeCurrentCaisse(beforeLastCaisse);

        if (lastCaisse.getClosed() == 0) {
            // 0 = closed
            // go to infos Caisse
            goToWindow("/sample/View/CashRegister/infosLastCaisse.fxml");

        } else {
            // 1 = opened
            Global.showInfoMessage(
                    "City App ERP",
                    "Vérification du statut de la caisse.",
                    "La caisse précedente n'est pas encore fermée. Veuillez la fermer pour en créer une nouvelle."
            );
        }

            /*System.out.println(item.getDate());
            System.out.println(item.getNumeroShift());*/
        // goToWindow("/sample/View/CashRegister/createCaisse.fxml");
    }

    private void fillTable() {
        ResultSet caisseRow = dbHandler.getAllFromCaisse();

        // Set table column names
        TableColumn date = new TableColumn("Date");
        TableColumn shift = new TableColumn("Shift");
        tableDateShifts.getColumns().addAll(date, shift);

        // Create list data
        ObservableList<Caisse> data = FXCollections.observableArrayList();

        try {
            while (caisseRow.next()) {
                Caisse caisse = new Caisse(
                        caisseRow.getInt("idCaisse"),
                        caisseRow.getDate("date"),
                        caisseRow.getDouble("montant"),
                        caisseRow.getInt("numeroShift"),
                        caisseRow.getString("remarque"),
                        caisseRow.getInt("closed"),
                        caisseRow.getInt("employees_id"));
                data.add(0, caisse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        date.setCellValueFactory(new PropertyValueFactory<Caisse, String>("date"));
        shift.setCellValueFactory(new PropertyValueFactory<Caisse, String>("numeroShift"));

        tableDateShifts.setItems(data);
        tableDateShifts.getSelectionModel().select(0);
    }

   /* private void showDetailsByDate() {
        // get selected item in listview
        String item = listViewCaisse.getSelectionModel().getSelectedItem();
        // get id for the selected date
        int id = dbHandler.getCaisseIdByDate(item);

        // get info for that id

        // open new windows with those infos
        goToWindow("/sample/View/CashRegister/addCash.fxml", true);
    }*/

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
        // animate window
        new FadeIn(root).play();
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
        // animate window
        new FadeIn(root).play();
    }

    @Override
    public void setUserProfile() {
        lblConnectedUser.setText(Global.getConnectedUserName());
        // set disconnect tooltip
        Tooltip.install(btnLogOut, new Tooltip("Déconnexion"));
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
