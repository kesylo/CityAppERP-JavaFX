package sample.Controller.Reports;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.CollaboratorGlobal;
import sample.Global.Global;
import sample.Model.CustomReport;
import sample.Model.PlanningReport;
import sample.Model.Report;
import sample.Model.User;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import static java.util.Collections.sort;

public class CashPostponeController {

    //region UI
    @FXML
    private JFXTextField txtReport;

    @FXML
    private JFXButton btnAddReport;

    @FXML
    private JFXComboBox<User> comboUser;

    @FXML
    private TableView<CustomReport> tableDateHours;

    @FXML
    private TableColumn<CustomReport, String> clmDate;

    @FXML
    private TableColumn<CustomReport, String> clmMontant;

    @FXML
    private TableColumn<CustomReport, String> clmUser;

    @FXML
    private TableColumn<CustomReport, String> clmStatus;
    //endregion

    @FXML
    void selectAllReport(MouseEvent event) {
        txtReport.selectAll();
    }

    public void clickItem() {
        tableDateHours.setOnMouseClicked(event1 -> {
            if (event1.getClickCount() == 2){
                CustomReport selectedReport = tableDateHours.getSelectionModel().getSelectedItem();

                if (!selectedReport.getStatus().equals("Payé")){
                    Boolean result = Global.showInfoMessageWithBtn("Voulez vous marquer le report du " +
                                    selectedReport.getDate() +
                                    " de " +
                                    selectedReport.getUserName() + " comme 'Payé' ?",
                            "",
                            "Oui",
                            "Non");

                    if (result){
                        updateReport(selectedReport.getIdCustomReport());
                    }
                }
            }
        });
    }

    private DBHandler dbHandler = new DBHandler();
    private DialogController<String> wd = null;
    private ResultSet rs = null;
    private ObservableList<Report> reportList = FXCollections.observableArrayList();
    private ObservableList<CustomReport> customReportList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    void initialize() {

        loadUserList();

        // fill table
        fillTable();

        // force double value
        Global.inputTextFormater(txtReport, 7,2, 3);

        btnAddReport.setOnAction(event -> {
            addReport();
        });

    }

    private User findUserInListById(int id, ObservableList<User> userList) {

        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    private void fillTable() {
        Platform.runLater(() ->{
            wd = new DialogController<>(btnAddReport.getScene().getWindow(), "chargement des reports...");

            wd.exec("123", inputParam -> {

                reportList = dbHandler.getAllReports();

                for (Report r : reportList){
                    CustomReport cReport = new CustomReport();

                    cReport.setIdCustomReport(r.getIdReport());
                    cReport.setReportAmount(r.getReportAmount());
                    cReport.setDate(r.getDate());

                    if (r.getStatus() == 0){
                        cReport.setStatus("Non payé");
                    }else {
                        cReport.setStatus("Payé");
                    }

                    // get user
                    User user = findUserInListById(r.getUserID(), userList);
                    if (user != null){
                        cReport.setUserName(user.getFirstName() + " " + user.getLastName());
                    }

                    customReportList.add(cReport);
                }

                return 1;
            });

            Platform.runLater(() ->{

                // create sort by date
                clmStatus.setSortType(TableColumn.SortType.ASCENDING);

                clmDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
                clmStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
                clmMontant.setCellValueFactory(new PropertyValueFactory<>("ReportAmount"));
                clmUser.setCellValueFactory(new PropertyValueFactory<>("UserName"));

                // set table data
                tableDateHours.setItems(customReportList);

                // apply sort by date
                tableDateHours.getSortOrder().add(clmStatus);

                // select first
                tableDateHours.getSelectionModel().selectFirst();

            });
        });
    }

    private void updateReport(int idCustomReport) {
        Platform.runLater(() -> {
            wd = new DialogController<>(btnAddReport.getScene().getWindow(), "Mise à jour...");

            wd.exec("1", inputParam -> {

                dbHandler.updateReport(idCustomReport);
                return 1;
            });

            updateTable();
        });
    }

    private void updateTable() {
        Global.showInfoMessage("Operation éffectuée!",
                "");

        wd = new DialogController<>(btnAddReport.getScene().getWindow(), "Chargement...");
        wd.exec("1", inputParam -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });

        tableDateHours.getItems().clear();
        fillTable();
    }

    private void addReport() {
        if (txtReport.getText().equals("") || Double.parseDouble(txtReport.getText()) < 0.1) {
            Global.showInfoMessage("Verifiez le montant du report",
                    "la valeur ne peux pas être vide!");
        }else {
            Report report = new Report(
                    1,
                    Double.parseDouble(txtReport.getText()),
                    Global.getSystemDateDMY(),
                    comboUser.getValue().getId(),
                    0
            );

            // save
            saveInDB(report);

            updateTable();
        }
    }

    private void saveInDB(Report report) {
        Platform.runLater(() -> {
            wd = new DialogController<>(btnAddReport.getScene().getWindow(), "Ajout du report...");

            wd.exec("1", inputParam -> {

                dbHandler.addReport(report);
                return 1;
            });


        });
    }

    private void loadUserList(){

        Platform.runLater(() ->{
            wd = new DialogController<>(btnAddReport.getScene().getWindow(), "Chargement des utilisateurs...");

            wd.exec("123", inputParam -> {

                ResultSet userRow = dbHandler.getActiveEmployees();

                try {
                    while (userRow.next()){
                        // create users list
                        User user = new User(
                                userRow.getInt("id"),
                                userRow.getString("address"),
                                userRow.getString("city"),
                                userRow.getString("CivilStatus"),
                                userRow.getString("dept"),
                                userRow.getString("email"),
                                userRow.getString("firstName"),
                                userRow.getInt("houseNum"),
                                userRow.getString("inService"),
                                userRow.getString("lastName"),
                                userRow.getString("letterBoxNum"),
                                userRow.getString("nationalRegisterNum"),
                                userRow.getString("outService"),
                                userRow.getString("password"),
                                userRow.getInt("postalCode"),
                                userRow.getString("pseudo"),
                                userRow.getString("sex"),
                                userRow.getInt("role"),
                                userRow.getString("phoneNumber"),
                                userRow.getDouble("salary1"),
                                userRow.getDouble("salary2"),
                                userRow.getString("status"),
                                userRow.getInt("employeeNumber"),
                                userRow.getString("birthday"),
                                userRow.getString("phoneCountry"),
                                userRow.getString("country"),
                                userRow.getString("iban")
                        );
                        userList.add(user);
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    sort(userList);
                    comboUser.setItems(userList);
                    comboUser.getSelectionModel().selectFirst();
                });

                return 1;
            });
        });
    }
}
