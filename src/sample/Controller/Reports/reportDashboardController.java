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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.Global;
import sample.Model.Planning;
import sample.Model.Report;
import sample.Model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private JFXButton btnAddPayment;

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

        btnAddPayment.setOnAction(event -> {
            URL url = getClass().getResource("/sample/View/Reports/payment.fxml");
            Global.navigateModal(url, "Paiements");
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
            // variables
            Double totalWorked = 0.0;

            //region heading
            // create excel sheet
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Timesheet de LOIC" );
            sheet.setFitToPage(true);
            sheet.setHorizontallyCenter(true);


            // row yellow
            XSSFRow row1 = sheet.createRow(0);
            createCell(row1, wb, "Nom :", 0, IndexedColors.YELLOW.getIndex());
            drawBorders(0,0,0,0, BorderExtent.ALL, sheet);
            createCell(row1, wb, "LOIC", 1, IndexedColors.YELLOW.getIndex());
            drawBorders(0,0,1,5, BorderExtent.OUTSIDE, sheet);
            createCell(row1, wb, "12.0", 5, IndexedColors.YELLOW.getIndex());
            drawBorders(0,0,5,5, BorderExtent.ALL, sheet);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));

            // row green
            XSSFRow row2 = sheet.createRow(1);
            row2.setHeightInPoints(45);
            List<String> values = new ArrayList<>();
            values.add("Date");
            values.add("Heure de début");
            values.add("Heure de fin");
            values.add("Pause");
            values.add("Heures prestées");
            values.add("Payts");
            values.add("Heures normales");
            values.add("Heures supplémentaires");
            values.add("A Payer");
            values.add("Report");
            values.add("Solde");

            for (int i = 0; i < values.size(); i ++){
                createCell(row2, wb, values.get(i), i, IndexedColors.SEA_GREEN.getIndex());
            }
            drawBorders(1,1,0,10, BorderExtent.ALL, sheet);
            //endregion

            // data
            int indexStart = 3;
            int startDataRow = 3;
            for (Planning aPlanningList : planningList) {
                XSSFRow rowDates = sheet.createRow(indexStart);

                // dates
                createCell(rowDates, wb, aPlanningList.getPrestationDate(), 0, IndexedColors.GREY_25_PERCENT.getIndex());

                // start Time
                String oldString = aPlanningList.getStartTime();
                String newString = oldString.replace(":", ".");
                Double value1 = Double.parseDouble(newString);
                createCell(rowDates, wb, newString, 1, IndexedColors.WHITE.getIndex());

                // end Time
                String oldString2 = aPlanningList.getEndTime();
                String newString2 = oldString2.replace(":", ".");
                Double value2 = Double.parseDouble(newString2);
                createCell(rowDates, wb, newString2, 2, IndexedColors.WHITE.getIndex());

                // work time
                Double workTime = value2 - value1;
                createCell(rowDates, wb, workTime.toString(), 4, IndexedColors.WHITE.getIndex());

                // compute total time
                if (workTime > 0) {
                    totalWorked += workTime;
                }
                indexStart += 1;
            }
            drawBorders(startDataRow,indexStart - 1,0,4, BorderExtent.ALL, sheet);




            // row purple and green
            XSSFRow row3 = sheet.createRow(2);
            createCell(row3, wb, "", 1, IndexedColors.GREY_25_PERCENT.getIndex());
            drawBorders(2,2,0,0, BorderExtent.ALL, sheet);

            String value = "Heures au format décimal";
            createCell(row3, wb, value, 1, IndexedColors.ROSE.getIndex());
            drawBorders(2,2,1,3, BorderExtent.OUTSIDE, sheet);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));

            createCell(row3, wb, totalWorked.toString(), 4, IndexedColors.SEA_GREEN.getIndex());
            // payments from DB
            createCell(row3, wb, "00.00", 5, IndexedColors.SEA_GREEN.getIndex());
            // declared Hours from input
            createCell(row3, wb, "86.00", 6, IndexedColors.SKY_BLUE.getIndex());
            // extra hour (computed: worked Hours - declared hours)
            createCell(row3, wb, "20.75", 7, IndexedColors.SEA_GREEN.getIndex());
            // to pay (computed: salary * extra hour)
            createCell(row3, wb, "246.00", 8, IndexedColors.SEA_GREEN.getIndex());
            // report from DB
            createCell(row3, wb, "6.00", 9, IndexedColors.SKY_BLUE.getIndex());
            // balance computed
            createCell(row3, wb, "252.00", 10, IndexedColors.SEA_GREEN.getIndex());

            drawBorders(2,2,3,10, BorderExtent.ALL, sheet);

            // auto size columns
            for (int i = 0; i < 10; i++){
                sheet.autoSizeColumn(i);
            }

            // write to file
            FileOutputStream fileOut = new FileOutputStream("test.xlsx");
            wb.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCell(XSSFRow row, XSSFWorkbook wb, String value, int columnIndex, short color){

        Cell cell = row.createCell(columnIndex);

        if (Global.isStringNumeric(value)){
            double v = Double.parseDouble(value);
            cell.setCellValue(v);
        }else {
            cell.setCellValue(value);
        }


        // create it's style
        CellStyle style = wb.createCellStyle();

        // create font
        Font headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        style.setFont(headerFont);

        // add color
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // align
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);

        if (Global.isStringNumeric(value)){
            style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("00.00"));
        }

        style.setWrapText(true);

        // apply created style
        cell.setCellStyle(style);
    }

    private void drawBorders(int firstRow, int lastRow, int firstCol, int lastCol, BorderExtent type, XSSFSheet sheet){
        PropertyTemplate pt = new PropertyTemplate();
        pt.drawBorders(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol),
                BorderStyle.THIN, type);
        pt.applyBorders(sheet);
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
