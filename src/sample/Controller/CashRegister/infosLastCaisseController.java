package sample.Controller.CashRegister;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
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
import sample.Model.Cash;
import sample.Model.Expense;
import sample.Model.Income;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class infosLastCaisseController implements BasicSetup {

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
    private TableView<Income> tableIncomes;

    @FXML
    private TableColumn<Income, Date> clmCreationDate;

    @FXML
    private TableColumn<Income, String> clmTime;

    @FXML
    private TableColumn<Income, Double> clmAmount;

    @FXML
    private TableColumn<Income, String> clmReason;

    @FXML
    private TableColumn<Income, String> clmComments;

    @FXML
    private TableColumn<Income, Integer> clmIndex;
    //endregion

    //region Table Expense
    @FXML
    private TableView<Expense> tableExpenses;

    @FXML
    private TableColumn<Expense, Date> clmCreationDateE;

    @FXML
    private TableColumn<Expense, String> clmTimeE;

    @FXML
    private TableColumn<Expense, Double> clmAmountE;

    @FXML
    private TableColumn<Expense, String> clmReasonE;

    @FXML
    private TableColumn<Expense, String> clmSalaryBeneficial;

    @FXML
    private TableColumn<Expense, String> clmCommentsE;
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
        logOut();
    }

    @FXML
    void initialize() {
        // init DB access
        dbHandler = new DBHandler();

        // set caisse infos
        setCaisseInfos();

        btnRetour.setOnAction(event -> {
            goToWindow("/sample/View/CashRegister/caisseDashboard.fxml", true);
        });

        btnOK.setOnAction(event -> {
            boolean action = Global.showInfoMessageWithBtn(
                    "Verification des informations de caisse.",
                    "Les informations de caisse présentées sont elles égales à celles que vous avez dans votre caisse physique ?",
                    "Oui",
                    "Non");

            if (action){
                //System.out.println("ok");
                goToWindow("/sample/View/CashRegister/createCaisse.fxml", true);
            }else {
                //System.out.println("cancel");
                goToWindow("/sample/View/CashRegister/countCashCaisse.fxml", false);
            }
        });
    }



    /*---------------------------------------------------------------------------------------------------*/

    private void goToWindow(String windowPath, boolean closeCurrent) {
        // navigate to new screen
        if (closeCurrent){
            btnRetour.getScene().getWindow().hide();
        }

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
        //ResultSet rs = dbHandler.getCash(Global.getCurrentCaisse().getId(), Global.getCurrentCaisse().getNumeroShift());
        ResultSet rs = dbHandler.getCash(1, 1);

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
        ObservableList<Expense> data = FXCollections.observableArrayList();

        // get data from db
        //ResultSet rs = dbHandler.getIncomeExpense(Global.getCurrentCaisse().getId(), Global.getCurrentCaisse().getNumeroShift(), 1);
        ResultSet rs = dbHandler.getIncomeExpense(0, 0, 0);

        try {
            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getDouble("montant"),
                        rs.getDate("date"),
                        rs.getString("time"),
                        rs.getString("remarque"),
                        rs.getString("reason"),
                        rs.getInt("type"),
                        rs.getString("salaryBeneficial"));

                data.add(expense);

                // calculate total
                totalExpense += expense.getAmount();
                lblTotalExpenses.setText("- " + totalExpense + " €");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // link ui tabs to class methods
        clmCreationDateE.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        clmTimeE.setCellValueFactory(new PropertyValueFactory<>("Time"));
        clmAmountE.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        clmReasonE.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        clmCommentsE.setCellValueFactory(new PropertyValueFactory<>("MoreInfos"));
        clmSalaryBeneficial.setCellValueFactory(new PropertyValueFactory<>("SalaryBeneficial"));

        // add list to table
        tableExpenses.setItems(data);
    }

    private void fillIncomeTab() {
        // Create list data
        ObservableList<Income> data = FXCollections.observableArrayList();

        // get data from db
        //ResultSet rs = dbHandler.getIncomeExpense(Global.getCurrentCaisse().getId(), Global.getCurrentCaisse().getNumeroShift(), 1);
        ResultSet rs = dbHandler.getIncomeExpense(0, 0, 1);

        try {
            while (rs.next()) {
                Income income = new Income(
                        rs.getInt("type"),
                        rs.getDouble("montant"),
                        rs.getDate("date"),
                        rs.getString("time"),
                        rs.getString("remarque"),
                        rs.getString("reason"),
                        rs.getString("indexClient"));

                data.add(income);

                // calculate total
                totalIncome += income.getAmount();
                lblTotalIncomes.setText("+ " + totalIncome + " €");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // link ui tabs to class methods
        clmCreationDate.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
        clmAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        clmReason.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        clmComments.setCellValueFactory(new PropertyValueFactory<>("MoreInfos"));
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
        btnOK.getScene().getWindow().hide();
    }
}
