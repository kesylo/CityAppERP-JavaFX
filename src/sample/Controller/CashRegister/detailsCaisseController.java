package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
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
import sample.Controller.Global;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Model.CaisseIncExp;
import sample.Model.Cash;
import sample.Model.User;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class detailsCaisseController {
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

    //region UI elements
    @FXML
    private Label lblConnectedUser;

    @FXML
    private JFXTextArea txtAreaCommentCaisse;

    @FXML
    private ImageView btnLogOut;

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
    private Label lblTotalExpenses;

    @FXML
    private Label lblDate;

    @FXML
    private JFXButton btnOk;

    @FXML
    private JFXButton btnRetour;
    //endregion

    private DBHandler dbHandler;
    private Double totalIncome = 0.0;
    private Double totalExpense = 0.0;
    private Double totalCash = 0.0;
    private Double balance = 0.0;
    private DialogController wd = null;

    @FXML
    void logOut(MouseEvent event) {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnOk);
    }

    @FXML
    void initialize() {

        // init DB access
        dbHandler = new DBHandler();

        // set headers
        fillHeader();

        // fill incomes
        getIncomeFromDB();

        // fill expenses
        getExpenseFromDB();

        // fill cash table
        getCashFromDB();


        btnRetour.setOnAction(event -> {
            // close window
            btnOk.getScene().getWindow().hide();
        });

        btnOk.setOnAction(event -> {
            // close window
            btnOk.getScene().getWindow().hide();

        });

    }

    //region Methods

    private void computeCaisse() {

        if (Global.getNberOfCaisses() >= 1){
            balance = 0.0
                    - totalExpense
                    + totalIncome;

            lblTotalCaisse.setText(Global.formatDouble(balance) + " €");

            if (Global.getNberOfCaisses() >= 2){
                // formula: Caisse before - expenses + incomes
                balance = Global.getBeforeCurrentCaisse().getMontant()
                        - totalExpense
                        + totalIncome;

                lblTotalCaisse.setText(Global.formatDouble(balance) + " €");
            }
        }
    }

    private void fillHeader() {

        // set date
        lblDate.setText(Global.getPreviewCaisse().getDate().toString());

        // set number shift
        lblShiftNumber.setText(Global.getPreviewCaisse().getNumeroShift() + "");

        // set created by
        Platform.runLater(() ->{
            wd = new DialogController(btnOk.getScene().getWindow(), "Chargement...");
            wd.exec("123", inputParam -> {

                Platform.runLater(() ->{
                    int idCreator = Global.getPreviewCaisse().getIdEmployes();
                    User currentUser = dbHandler.getEmployeObjByID(idCreator);
                    lblCaisseCreator.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
                });
                return new Integer(1);
            });
        });


        // set state
        if (Global.getPreviewCaisse().getClosed() == 0){
            lblCaisseStatus.setText("Fermée");
        }
        if (Global.getPreviewCaisse().getClosed() == 1){
            lblCaisseStatus.setText("Ouverte");
        }

        // set comment
        txtAreaCommentCaisse.setText(Global.getPreviewCaisse().getRemarque());
    }


    private void getCashFromDB() {
        // Create list data
        ObservableList<Cash> data = FXCollections.observableArrayList();

        Platform.runLater(() ->{
            wd = new DialogController(btnOk.getScene().getWindow(), "Chargement du cash...");

            wd.exec("123", inputParam -> {

                ResultSet rs = dbHandler.getCash(Global.getCurrentCaisse().getId(), Global.getCurrentCaisse().getNumeroShift());

                try {
                    while (rs.next()) {
                        Cash cash = new Cash(
                                rs.getInt("caisse_idCaisse"),
                                rs.getInt("numeroShift"),
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

                        // ui related element
                        Platform.runLater(() ->{
                            fillCashTab(data);
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return new Integer(1);
            });

        });


    }

    private void fillCashTab(ObservableList<Cash> data) {

        for (Cash d : data){
            totalCash = d.getLessThanFiftyCents()
                    + d.getFiftyCents() * 0.50
                    + d.getOneEuro()
                    + d.getTwoEuros() * 2
                    + d.getFiveEuros() * 5
                    + d.getTenEuros() * 10
                    + d.getTwentyEuros() * 20
                    + d.getFiftyEuros() * 50
                    + d.getOnehundredeuros() * 100
                    + d.getTwoHundredEuros() * 200;
        }

        lblTotalCash.setText(Global.formatDouble(totalCash) + " €");


        // link ui tabs to class methods
        clmLessThanOne.setCellValueFactory(new PropertyValueFactory<>("LessThanFiftyCents"));
        clmFiftyCents.setCellValueFactory(new PropertyValueFactory<>("FiftyCents"));
        clmOneEuro.setCellValueFactory(new PropertyValueFactory<>("OneEuro"));
        clmTwoEuros.setCellValueFactory(new PropertyValueFactory<>("TwoEuros"));
        clmFiveEuros.setCellValueFactory(new PropertyValueFactory<>("FiveEuros"));
        clmTenEuros.setCellValueFactory(new PropertyValueFactory<>("TenEuros"));
        clmTwentyEuros.setCellValueFactory(new PropertyValueFactory<>("TwentyEuros"));
        clmFiftyEuros.setCellValueFactory(new PropertyValueFactory<>("FiftyEuros"));
        clmOneHundredEuros.setCellValueFactory(new PropertyValueFactory<>("Onehundredeuros"));
        clmTwoHundredEuros.setCellValueFactory(new PropertyValueFactory<>("TwoHundredEuros"));

        // add list to table
        tableCash.setItems(data);

        // compute caisse
        computeCaisse();
    }

    private ObservableList<CaisseIncExp> getExpenseFromDB(){
        // Create list data
        ObservableList<CaisseIncExp> data = FXCollections.observableArrayList();

        Platform.runLater(() ->{
            wd = new DialogController(btnOk.getScene().getWindow(), "Chargement des depenses ...");

            wd.exec("123", inputParam -> {
                // get data from db
                ResultSet rs = dbHandler.getIncomeExpense(Global.getCurrentCaisse().getId(), Global.getCurrentCaisse().getNumeroShift(), 1);

                try {
                    while (rs.next()) {
                        CaisseIncExp expense = new CaisseIncExp(
                                rs.getDouble("montant"),
                                rs.getString("date"),
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
                    }

                    // ui related element
                    Platform.runLater(() ->{
                        fillExpenseTab(data);
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return new Integer(1);
            });
        });
        return data;
    }

    private void fillExpenseTab(ObservableList<CaisseIncExp> data) {

        for (int i=0; i < data.size(); i++){
            totalExpense += data.get(i).getAmount();
        }
        lblTotalExpenses.setText(Global.formatDouble(totalExpense) + " €");

        // link ui tabs to class methods
        clmCreationDateE.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        clmTimeE.setCellValueFactory(new PropertyValueFactory<>("Time"));
        clmAmountE.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        clmReasonE.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        clmCommentsE.setCellValueFactory(new PropertyValueFactory<>("Comment"));
        clmSalaryBeneficial.setCellValueFactory(new PropertyValueFactory<>("SalaryBeneficial"));

        // add list to table
        tableExpenses.setItems(data);

        // compute caisse
        computeCaisse();
    }

    private ObservableList<CaisseIncExp> getIncomeFromDB(){
        // Create list data
        ObservableList<CaisseIncExp> data = FXCollections.observableArrayList();

        Platform.runLater(() ->{
            wd = new DialogController(btnOk.getScene().getWindow(), "Chargement des recettes ...");

            wd.exec("123", inputParam -> {
                ResultSet rs = dbHandler.getIncomeExpense(Global.getCurrentCaisse().getId(), Global.getCurrentCaisse().getNumeroShift(), 0);
                //ResultSet rs = dbHandler.getIncomeExpense(0, 0, 1);

                try {
                    while (rs.next()) {
                        CaisseIncExp income = new CaisseIncExp(
                                rs.getDouble("montant"),
                                rs.getString("date"),
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
                    }
                    // ui related element
                    Platform.runLater(() ->{
                        fillIncomeTab(data);
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return new Integer(1);
            });
        });
        return data;
    }

    private void fillIncomeTab(ObservableList<CaisseIncExp> data) {
        for (int i=0; i < data.size(); i++){
            totalIncome += data.get(i).getAmount();
            lblTotalIncomes.setText(Global.formatDouble(totalIncome) + " €");
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

        // compute caisse
        computeCaisse();
    }

    //endregion
}