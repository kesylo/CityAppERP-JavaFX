package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sample.Controller.Global;
import sample.Database.DBHandler;
import sample.Model.Caisse;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class caisseDashboardController{

    //region UI

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private TableColumn<Caisse, String> clmDate;

    @FXML
    private TableColumn<Caisse, Integer> clmShift;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private TableView<Caisse> tableDateShifts;

    @FXML
    private JFXButton btnCloseCaisse;

    @FXML
    private JFXButton btnFillCaisse;

    @FXML
    private JFXButton btnDetailCaisse;

    @FXML
    private Label lblConnectedUser;


    @FXML
    private JFXButton btnIncomeExpense;

    @FXML
    private AnchorPane anchorParent;
    //endregion

    private DBHandler dbHandler;

    @FXML
    void logOut(MouseEvent event) {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnFillCaisse);
    }

    @FXML
    void initialize() {
        // init global variables
        initGlobal();

        // init DB access
        dbHandler = new DBHandler();

        // set user profile
        Global.setUserProfile(lblConnectedUser, btnLogOut);

        // add dates to listview
        fillTable();

        btnFillCaisse.setOnAction(event -> {
            initCaissesInfos();
        });

       btnDetailCaisse.setOnAction(event -> {
           if (Global.getNberOfCaisses() >= 1){


               // code here
               URL navPath = getClass().getResource("/sample/View/CashRegister/detailsCaisse.fxml");
               Global.goToWindow(navPath, btnFillCaisse,"Details", false);

           }
       });

       btnCloseCaisse.setOnAction(event -> {
           if (Global.getNberOfCaisses() >= 1){
               closeCaisse();
           }
       });

       btnRefresh.setOnAction(event -> {
           tableDateShifts.getItems().clear();
           fillTable();
       });

       btnIncomeExpense.setOnAction(event -> {
           if (Global.getNberOfCaisses() >= 1){
               URL navPath = getClass().getResource("/sample/View/CashRegister/addIncomeExpense.fxml");
               Global.goToWindow(navPath, btnFillCaisse,"Actions", true);
           }
       });
    }



    /*----------------------------------------------------------------------------------------------*/
    private void initGlobal() {
        Global.navFrom = "";
        Global.setComputedSoldeCaisse(0.0);
        Global.setCaisseCash(null);
        Global.setCountCashResult(0.0);
    }

    private void closeCaisse() {
        // get last row in table
        Caisse lastCaisse = tableDateShifts.getItems().get(0);
        if (lastCaisse.getClosed() == 0){
            Global.showInfoMessage("Erreur lors de la fermeture de la caisse.",
                    "Cette caisse est deja fermée.");
        } else {
            // close caisse
            boolean action = Global.showInfoMessageWithBtn(
                    "Fermeture de la caisse",
                    "Etes-vous sûr de vouloir fermer cette caisse ?",
                    "Oui",
                    "Non");

            if (action){
                // go to fermeture
                URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
                Global.goToWindow(navPath, btnFillCaisse,"Fermeture", true);
            }
        }
    }

    private void initCaissesInfos() {
        if (Global.getNberOfCaisses() >= 1){
            if (Global.getCurrentCaisse().getClosed() == 0) {
                // 0 = closed
                // go to infos Caisse
                URL location = getClass().getResource("/sample/View/CashRegister/infosLastCaisse.fxml");
                Global.goToWindow(location, btnFillCaisse, "Recap", true);

            } else {
                // 1 = opened
                Global.showInfoMessage(
                        "Vérification du statut de la caisse.",
                        "La caisse précedente n'est pas encore fermée. Veuillez la fermer pour en créer une nouvelle."
                );
            }
        }else {
            // go to infos Caisse
            URL location = getClass().getResource("/sample/View/CashRegister/createCaisse.fxml");
            Global.goToWindow(location, btnFillCaisse, "Creation", true);
        }
    }

    private void fillTable() {
        ResultSet caisseRow = dbHandler.getAllFromCaisse();

        /*// Set table column names
        TableColumn date = new TableColumn("Date");
        TableColumn shift = new TableColumn("Shift");
        tableDateShifts.getColumns().addAll(date, shift);*/

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
                        caisseRow.getInt("employees_id"),
                        caisseRow.getString("date_fermeture"));
                data.add(0, caisse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*date.setCellValueFactory(new PropertyValueFactory<Caisse, String>("date"));
        shift.setCellValueFactory(new PropertyValueFactory<Caisse, String>("numeroShift"));*/

        clmDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        clmShift.setCellValueFactory(new PropertyValueFactory<>("NumeroShift"));

        tableDateShifts.setItems(data);

        // set table size globally for future tests:
        Global.setNberOfCaisses(data.size());

        if (data.size() >= 1){
            tableDateShifts.getSelectionModel().select(0);

            // get last row in table
            Caisse lastCaisse = tableDateShifts.getItems().get(0);

            // make last caisse global
            Global.setCurrentCaisse(lastCaisse);

            if (data.size() >= 2){
                // get before last row in table
                Caisse beforeLastCaisse = tableDateShifts.getItems().get(1);
                // make before last caisse global
                Global.setBeforeCurrentCaisse(beforeLastCaisse);
            }
        }
    }

}
