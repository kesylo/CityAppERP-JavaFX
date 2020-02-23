package sample.Controller.Reports;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.Global;
import sample.Model.Caisse;
import sample.Model.CaisseIncExp;
import sample.Model.Payment;
import sample.Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

import static java.util.Collections.sort;

public class PaymentController {

    //region UI
    @FXML
    private JFXComboBox<User> comboUser;

    @FXML
    private JFXComboBox<String> comboMonth;

    @FXML
    private JFXComboBox<Integer> comboYear;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private JFXButton btnCalculate;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXRadioButton radioBank;

    @FXML
    private JFXRadioButton radioFromCaisse;

    @FXML
    private TableView<Payment> tablePayment;

    @FXML
    private TableColumn<Payment, String> clmUser;

    @FXML
    private TableColumn<Payment, Double> clmAmount;

    @FXML
    private TableColumn<Payment, String> clmDate;

    @FXML
    private TableColumn<Payment, String> clmDescription;

    @FXML
    private JFXButton btnDelete;
    //endregion

    private DBHandler dbHandler = new DBHandler();
    private boolean firstRun = true;
    private Caisse lastCaisse = new Caisse();
    private DialogController<String> wd = null;
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<Integer> year = FXCollections.observableArrayList();
    private ObservableList<Payment> userPaymentList = FXCollections.observableArrayList();

    @FXML
    void userCmbChange(ActionEvent event) {
        updateTable();
    }

    @FXML
    void initialize() {

        fillComboBox();

        // code to run async
        Platform.runLater(() ->{
            wd = new DialogController<>(btnCalculate.getScene().getWindow(), "Enrégistrement...");
            wd.exec("123", inputParam -> {

                loadUserList();

                lastCaisse = dbHandler.getLastCaisse();

                getAllPayments();

                fillTablePayments();


                return 1;
            });
        });

        Global.inputTextFormater(txtAmount, 10,2, 3);

        btnCalculate.setOnAction(event -> {

            if (txtAmount.getText().equals("") || Double.parseDouble(txtAmount.getText()) == 0
            || txtDescription.getText().equals("") || txtDescription.getText() == null)
            {
                Global.showErrorMessage(
                        "Erreur sur les valeurs entrées.",
                        "Le montant ne peut pas être égal à 0 et la description ne peut pas être vide");

            }else {
                if (radioBank.isSelected()){
                    savePayment();
                }

                if (radioFromCaisse.isSelected()){
                    addExpenseToCaisse();
                }
                updateTable();
            }
        });

        btnShow.setOnAction(event -> {
            updateTable();
        });

        btnDelete.setOnAction(event -> {
            boolean action = Global.showInfoMessageWithBtn(
                    "Suppréssion du paiement",
                    "Etes-vous sûr de vouloir supprimer ce paiement ?",
                    "Oui",
                    "Non");

            if (action){
                deletePayment();
            }
        });
    }

    private void deletePayment() {
        Platform.runLater(() ->{
            Payment selectedPayment = tablePayment.getSelectionModel().getSelectedItem();
            int idPayment = selectedPayment.getIdPayment();

            if (selectedPayment.getFrom() == 0){
                // from = 0 that meet data is from Recettes Table
                dbHandler.deleteInDB(idPayment, "caisse_recettes", "id_caisse_recettes");
            }else {
                // from = 1 that meet data is from Payment Table
                dbHandler.deleteInDB(idPayment, "payments", "idPayments");
            }

            updateTable();
        });
    }

    private void fillTablePayments() {

        Platform.runLater(() ->{
            // create sort by date
            clmDate.setSortType(TableColumn.SortType.ASCENDING);

            // set users names
            for (Payment p : userPaymentList){
                p.setUserName(getUserNameByIdInList(p.getUserId()));
            }

            clmDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
            clmUser.setCellValueFactory(new PropertyValueFactory<>("UserName"));
            clmAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            clmDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));

            if (userPaymentList.size() == 0 && !firstRun){
                Global.showInfoMessage("Pas de données",
                        "Pas de données à afficher pour le filtre sélectionné!");
            }

            // set table data
            tablePayment.setItems(userPaymentList);

            // apply sort by date
            tablePayment.getSortOrder().add(clmDate);

            // select first
            tablePayment.getSelectionModel().selectFirst();

            firstRun = false;
        });


    }

    private void updateTable() {
        tablePayment.getItems().clear();
        Platform.runLater(() ->{
            getAllPayments();
            fillTablePayments();
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

    private void fillComboBox() {
        // month
        ObservableList<String> month = FXCollections.observableArrayList();
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
        int currentMonth = LocalDate.now().getMonth().getValue();
        int index = 0;

        for (int i = 0; i< month.size(); i++){
            if (i == currentMonth -1){
                index = i;
            }
        }
        comboMonth.getSelectionModel().select(index);

        // year
        LocalDate startDate = LocalDate.of(2015,1,1);
        LocalDate endDate = LocalDate.of(2030,1,1);
        List<LocalDate> dates = Global.getYearsBetween(startDate, endDate);
        int currentYear = LocalDate.now().getYear();
        index = 0;
        int i = 0;

        for (LocalDate date : dates) {
            int intYear = date.getYear();
            year.add(intYear);
            if (intYear == currentYear){
                index = i;
            }
            i ++;
        }
        comboYear.setItems(year);
        comboYear.getSelectionModel().select(index);
    }

    private void getAllPayments() {
        Platform.runLater(() ->{
            User user = comboUser.getValue();
            int month = comboMonth.getSelectionModel().getSelectedIndex();
            month += 1;
            int year = comboYear.getValue();
            userPaymentList = dbHandler.getUserPayments(user, month, year);
        });
    }

    private void savePayment() {
        Payment formData = createOutput();
        User user = comboUser.getSelectionModel().getSelectedItem();

        Payment payment = new Payment(
                formData.getAmount(),
                formData.getDate(),
                user.getId(),
                formData.getDescription()
        );

        // run async
        wd = new DialogController<>(btnCalculate.getScene().getWindow(), "Enrégistrement...");
        wd.exec("123", inputParam -> {

            Platform.runLater(() ->{
                dbHandler.addPayment(payment);

                Global.showInfoMessage("Enrégistrement éffectué !",
                        "Le Paiement a été ajouté");

                Global.successSystemNotif("Enrégistrement éffectué !", "#f7a631");
            });
            return 1;
        });
    }

    private void addExpenseToCaisse() {

        if (lastCaisse.getClosed() == 1){
            Payment formData = createOutput();
            User user = comboUser.getSelectionModel().getSelectedItem();
            String name = user.getFirstName() + " " + user.getLastName();
            String upName = name.toUpperCase();
            CaisseIncExp expense = new CaisseIncExp(
                    formData.getAmount(),
                    formData.getDate(),
                    Global.getSystemTime(),
                    formData.getUserId(),
                    formData.getDescription(),
                    lastCaisse.getNumeroShift(),
                    lastCaisse.getId(),
                    "Salaire",
                    "",
                    1,
                    upName
            );

            // run async
            wd = new DialogController<>(btnCalculate.getScene().getWindow(), "Enrégistrement...");
            wd.exec("123", inputParam -> {

                Platform.runLater(() ->{
                    dbHandler.addIncORExp(expense);

                    Global.showInfoMessage("Enrégistrement éffectué !",
                            "Le Paiement a été ajouté à la caisse");

                    Global.successSystemNotif("Enrégistrement éffectué !", "#f7a631");
                });
                return 1;
            });
        } else {
            Global.showErrorMessage("Impossible d'ajouter cette dépense à la caisse.",
                    "La caisse est déja fermée!");
        }

    }

    private Payment createOutput() {
        String date;
        if (radioBank.isSelected()){
            date = Global.getSystemDateDMY();
        }else {
            date = Global.getSystemDateYMD();
        }
        double amount = 0;


        if (!txtDescription.getText().equals("") && txtDescription.getText() != null){
            if (txtAmount.getText().equals("") || txtAmount.getText() == null){
                amount = 0;
            } else {
                amount = Double.parseDouble(txtAmount.getText());
            }
        } else {
            Global.showErrorMessage("Veuillez remplir tous les champs",
                    "La case 'Description' ne peut pas être vide!");
        }

        return new Payment(
                amount,
                date,
                comboUser.getSelectionModel().getSelectedItem().getId(),
                txtDescription.getText()
        );
    }

    private void loadUserList(){

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

    }









}
