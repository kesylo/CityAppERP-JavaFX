package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sample.Controller.Global;
import sample.Controller.dialogController;
import sample.Database.DBHandler;
import sample.Model.Caisse;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class caisseDashboardController{

    //region UI

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private TableColumn<Caisse, String> clmDate;

    @FXML
    private TableColumn<Caisse, Integer> clmShift;

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
    private dialogController wd = null;

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

        // get all caisses
        getAllCaisses();

        btnCancel.setOnAction(event -> {
            btnCancel.getScene().getWindow().hide();
        });

        btnFillCaisse.setOnAction(event -> {
            initCaissesInfos();
        });

        btnDetailCaisse.setOnAction(event -> {

           if (Global.getNberOfCaisses() >= 1){
               // set the preview caisse globally
               Caisse previewCaisse = tableDateShifts.getSelectionModel().getSelectedItem();
               Global.setPreviewCaisse(previewCaisse);
               // open preview window
               URL navPath = getClass().getResource("/sample/View/CashRegister/detailsCaisse.fxml");
               Global.stayButGoToWindow(navPath,"Details");
           }
       });

        btnCloseCaisse.setOnAction(event -> {
           if (Global.getNberOfCaisses() >= 1){
               closeCaisse();
           }
       });

        btnRefresh.setOnAction(event -> {
           tableDateShifts.getItems().clear();
           getAllCaisses();
       });

        btnIncomeExpense.setOnAction(event -> {
           if (Global.getNberOfCaisses() >= 1){
               URL navPath = getClass().getResource("/sample/View/CashRegister/addIncomeExpense.fxml");
               Global.closeAndGoToWindow(navPath,"Actions");
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
                    "Etes-vous sûr de vouloir fermer la dernière caisse ?",
                    "Oui",
                    "Non");

            if (action){
                // go to fermeture
                URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
                Global.closeAndGoToWindow(navPath,"Fermeture");
            }
        }
    }

    private void initCaissesInfos() {
        if (Global.getNberOfCaisses() >= 1){
            if (Global.getCurrentCaisse().getClosed() == 0) {
                // 0 = closed
                // go to infos Caisse
                URL location = getClass().getResource("/sample/View/CashRegister/infosLastCaisse.fxml");
                Global.closeAndGoToWindow(location, "Recap");

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
            Global.closeAndGoToWindow(location, "Creation");
        }
    }

    private void getAllCaisses() {

        Platform.runLater(() ->{
            // prepare loading screen
            wd = new dialogController(btnFillCaisse.getScene().getWindow(), "Chargement...");

            wd.exec("123", inputParam -> {
                // run long longTask
                final ObservableList<Caisse> data = longTask();
                Global.setCaisseList(data);

                Platform.runLater(() ->{
                    fillTable(Global.getCaisseList());
                });

                return new Integer(1);
            });
        });
    }

    private ObservableList<Caisse> longTask() {
        ObservableList<Caisse> data = FXCollections.observableArrayList();

        ResultSet caisseRow = dbHandler.getAllFromCaisse();
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
        return data;
    }

    private void fillTable( ObservableList<Caisse> data) {

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
        tableDateShifts.getSelectionModel().selectFirst();
    }

}
