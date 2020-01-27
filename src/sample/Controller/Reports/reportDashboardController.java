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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.Global;
import sample.Model.Planning;
import sample.Model.Report;
import sample.Model.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Collections.*;

public class reportDashboardController {
    //region UI
    @FXML
    private JFXButton btnhide;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExport;

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
    ObservableList<Report> userReportList = FXCollections.observableArrayList();
    ObservableList<Planning> planningList = FXCollections.observableArrayList();
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

        btnExport.setOnAction(event -> {
            // check if calculate is pressed
            exportToExcel();
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

    private void exportToExcel() {

        try {
        // create excel sheet
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Timesheet de " +
                comboUser.getValue().getFirstName() + " " + comboUser.getValue().getLastName());
        XSSFRow header1 = sheet.createRow(0);
        header1.createCell(0).setCellValue("Nom :");
        header1.createCell(1).setCellValue("LOIC");
        header1.createCell(2).setCellValue("12.5");

        XSSFRow header2 = sheet.createRow(1);
        header2.setHeightInPoints(30);
        header2.createCell(0).setCellValue("Date");
        header2.createCell(1).setCellValue("Heure de début");
        header2.createCell(2).setCellValue("Heure de fin");
        header2.createCell(3).setCellValue("Pause");
        header2.createCell(4).setCellValue("Heures prestées");
        header2.createCell(5).setCellValue("Payts");
        header2.createCell(6).setCellValue("Heures normales");
        header2.createCell(7).setCellValue("Heures supplémentaires");
        header2.createCell(8).setCellValue("A Payer");
        header2.createCell(9).setCellValue("Report");
        header2.createCell(9).setCellValue("Solde");

        XSSFRow header3 = sheet.createRow(2);
        header3.createCell(1).setCellValue("Heures au format décimal");


        // write to file

            FileOutputStream fileOut = new FileOutputStream("test.xlsx");
            wb.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        /*for (Report r : userReportList){

        }*/
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
                    sort(userList);
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

    private void fillTable(String choice){
        //ObservableList<Planning> planningList = FXCollections.observableArrayList();
        ObservableList<Report> reportList = FXCollections.observableArrayList();
        ObservableList<Integer> usersInSameDept;

        long min, totalMin = 0;

        int selectedYear = comboYear.getValue();
        int selectedMonth = Global.monthToInt(comboMonth.getValue());
        String selectedService =comboDept.getValue();
        int selectedUser = comboUser.getSelectionModel().getSelectedItem().getId();

        switch (choice) {
            case "userMonth":
                rs = dbHandler.getUserServicesInMonth(selectedUser,selectedMonth, selectedYear);
                break;
            case "userYear":
                rs = dbHandler.getUserServicesInYear(selectedUser, selectedYear);
                break;
            case "deptMonth":
                usersInSameDept = getUsersIDInSameDept(selectedService);
                planningList = dbHandler.getDeptServiceInMonth(usersInSameDept,selectedMonth, selectedYear);
                break;
            case "deptYear":
                usersInSameDept = getUsersIDInSameDept(selectedService);
                planningList = dbHandler.getDeptServiceInYear(usersInSameDept, selectedYear);
                break;
        }

        if (choice.equals("userMonth") || choice.equals("userYear")){
            try {
                while (rs.next()){
                    Planning planning = new Planning(
                            rs.getString("date"),
                            rs.getString("startTime"),
                            rs.getString("endTime")
                    );
                    planningList.add(planning);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (Planning p : planningList) {

            if (!p.getStartTime().equals("") && !p.getEndTime().equals("")){

                //System.out.println(p.getStartTime() + " " + p.getEndTime());

                LocalTime startTime = LocalTime.parse(p.getStartTime());
                LocalTime endTime = LocalTime.parse(p.getEndTime());

                min = MINUTES.between(startTime, endTime);

                totalMin += min;

                Report report = new Report(min + "", p.getPrestationDate());
                reportList.add(report);

            }
        }

        // set locally for the excel method
        //planningList = planningsList;

        // add list to table
        for (Report r :reportList) {
            r.setMinutes(Global.minutesToTime(Integer.valueOf(r.getMinutes())));
        }

        // create sort by date
        clmDate.setSortType(TableColumn.SortType.ASCENDING);

        clmDate.setCellValueFactory(new PropertyValueFactory<>("ServiceDate"));
        clmShift.setCellValueFactory(new PropertyValueFactory<>("Minutes"));

        // set table data
        tableDateHours.setItems(reportList);

        // apply sort by date
        tableDateHours.getSortOrder().add(clmDate);

        // set text
        String totalTime = Global.minutesToTime(totalMin);
        hoursPrested.setText(totalTime);

        // select first
        tableDateHours.getSelectionModel().selectFirst();
    }

    private ObservableList<Integer> getUsersIDInSameDept(String selectedService) {

        ObservableList<Integer> usersInSameDept = FXCollections.observableArrayList();
        rs = dbHandler.getUserIDInSelectedDept(selectedService);

        try {
            while (rs.next()){
                usersInSameDept.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersInSameDept;
    }


}
