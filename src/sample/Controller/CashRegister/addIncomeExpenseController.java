package sample.Controller.CashRegister;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import sample.Controller.Global;
import sample.Database.DBHandler;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.jfoenix.validation.RegexValidator;
import sample.Model.CaisseIncExp;

public class addIncomeExpenseController {
    //region UI
    @FXML
    private JFXCheckBox ckcAddManyEntries;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private VBox vboxIncome;

    @FXML
    private VBox vboxExpense;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblShiftNum;

    @FXML
    private Label lblUser;

    @FXML
    private JFXRadioButton radioIncome;

    @FXML
    private ToggleGroup group;

    @FXML
    private JFXRadioButton radioExpense;

    @FXML
    private ComboBox<String> comboSource;

    @FXML
    private ComboBox<String> comboIncomeBank;

    @FXML
    private JFXTextField txtClientIndex;

    @FXML
    private JFXTextArea txtAreaCommentCaisse;

    @FXML
    private ComboBox<String> comboRaison;

    @FXML
    private ComboBox<String> comboExpenseBank;

    @FXML
    private ComboBox<String> comboSalaryBeneficial;

    @FXML
    private JFXTextArea txtAreaComment;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnCreate;

    @FXML
    private Label lblIndexIndicator;
    //endregion

    @FXML
    void selectText(MouseEvent event) {
        txtAmount.selectAll();
    }

    @FXML
    void onType(KeyEvent event) {

    }

    DBHandler dbHandler = new DBHandler();
    boolean isIncome = true;
    boolean isExpense = false;
    boolean isClientIdCorrtect = false;
    boolean isRegistrationComplete = false;
    Double amount = 0.0;

    @FXML
    void initialize() {
        loadHeader();

        loadBody();

        // force the field to be float only
        txtAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    txtAmount.setText(oldValue);
                }
            }
        });

        // validate client index
        RegexValidator val = new RegexValidator();

        txtClientIndex.getValidators().add(val);
        val.setRegexPattern("\\d{2}+[-+]\\d{4}+[-+]\\d{5}");
        val.setMessage("Verifiez le numero du client");
        txtClientIndex.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    isClientIdCorrtect = txtClientIndex.validate();
                }
            }
        });

        btnCancel.setOnAction(event -> {
            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.goToWindow(navPath, btnCreate,"Caisse", true);
        });

        btnCreate.setOnAction(event -> {
            // check if caisse opened before adding
            if (Global.getCurrentCaisse().getClosed() == 1){
                amount = Double.valueOf(txtAmount.getText());

                if (amount != null && amount != 0.0){
                    if (ckcAddManyEntries.isSelected()){
                        saveToDB();
                        if (isRegistrationComplete){
                            URL navPath = getClass().getResource("/sample/View/CashRegister/addIncomeExpense.fxml");
                            Global.goToWindow(navPath, btnCreate,"Actions", true);
                        }
                    } else {
                        saveToDB();
                        if (isRegistrationComplete){
                            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
                            Global.goToWindow(navPath, btnCreate,"Caisse", true);
                        }
                    }
                }
                else {
                    Global.showErrorMessage("Erreur sur le montant.",
                            "Le montant de l'opération n'est pas correct.");
                }
            }
            else {
                Global.showErrorMessage("Erreur sur la caisse. Contacter l'administrateur.",
                        "Cette caisse est déja fermée. Vous ne pouvez plus la modifier.");
            }

        });

    }
    /*----------------------------------------------------------------------------------*/

    private void saveToDB() {
        CaisseIncExp income;
        CaisseIncExp expense;

        if (isIncome){
            if (comboSource.getSelectionModel().getSelectedIndex() == 0){ // client
                if (isClientIdCorrtect){
                    income = new CaisseIncExp(amount,
                            Date.valueOf(LocalDate.now()),
                            Global.getSystemTime(),
                            Global.getConnectedUser().getId(),
                            "",
                            Global.getCurrentCaisse().getNumeroShift(),
                            Global.getCurrentCaisse().getId(),
                            "",
                            txtClientIndex.getText(),
                            0,
                            "");
                    // add to DB
                    dbHandler.addIncORExp(income);
                    isRegistrationComplete = true;
                }else {
                    Global.showInfoMessage(
                            "Verifiez le numero d'index.",
                            "Il doit etre au faormat: XX-XXXX-XXXXX");
                }
            }
            else if (comboSource.getSelectionModel().getSelectedIndex() == 1){ // bank
                income = new CaisseIncExp(amount,
                        Date.valueOf(LocalDate.now()),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        "",
                        Global.getCurrentCaisse().getNumeroShift(),
                        Global.getCurrentCaisse().getId(),
                        comboIncomeBank.getValue(),
                        "",
                        0,
                        "");
                // add to DB
                dbHandler.addIncORExp(income);
                isRegistrationComplete = true;
                System.out.println(income.toString());
            }
            else if (comboSource.getSelectionModel().getSelectedIndex() == 2){ // other

                income = new CaisseIncExp(amount,
                        Date.valueOf(LocalDate.now()),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        txtAreaCommentCaisse.getText(),
                        Global.getCurrentCaisse().getNumeroShift(),
                        Global.getCurrentCaisse().getId(),
                        "",
                        "",
                        0,
                        "");
                // add to DB
                dbHandler.addIncORExp(income);
                isRegistrationComplete = true;
            }
            else {
                Global.showErrorMessage("Veuillez remplir le formulaire",
                        "Les informations remplies ne sont pas complètes");
                isRegistrationComplete = false;
            }
        }

        if (isExpense) {
            if (comboRaison.getSelectionModel().getSelectedIndex() == 0) { // salary
                expense = new CaisseIncExp(amount,
                        Date.valueOf(LocalDate.now()),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        "",
                        Global.getCurrentCaisse().getNumeroShift(),
                        Global.getCurrentCaisse().getId(),
                        comboRaison.getValue(),
                        "",
                        1,
                        comboSalaryBeneficial.getValue());
                // add to DB
                dbHandler.addIncORExp(expense);
                isRegistrationComplete = true;
            }
            else if (comboRaison.getSelectionModel().getSelectedIndex() == 1){ // bank
                comboExpenseBank.getSelectionModel().selectFirst();

                expense = new CaisseIncExp(amount,
                        Date.valueOf(LocalDate.now()),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        "",
                        Global.getCurrentCaisse().getNumeroShift(),
                        Global.getCurrentCaisse().getId(),
                        comboExpenseBank.getValue(),
                        "",
                        1,
                        "");
                // add to DB
                dbHandler.addIncORExp(expense);
                isRegistrationComplete = true;

            }
            else if (comboRaison.getSelectionModel().getSelectedIndex() == 2){ // other
                expense = new CaisseIncExp(amount,
                        Date.valueOf(LocalDate.now()),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        txtAreaComment.getText(),
                        Global.getCurrentCaisse().getNumeroShift(),
                        Global.getCurrentCaisse().getId(),
                        comboRaison.getValue(),
                        "",
                        1,
                        "");
                // add to DB
                dbHandler.addIncORExp(expense);
                isRegistrationComplete = true;
            }
            else {
                Global.showErrorMessage("Veuillez remplir le formulaire",
                        "Les informations remplies ne sont pas complètes");
                isRegistrationComplete = true;
            }
        }
    }

    private void loadHeader() {
        lblDate.setText(Global.getCurrentCaisse().getDate().toString());
        lblShiftNum.setText(Global.getCurrentCaisse().getNumeroShift() + "");
        lblUser.setText(Global.getConnectedUser().getFirstName());
    }

    private void loadBody() {
        //region Income
        // provenance Income
        ObservableList<String> data = FXCollections.observableArrayList();
        data.add("Client");
        data.add("Transfert de la banque");
        data.add("Autre");
        comboSource.setItems(data);

        // bank Income
        ObservableList<String> dataBank = FXCollections.observableArrayList();
        dataBank.add("EPB");
        dataBank.add("KBC");
        comboIncomeBank.setItems(dataBank);
        //endregion

        //region Expense
        // reason expense
        ObservableList<String> dataExpense = FXCollections.observableArrayList();
        dataExpense.add("Salaire");
        dataExpense.add("Transfert vers la banque");
        dataExpense.add("Dépenses diverses");
        comboRaison.setItems(dataExpense);

        // bank Expense
        ObservableList<String> dataBankExpense = FXCollections.observableArrayList();
        dataBankExpense.add("EPB");
        dataBankExpense.add("KBC");
        comboExpenseBank.setItems(dataBankExpense);
        //endregion

        loadUserList();
    }

    public void loadUserList(){
        ResultSet userRow = dbHandler.getAllEmployeesNames();
        ObservableList<String> usersList = FXCollections.observableArrayList();
        String firstName;
        String lastName;
        String fullName;

        try {
            while (userRow.next()){
                firstName = userRow.getString("firstName");
                lastName = userRow.getString("lastName");
                fullName = firstName + " " + lastName;
                usersList.add(fullName);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        comboSalaryBeneficial.setItems(usersList);
    }

    public void comboBoxProvenance(ActionEvent event){
       if (comboSource.getSelectionModel().getSelectedIndex() == 0){ // client selected
           txtClientIndex.setDisable(false);
           comboIncomeBank.setDisable(true);
           txtAreaCommentCaisse.setDisable(true);
       }
       if (comboSource.getSelectionModel().getSelectedIndex() == 1){ // bank selected
           txtClientIndex.setDisable(true);
           comboIncomeBank.setDisable(false);
           comboIncomeBank.getSelectionModel().selectFirst();
           txtAreaCommentCaisse.setDisable(true);
       }
       if (comboSource.getSelectionModel().getSelectedIndex() == 2){ // other selected
           // reset index field
           txtClientIndex.clear();
           txtClientIndex.resetValidation();

           txtClientIndex.setDisable(true);
           comboIncomeBank.setDisable(true);
           txtAreaCommentCaisse.setDisable(false);
       }
   }

    public void comboBoxReason(ActionEvent event){
        if (comboRaison.getSelectionModel().getSelectedIndex() == 0){ // salary selected
            comboSalaryBeneficial.getSelectionModel().selectFirst();
            comboSalaryBeneficial.setDisable(false);
            comboExpenseBank.setDisable(true);
            txtAreaComment.setDisable(true);
        }
        if (comboRaison.getSelectionModel().getSelectedIndex() == 1){ // bank selected
            comboExpenseBank.getSelectionModel().selectFirst();
            comboSalaryBeneficial.setDisable(true);
            comboExpenseBank.setDisable(false);
            txtAreaComment.setDisable(true);
        }
        if (comboRaison.getSelectionModel().getSelectedIndex() == 2){ // other selected
            comboSalaryBeneficial.setDisable(true);
            comboExpenseBank.setDisable(true);
            txtAreaComment.setDisable(false);
        }
    }

    public void radioSelect(ActionEvent event){
        if (radioIncome.isSelected()){
            vboxIncome.setDisable(false);
            vboxExpense.setDisable(true);
            isIncome = true;
            isExpense = false;
        }
        if (radioExpense.isSelected()) {
            vboxIncome.setDisable(true);
            vboxExpense.setDisable(false);
            isIncome = false;
            isExpense = true;
        }
    }
}
