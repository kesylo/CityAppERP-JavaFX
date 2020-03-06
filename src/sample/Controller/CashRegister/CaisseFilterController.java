//region Imports
package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.Global;
import sample.Model.Caisse;
import sample.Model.User;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.util.Collections.sort;
//endregion

public class CaisseFilterController {
    //region UI
    @FXML
    private JFXDatePicker datePickerFrom;

    @FXML
    private JFXDatePicker datePickerTo;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnCompute;

    @FXML
    private TableView<Caisse> tableCaisses;

    @FXML
    private TableColumn<Caisse, Integer> clmDayNbr;

    @FXML
    private TableColumn<Caisse, String> clmCaisseNbr;

    @FXML
    private TableColumn<Caisse, String> clmUser;

    @FXML
    private TableColumn<Caisse, String> clmDate;

    @FXML
    private TableColumn<Caisse, Double> clmIncome;

    @FXML
    private TableColumn<Caisse, Double> clmExpense;

    @FXML
    private TableColumn<Caisse, String> clmError;

    @FXML
    private TableColumn<Caisse, Double> clmBalance;

    @FXML
    private JFXButton btnExport;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;

    //-----------------------------
    @FXML
    private Label txtDateFrom;

    @FXML
    private Label txtDateTo;

    @FXML
    private Label txtIncome;

    @FXML
    private Label txtExpense;

    @FXML
    private Label txtError;

    @FXML
    private Label txtBalance;

    //endregion

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnBack);
    }

    @FXML
    void dateFromChange(ActionEvent event) {
        // check if dateFrom < dateTo
        if (datePickerFrom.getValue().isBefore(datePickerTo.getValue())){
            getCaisseBetween(datePickerFrom.getValue(), datePickerTo.getValue());
        }else {
            Global.showErrorMessage("Erreur sur les dates",
                    "La date de départ ne peut pas être avant la date de fin");
        }
    }

    @FXML
    void dateToChange(ActionEvent event) {
        // check if dateFrom < dateTo
        if (datePickerFrom.getValue().isBefore(datePickerTo.getValue())){
            getCaisseBetween(datePickerFrom.getValue(), datePickerTo.getValue());
        }else {
            Global.showErrorMessage("Erreur sur les dates",
                    "La date de départ ne peut pas être avant la date de fin");
        }
    }

    private DBHandler dbHandler = new DBHandler();
    private DialogController<String> wd = null;
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<Caisse> filteredCaisses = FXCollections.observableArrayList();
    private Double income = 0.0;
    private Double expense = 0.0;
    private Double balance = 0.0;
    private Double error = 0.0;

    @FXML
    void initialize() {
        Global.formatDatePicker(datePickerFrom);
        Global.formatDatePicker(datePickerTo);

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        datePickerFrom.setValue(yesterday);
        datePickerTo.setValue(today);

        loadUserList();

        Global.setUserProfile(lblConnectedUser, btnLogOut);
        // set profile photo
        Global.setProfileIcon(photo);

        Platform.runLater(() -> {
            // check if dateFrom < dateTo
            if (datePickerFrom.getValue().isBefore(datePickerTo.getValue())){
                getCaisseBetween(datePickerFrom.getValue(), datePickerTo.getValue());
            }else {
                Global.showErrorMessage("Erreur sur les dates",
                        "La date de départ ne peut pas être avant la date de fin");
            }
        });

        btnBack.setOnAction(event -> {
            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.navigateTo(navPath,"Caisse");
        });

        btnCompute.setOnAction(event -> {
            // check if dateFrom < dateTo
            if (datePickerFrom.getValue().isBefore(datePickerTo.getValue())){
                getCaisseBetween(datePickerFrom.getValue(), datePickerTo.getValue());
            }else {
                Global.showErrorMessage("Erreur sur les dates",
                        "La date de départ ne peut pas être avant la date de fin");
            }
        });
    }

    private String getUserNameByIdInList(int idUser) {
        for (User user : userList){
            if (user.getId() == idUser){
                return user.getFirstName() + " " + user.getLastName();
            }
        }
        return null;
    }

    private void fillTable() {
        // reset
        income = 0.0;
        expense = 0.0;
        error = 0.0;
        balance = 0.0;
        // set user names in caisse object
        for (Caisse c : filteredCaisses){
            c.setEmployeeName(getUserNameByIdInList(c.getIdEmployes()));
            // set day number
            LocalDate d = Global.stringToLocalDate(c.getDate());
            c.setDayNbr(Global.getDateNberInYear(d));

            // compute income
            income += c.getIncomes();

            // compute expense
            expense += c.getExpenses();

            // balance
            balance += c.getMontant();

            // error
            error += c.getError_amount();
        }

        clmDayNbr.setCellValueFactory(new PropertyValueFactory<>("DayNbr"));
        clmCaisseNbr.setCellValueFactory(new PropertyValueFactory<>("NumeroCaisse"));
        clmUser.setCellValueFactory(new PropertyValueFactory<>("EmployeeName"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmIncome.setCellValueFactory(new PropertyValueFactory<>("Incomes"));
        clmExpense.setCellValueFactory(new PropertyValueFactory<>("Expenses"));
        clmError.setCellValueFactory(new PropertyValueFactory<>("Error_amount"));
        clmBalance.setCellValueFactory(new PropertyValueFactory<>("Montant"));

        // set table data
        tableCaisses.setItems(filteredCaisses);

        // select first
        tableCaisses.getSelectionModel().selectFirst();

        setBottomText();
    }

    private void setBottomText() {
        txtDateFrom.setText(datePickerFrom.getValue().toString());
        txtDateTo.setText(datePickerTo.getValue().toString());
        txtIncome.setText(Global.formatDouble(income));
        txtExpense.setText(Global.formatDouble(expense));
        txtBalance.setText(Global.formatDouble(balance));
        txtError.setText(Global.formatDouble(error));
    }

    private void getCaisseBetween(LocalDate dateFrom, LocalDate dateTo) {

        wd = new DialogController<>(btnBack.getScene().getWindow(), "Chargement...");

        wd.exec("123", inputParam -> {

            filteredCaisses =  dbHandler.getCaisseBetween(dateFrom, dateTo);

            Platform.runLater(this::fillTable);
            return 1;
        });
    }

    private void loadUserList(){

        Platform.runLater(() ->{
            wd = new DialogController<>(btnBack.getScene().getWindow(), "Chargement des utilisateurs...");

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
                return 1;
            });
        });
    }
}
