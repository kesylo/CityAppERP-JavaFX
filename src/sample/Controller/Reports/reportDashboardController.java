package sample.Controller.Reports;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.Global;
import sample.Global.PlanningGlobal;
import sample.Model.Planning;
import sample.Model.Report;
import sample.Model.User;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

public class reportDashboardController {
    //region UI
    @FXML
    private JFXButton btnhide;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXComboBox<User> comboUser;

    @FXML
    private JFXComboBox<String> comboDept;

    @FXML
    private JFXRadioButton radioCollaborator;

    @FXML
    private JFXRadioButton radioDept;

    @FXML
    private JFXComboBox<String> comboMonth;

    @FXML
    private JFXComboBox<Integer> comboYear;

    @FXML
    private JFXRadioButton radioMonth;

    @FXML
    private JFXRadioButton radioYear;

    @FXML
    private JFXButton btnCalculate;

    @FXML
    private TableView<Report> tableDateHours;

    @FXML
    private TableColumn<Report, String> clmDate;

    @FXML
    private TableColumn<Report, String> clmShift;

    @FXML
    private Label hoursPrested;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;
    //endregion

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnBack);
    }

    private DBHandler dbHandler = new DBHandler();
    private DialogController<String> wd = null;
    private ObservableList<String> dept = FXCollections.observableArrayList();
    private ObservableList<String> month = FXCollections.observableArrayList();
    private ObservableList<Integer> year = FXCollections.observableArrayList();
    private ResultSet rs = null;

    @FXML
    void initialize() {

        Global.setUserProfile(lblConnectedUser, btnLogOut);

        // set profile photo
        Global.setProfileIcon(photo);

        loadUserList();

        fillComboBoxes();

        radioCollaborator.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                comboDept.setDisable(true);
                // what i need to be active
                comboUser.setDisable(false);
            }
        });

        radioDept.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                comboUser.setDisable(true);
                // what i need to be active
                comboDept.setDisable(false);
            }
        });

        radioMonth.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                // what i need to be active
                comboMonth.setDisable(false);
            }
        });

        radioYear.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                comboMonth.setDisable(true);
                // what i need to be active
                comboYear.setDisable(false);
            }
        });

        btnCalculate.setOnAction(event -> {
            showServices();
        });

        btnBack.setOnAction(event -> {
            URL url = getClass().getResource("/sample/View/dashboard.fxml");
            Global.navigateTo(url, "Dashboard");
        });
    }

    /* ==============================================================================================================================*/

    private void showServices() {
        if (radioCollaborator.isSelected() && radioMonth.isSelected()){
            fillTable("userMonth");
        }else if (radioCollaborator.isSelected() && radioYear.isSelected()){
            fillTable("userYear");
        }else if (radioDept.isSelected() && radioMonth.isSelected()){
            fillTable("deptMonth");
        }else if (radioDept.isSelected() && radioYear.isSelected()){
            fillTable("deptYear");
        }
    }

    private void loadUserList(){

        Platform.runLater(() ->{
            wd = new DialogController<>(btnBack.getScene().getWindow(), "Chargement des utilisateurs...");

            wd.exec("123", inputParam -> {

                ObservableList<User> userList = FXCollections.observableArrayList();
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
                    comboUser.setItems(userList);
                    comboUser.getSelectionModel().selectFirst();
                });

                return 1;
            });
        });
    }

    private void fillComboBoxes() {
        // dept
        dept.add("Front Office");
        dept.add("Back Office");
        dept.add("House Keeping");
        dept.add("Maintenance");
        comboDept.setItems(dept);
        comboDept.getSelectionModel().selectFirst();

        // month
        month.add("Janvier");
        month.add("Février");
        month.add("Mars");
        month.add("Avril");
        month.add("Mai");
        month.add("Juin");
        month.add("Juillet");
        month.add("Aout");
        month.add("Septembre");
        month.add("Octobre");
        month.add("Novembre");
        month.add("Decembre");
        comboMonth.setItems(month);
        comboMonth.getSelectionModel().selectFirst();

        // year
        LocalDate startDate = LocalDate.of(2015,1,1);
        LocalDate endDate = LocalDate.of(2030,1,1);
        List<LocalDate> dates = Global.getYearsBetween(startDate, endDate);

        for (LocalDate date : dates) {
            int intDate = date.getYear();
            year.add(intDate);
        }
        comboYear.setItems(year);
        comboYear.getSelectionModel().selectFirst();

    }

    /*private void getServices(String choice) {
        Platform.runLater(() ->{

            // prepare loading screen
            wd = new DialogController<>(btnBack.getScene().getWindow(), "Chargement des Prestations...");

            wd.exec("123", inputParam -> {

                ObservableList<Planning> data = FXCollections.observableArrayList();
                ObservableList<Report> reportList = FXCollections.observableArrayList();
                switch (choice) {
                    case "userMonth":
                        data = longTask("userMonth");
                        break;
                    case "userYear":
                        data = longTask("userYear");
                        break;
                    case "deptMonth":
                        data = longTask("deptMonth");
                        break;
                    case "deptYear":
                        data = longTask("deptYear");
                        break;
                }

                // set users Globally
                PlanningGlobal.setPlanningsList(data);
                for (Planning p : data) {

                    LocalTime startTime = LocalTime.parse(p.getStartTime());
                    LocalTime endTime = LocalTime.parse(p.getEndTime());

                    long min = MINUTES.between(startTime, endTime);

                    Report report = new Report(min + "", p.getPrestationDate());
                    reportList.add(report);
                }
                PlanningGlobal.setReportList(reportList);

                Platform.runLater(() ->{
                    fillTable(PlanningGlobal.getReportList());
                });
                return 1;
            });
        });

    }*/

    private void fillTable(String choice){
        ObservableList<Planning> data = FXCollections.observableArrayList();
        ObservableList<Report> reportList = FXCollections.observableArrayList();
        long min, totalMin=0;
        String totalTime;

        int selectedYear = comboYear.getValue();
        int seletedMonth = Global.monthToInt(comboMonth.getValue());
        int seletedUser = comboUser.getSelectionModel().getSelectedItem().getId();

        switch (choice) {
            case "userMonth":
                rs = dbHandler.getUserServicesInMonth(seletedUser,seletedMonth, selectedYear);
                break;
            case "userYear":
                //rs = dbHandler.getUserServicesInYear();
                break;
            case "deptMonth":
                //rs = dbHandler.getDeptServiceInMonth();
                break;
            case "deptYear":
                //rs = dbHandler.getDeptServiceInYear();
                break;
        }

        try {
            while (rs.next()){
                Planning planning = new Planning(
                        rs.getString("date"),
                        rs.getString("startTime"),
                        rs.getString("endTime")
                );
                data.add(planning);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Planning p : data) {

            LocalTime startTime = LocalTime.parse(p.getStartTime());
            LocalTime endTime = LocalTime.parse(p.getEndTime());

            min = MINUTES.between(startTime, endTime);

            totalMin += min;

            Report report = new Report(min + "", p.getPrestationDate());
            reportList.add(report);
        }


        clmDate.setCellValueFactory(new PropertyValueFactory<>("ServiceDate"));
        clmShift.setCellValueFactory(new PropertyValueFactory<>("Minutes"));

        // add list to table
        for (Report r :reportList) {
            r.setMinutes(Global.minutesToTime(Integer.valueOf(r.getMinutes())));
        }
        tableDateHours.setItems(reportList);

        // set text

        hoursPrested.setText(Global.minutesToTime(totalMin));

        // select first
        tableDateHours.getSelectionModel().selectFirst();
    }

    /*private ObservableList<Planning> longTask(String choice) {
        ObservableList<Planning> data = FXCollections.observableArrayList();
        int selectedYear = comboYear.getValue();
        int seletedMonth = Global.monthToInt(comboMonth.getValue());

        switch (choice) {
            case "userMonth":
                rs = dbHandler.getUserServicesInMonth(seletedMonth, selectedYear);
                break;
            case "userYear":
                //rs = dbHandler.getUserServicesInYear();
                break;
            case "deptMonth":
                //rs = dbHandler.getDeptServiceInMonth();
                break;
            case "deptYear":
                //rs = dbHandler.getDeptServiceInYear();
                break;
        }

        try {
            while (rs.next()){
                Planning planning = new Planning(
                        rs.getString("date"),
                        rs.getString("startTime"),
                        rs.getString("endTime")
                );
                data.add(planning);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }*/
}
