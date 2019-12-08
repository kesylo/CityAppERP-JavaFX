package sample.Controller.CashRegister;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import sample.Controller.DialogController;
import sample.Controller.Global.CashRegisterGlobal;
import sample.Controller.Global.Global;
import sample.Database.DBHandler;
import sample.Model.CaisseIncExp;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addIncomeExpenseController {
    //region UI
    @FXML
    private JFXCheckBox ckcAddManyEntries;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;

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

    //endregion

    @FXML
    void selectText() {
        txtAmount.selectAll();
    }

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnCreate);
    }


    private DBHandler dbHandler = new DBHandler();
    private boolean isIncome = true;
    private boolean isExpense = false;
    private DialogController<String> wd = null;
    private double amount = 0.0;

    @FXML
    void initialize() {
        // set profile photo
        Global.setProfileIcon(photo);

        // set user profile
        Global.setUserProfile(lblConnectedUser, btnLogOut);

        loadHeader();

        loadBody();

        // force the field to be float only
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([.]\\d{0,4})?")) {
                txtAmount.setText(oldValue);
            }
        });

        btnCancel.setOnAction(event -> {
            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.navigateTo(navPath,"Caisse");
        });

        btnCreate.setOnAction(event -> {

            // check if caisse opened before adding
            if (CashRegisterGlobal.getCurrentCaisse().getClosed() == 1) {

                // check if amount textbox is not empty
                if (!txtAmount.getText().isEmpty()){
                    // get amount from input text
                    amount = Double.valueOf(txtAmount.getText());
                    if (amount != 0.0){
                        // check if fields are correct
                        checkIncomePossibility();
                        checkExpensePossibility();
                    }else {
                        // amount = 0
                        // amount txt empty
                        Global.showErrorMessage(
                                "Erreur sur le montant entré.",
                                "Le montant ne peut pas être égal à 0.");
                    }
                }else {
                    // amount txt empty
                    Global.showErrorMessage(
                            "Erreur sur le montant entré.",
                            "Vérifiez le champ 'montant' pour continuer.");
                }
            }else {
                Global.showErrorMessage(
                        "Erreur sur la caisse. Contacter l'administrateur.",
                        "Cette caisse est déja fermée. Vous ne pouvez plus la modifier.");
            }
        });

    }

    /*----------------------------------------------------------------------------------*/

    private void loadHeader() {
        lblDate.setText(CashRegisterGlobal.getCurrentCaisse().getDate());
        lblShiftNum.setText(CashRegisterGlobal.getCurrentCaisse().getNumeroShift() + "");
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
        comboSource.getSelectionModel().selectFirst();
    }

    private void loadUserList(){

        Platform.runLater(() ->{
            wd = new DialogController<>(btnCreate.getScene().getWindow(), "Chargement des utilisateurs...");

            wd.exec("123", inputParam -> {

                ObservableList<String> usersList = FXCollections.observableArrayList();
                String firstName;
                String lastName;
                String fullName;
                ResultSet userRow = dbHandler.getActiveEmployees();

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

                Platform.runLater(() -> comboSalaryBeneficial.setItems(usersList));

                return 1;
            });
        });



    }

    public void comboBoxProvenance(){
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

    public void comboBoxReason(){
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

    public void radioSelect(){
        if (radioIncome.isSelected()){
            vboxIncome.setDisable(false);
            vboxExpense.setDisable(true);
            isIncome = true;
            isExpense = false;
            comboSource.getSelectionModel().selectFirst();
        }
        if (radioExpense.isSelected()) {
            vboxIncome.setDisable(true);
            vboxExpense.setDisable(false);
            isIncome = false;
            isExpense = true;
            comboRaison.getSelectionModel().selectFirst();
        }
    }

    private void checkExpensePossibility() {
        // Expense
        if (radioExpense.isSelected()){
            // salary selected
            if (comboRaison.getSelectionModel().getSelectedIndex() == 0){
                // check if checkbox is checked
                commitToDB();
            }
            // bank selected
            else if (comboRaison.getSelectionModel().getSelectedIndex() == 1){
                // check if checkbox is checked
                commitToDB();
            }
            // other selected
            else if (comboRaison.getSelectionModel().getSelectedIndex() == 2){
                // check if comment empty
                if (!txtAreaComment.getText().isEmpty()){
                    // check if checkbox is checked
                    commitToDB();
                }
                else {
                    // Comment is empty
                    Global.showErrorMessage(
                            "Erreur sur le commentaire",
                            "Le champ details ne peut pas être vide.");
                }
            }
        }
    }

    private void checkIncomePossibility() {

        // Income
        if (radioIncome.isSelected()){
            // Client selected
            if (comboSource.getSelectionModel().getSelectedIndex() == 0){
                // check if txtindex empty
                if (!txtClientIndex.getText().isEmpty()){
                    // check if client index is ok
                    if (txtClientIndex.getText().matches("\\d{3}+[-+]\\d{4}+[-+]\\d{5}")){
                        // check if checkbox is checked
                        commitToDB();
                    }
                    else {
                        // regex pattern not ok
                        Global.showErrorMessage(
                                "Erreur sur l'index du client",
                                "L'index doit être sous la forme: XXX-XXXX-XXXXX");
                    }
                }
                else {
                    // client index empty
                    Global.showErrorMessage(
                            "Erreur sur l'index du client",
                            "L'index du client ne peux pas être vide.");
                }
            }
            // bank selected
            else if (comboSource.getSelectionModel().getSelectedIndex() == 1){
                // check if checkbox is checked
                commitToDB();
            }
            // other selected
            else if (comboSource.getSelectionModel().getSelectedIndex() == 2){
                // check if comment empty
                if (!txtAreaCommentCaisse.getText().isEmpty()){
                    // check if checkbox is checked
                    commitToDB();
                }
                else {
                    // Comment is empty
                    Global.showErrorMessage(
                            "Erreur sur le commentaire",
                            "Le champ details ne peut pas être vide.");
                }
            }
        }
    }

    private void commitToDB() {
        if (ckcAddManyEntries.isSelected()){
            // form ok. save to db
            saveToDB();
            URL navPath = getClass().getResource("/sample/View/CashRegister/addIncomeExpense.fxml");
            Global.navigateTo(navPath,"Actions");
            // show notification
            Global.successSystemNotif(
                    "Opération éffectuée avec succès.",
                    "#f7a631");
        }else {
            saveToDB();
            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.navigateTo(navPath,"Caisse");
            // show notification
            Global.successSystemNotif(
                    "Opération éffectuée avec succès.",
                    "#f7a631");
        }
    }

    private void saveToDB() {
        CaisseIncExp income;
        CaisseIncExp expense;

        if (isIncome){
            if (comboSource.getSelectionModel().getSelectedIndex() == 0){ // client
                if (txtClientIndex.getText().matches("\\d{3}+[-+]\\d{4}+[-+]\\d{5}")){
                    income = new CaisseIncExp(amount,
                            Global.getSystemDate(),
                            Global.getSystemTime(),
                            Global.getConnectedUser().getId(),
                            "",
                            CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                            CashRegisterGlobal.getCurrentCaisse().getId(),
                            "",
                            txtClientIndex.getText(),
                            0,
                            "");
                    // add to DB
                    Platform.runLater(() -> dbHandler.addIncORExp(income));

                }
            }
            else if (comboSource.getSelectionModel().getSelectedIndex() == 1){ // bank
                income = new CaisseIncExp(amount,
                        Global.getSystemDate(),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        "",
                        CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                        CashRegisterGlobal.getCurrentCaisse().getId(),
                        comboIncomeBank.getValue(),
                        "",
                        0,
                        "");
                // add to DB
                Platform.runLater(() -> dbHandler.addIncORExp(income));
            }
            else if (comboSource.getSelectionModel().getSelectedIndex() == 2){ // other

                income = new CaisseIncExp(amount,
                        Global.getSystemDate(),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        txtAreaCommentCaisse.getText(),
                        CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                        CashRegisterGlobal.getCurrentCaisse().getId(),
                        "",
                        "",
                        0,
                        "");
                // add to DB
                Platform.runLater(() -> dbHandler.addIncORExp(income));
            }
            else {
                Global.showErrorMessage("Veuillez remplir le formulaire",
                        "Les informations remplies ne sont pas complètes");
            }
        }

        if (isExpense) {
            if (comboRaison.getSelectionModel().getSelectedIndex() == 0) { // salary
                expense = new CaisseIncExp(amount,
                        Global.getSystemDate(),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        "",
                        CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                        CashRegisterGlobal.getCurrentCaisse().getId(),
                        comboRaison.getValue(),
                        "",
                        1,
                        comboSalaryBeneficial.getValue());
                // add to DB
                Platform.runLater(() -> dbHandler.addIncORExp(expense));
            }
            else if (comboRaison.getSelectionModel().getSelectedIndex() == 1){ // bank
                comboExpenseBank.getSelectionModel().selectFirst();

                expense = new CaisseIncExp(amount,
                        Global.getSystemDate(),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        "",
                        CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                        CashRegisterGlobal.getCurrentCaisse().getId(),
                        comboExpenseBank.getValue(),
                        "",
                        1,
                        "");
                // add to DB
                Platform.runLater(() -> dbHandler.addIncORExp(expense));

            }
            else if (comboRaison.getSelectionModel().getSelectedIndex() == 2){ // other
                expense = new CaisseIncExp(amount,
                        Global.getSystemDate(),
                        Global.getSystemTime(),
                        Global.getConnectedUser().getId(),
                        txtAreaComment.getText(),
                        CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                        CashRegisterGlobal.getCurrentCaisse().getId(),
                        comboRaison.getValue(),
                        "",
                        1,
                        "");
                // add to DB
                Platform.runLater(() -> dbHandler.addIncORExp(expense));
            }
            else {
                Global.showErrorMessage("Veuillez remplir le formulaire",
                        "Les informations remplies ne sont pas complètes");
            }
        }
    }
}
