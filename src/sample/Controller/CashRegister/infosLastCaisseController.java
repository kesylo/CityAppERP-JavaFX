package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sample.Controller.Global;
import sample.Database.DBHandler;
import sample.Model.CaisseIncExp;
import sample.Model.Cash;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class infosLastCaisseController  {

    //region UI elements
    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private Label lblShiftNumber;

    @FXML
    private Label lblCaisseCreator;

    @FXML
    private Label lblCaisseStatus;

    @FXML
    private Label lblTotalIncomes;

    @FXML
    private Label lblTotalCaisse;

    @FXML
    private JFXButton btnOK;

    @FXML
    private Label lblTotalExpenses;

    @FXML
    private JFXButton btnRetour;
    //endregion

    //region Table Income
    @FXML
    private TableView<CaisseIncExp> tableIncomes;

    @FXML
    private TableColumn<CaisseIncExp, Date> clmCreationDate;

    @FXML
    private TableColumn<CaisseIncExp, String> clmTime;

    @FXML
    private TableColumn<CaisseIncExp, Double> clmAmount;

    @FXML
    private TableColumn<CaisseIncExp, String> clmReason;

    @FXML
    private TableColumn<CaisseIncExp, String> clmComments;

    @FXML
    private TableColumn<CaisseIncExp, Integer> clmIndex;
    //endregion

    //region Table Expense
    @FXML
    private TableView<CaisseIncExp> tableExpenses;

    @FXML
    private TableColumn<CaisseIncExp, Date> clmCreationDateE;

    @FXML
    private TableColumn<CaisseIncExp, String> clmTimeE;

    @FXML
    private TableColumn<CaisseIncExp, Double> clmAmountE;

    @FXML
    private TableColumn<CaisseIncExp, String> clmReasonE;

    @FXML
    private TableColumn<CaisseIncExp, String> clmSalaryBeneficial;

    @FXML
    private TableColumn<CaisseIncExp, String> clmCommentsE;
    //endregion

    //region Table Cash
    @FXML
    private TableView<Cash> tableCash;

    @FXML
    private TableColumn<Cash, Double> clmLessThanOne;

    @FXML
    private TableColumn<Cash, Integer> clmFiftyCents;

    @FXML
    private TableColumn<Cash, Integer> clmOneEuro;

    @FXML
    private TableColumn<Cash, Integer> clmTwoEuros;

    @FXML
    private TableColumn<Cash, Integer> clmFiveEuros;

    @FXML
    private TableColumn<Cash, Integer> clmTenEuros;

    @FXML
    private TableColumn<Cash, Integer> clmTwentyEuros;

    @FXML
    private TableColumn<Cash, Integer> clmFiftyEuros;

    @FXML
    private TableColumn<Cash, Integer> clmOneHundredEuros;

    @FXML
    private TableColumn<Cash, Integer> clmTwoHundredEuros;

    @FXML
    private Label lblTotalCash;
    //endregion

    DBHandler dbHandler;
    double totalCash = 0;
    double totalExpense = 0;
    double totalIncome = 0;

    @FXML
    void logOut(MouseEvent event) {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnOK);
    }

    @FXML
    void initialize() {
        // init DB access
        dbHandler = new DBHandler();

        Global.setUserProfile(lblConnectedUser, btnLogOut);

        // set caisse infos
        setCaisseInfos();

        btnRetour.setOnAction(event -> {
            btnRetour.getScene().getWindow().hide();
        });

        btnOK.setOnAction(event -> {
            boolean action = Global.showInfoMessageWithBtn(
                    "Verification des informations de caisse.",
                    "Les informations de caisse présentées sont elles égales à celles que vous avez dans votre caisse physique ?",
                    "Oui",
                    "Non");

            if (action){
                URL navPath = getClass().getResource("/sample/View/CashRegister/createCaisse.fxml");
                Global.goToWindow(navPath, btnRetour,"Creation", true);
            }else {
                URL navPath = getClass().getResource("/sample/View/CashRegister/countCashCaisse.fxml");
                Global.goToWindow(navPath, btnRetour,"Comptage", true);
            }
        });
    }

    /*---------------------------------------------------------------------------------------------------*/

    private void setCaisseInfos() {
        // set creator
        lblCaisseCreator.setText(getNameById(Global.getCurrentCaisse().getIdEmployes()));

        // set state
        if (Global.getCurrentCaisse().getClosed() == 0) {
            lblCaisseStatus.setText("Fermée");
        } else {
            lblCaisseStatus.setText("Ouverte");
        }

        // set shift
        lblShiftNumber.setText("" + Global.getCurrentCaisse().getNumeroShift());

        // set date
        datePicker.setValue(Global.getCurrentCaisse().getDate().toLocalDate());

        // fill incomes
        fillIncomeTab();

        // fill expenses
        fillExpenseTab();

        // fill cash table
        fillCashTab();

        // calculate caisse
        computeCaisse();
    }

    private void computeCaisse() {
        // formula: Caisse before - expenses + incomes
        Double balance = Global.getBeforeCurrentCaisse().getMontant()
                - totalExpense
                + totalIncome;

        lblTotalCaisse.setText("" + balance + " €");

        // add to global var
        Global.getCurrentCaisse().setMontant(balance);
        // add to DB ?

    }

    private void fillCashTab() {
        // Create list data
        ObservableList<Cash> data = FXCollections.observableArrayList();

        // get data from db
        ResultSet rs = dbHandler.getCash(Global.getCurrentCaisse().getId(), Global.getCurrentCaisse().getNumeroShift());
        //ResultSet rs = dbHandler.getCash(1, 1);

        try {
            while (rs.next()) {
                Cash cash = new Cash(
                        rs.getDouble("lessThanOneEuro"),
                        rs.getInt("fiftyCents"),
                        rs.getInt("oneEuro"),
                        rs.getInt("twoEuros"),
                        rs.getInt("fiveEuros"),
                        rs.getInt("tenEuros"),
                        rs.getInt("twentyEuros"),
                        rs.getInt("fiftyEuros"),
                        rs.getInt("oneHundredEuros"),
                        rs.getInt("twoHundredEuros")
                );

                data.add(cash);

                // calculate total
                totalCash = cash.getLessThanFiftyCents()
                        + cash.getFiftyCents() * 0.50
                        + cash.getOneEuro()
                        + cash.getTwoEuros() * 2
                        + cash.getFiveEuros() * 5
                        + cash.getTenEuros() * 10
                        + cash.getTwentyEuros() * 20
                        + cash.getFiftyEuros() * 50
                        + cash.getTwoHundredEuros() * 100
                        + cash.getTwoHundredEuros() * 200;

                lblTotalCash.setText("" + totalCash + " €");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // link ui tabs to class methods
        clmLessThanOne.setCellValueFactory(new PropertyValueFactory<>("LessThanFiftyCents"));
        clmFiftyCents.setCellValueFactory(new PropertyValueFactory<>("FiftyCents"));
        clmOneEuro.setCellValueFactory(new PropertyValueFactory<>("OneEuro"));
        clmTwoEuros.setCellValueFactory(new PropertyValueFactory<>("TwoEuros"));
        clmFiveEuros.setCellValueFactory(new PropertyValueFactory<>("FiveEuros"));
        clmTenEuros.setCellValueFactory(new PropertyValueFactory<>("TenEuros"));
        clmTwentyEuros.setCellValueFactory(new PropertyValueFactory<>("TwentyEuros"));
        clmFiftyEuros.setCellValueFactory(new PropertyValueFactory<>("FiftyEuros"));
        clmOneHundredEuros.setCellValueFactory(new PropertyValueFactory<>("OneHundredEuros"));
        clmTwoHundredEuros.setCellValueFactory(new PropertyValueFactory<>("TwoHundredEuros"));

        // add list to table
        tableCash.setItems(data);
    }

    private void fillExpenseTab() {
        // Create list data
        ObservableList<CaisseIncExp> data = FXCollections.observableArrayList();

        // get data from db
        ResultSet rs = dbHandler.getIncomeExpense(Global.getCurrentCaisse().getId(), Global.getCurrentCaisse().getNumeroShift(), 1);
        //ResultSet rs = dbHandler.getIncomeExpense(0, 0, 0);

        try {
            while (rs.next()) {
                CaisseIncExp expense = new CaisseIncExp(
                        rs.getDouble("montant"),
                        rs.getDate("date"),
                        rs.getString("time"),
                        rs.getInt("employees_id"),
                        rs.getString("remarque"),
                        rs.getInt("numeroShift"),
                        rs.getInt("fk_idCaisse"),
                        rs.getString("reason"),
                        rs.getString("indexClient"),
                        rs.getInt("type"),
                        rs.getString("salaryBeneficial")
                );

                data.add(expense);

                // calculate total
                totalExpense += expense.getAmount();
                lblTotalExpenses.setText(totalExpense + " €");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // link ui tabs to class methods
        clmCreationDateE.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        clmTimeE.setCellValueFactory(new PropertyValueFactory<>("Time"));
        clmAmountE.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        clmReasonE.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        clmCommentsE.setCellValueFactory(new PropertyValueFactory<>("Comment"));
        clmSalaryBeneficial.setCellValueFactory(new PropertyValueFactory<>("SalaryBeneficial"));

        // add list to table
        tableExpenses.setItems(data);
    }

    private void fillIncomeTab() {
        // Create list data
        ObservableList<CaisseIncExp> data = FXCollections.observableArrayList();

        // get data from db
        ResultSet rs = dbHandler.getIncomeExpense(Global.getCurrentCaisse().getId(), Global.getCurrentCaisse().getNumeroShift(), 0);
        //ResultSet rs = dbHandler.getIncomeExpense(0, 0, 1);

        try {
            while (rs.next()) {
                CaisseIncExp income = new CaisseIncExp(
                        rs.getDouble("montant"),
                        rs.getDate("date"),
                        rs.getString("time"),
                        rs.getInt("employees_id"),
                        rs.getString("remarque"),
                        rs.getInt("numeroShift"),
                        rs.getInt("fk_idCaisse"),
                        rs.getString("reason"),
                        rs.getString("indexClient"),
                        rs.getInt("type"),
                        rs.getString("salaryBeneficial")
                );

                data.add(income);

                // calculate total
                totalIncome += income.getAmount();
                lblTotalIncomes.setText(totalIncome + " €");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // link ui tabs to class methods
        clmCreationDate.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
        clmAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        clmReason.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        clmComments.setCellValueFactory(new PropertyValueFactory<>("Comment"));
        clmIndex.setCellValueFactory(new PropertyValueFactory<>("IndexClient"));

        // add list to table
        tableIncomes.setItems(data);
    }

    private String getNameById(int idEmployes) {
        ResultSet employeRow = dbHandler.getEmployeByID(idEmployes);
        String name = "";

        try {
            while (employeRow.next()) {
                String fName = employeRow.getString("firstName");
                String lName = employeRow.getString("lastName");
                name = fName + " " + lName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }

}
