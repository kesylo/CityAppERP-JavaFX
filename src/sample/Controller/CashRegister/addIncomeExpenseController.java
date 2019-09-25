package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import com.jfoenix.validation.RegexValidator;

public class addIncomeExpenseController {
    //region UI

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
        /*if (txtClientIndex.getText().matches("\\d{2}+[-+]\\d{4}+[-+]\\d{5}")){
            System.out.println("ok");
        }*/

    }

    DBHandler dbHandler = new DBHandler();

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
                    txtClientIndex.validate();
                }
            }
        });

        // match client index
        /*txtClientIndex.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d{2}+[-+]\\d{4}+[-+]\\d{5}")) {
                    System.out.println("bad");
                }
            }
        });*/



        btnCancel.setOnAction(event -> {
            URL navPath = getClass().getResource("/sample/View/CashRegister/caisseDashboard.fxml");
            Global.goToWindow(navPath, btnCreate,"Caisse", true);
        });

    }

    /*----------------------------------------------------------------------------------*/

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
           txtAreaCommentCaisse.setDisable(true);
       }
       if (comboSource.getSelectionModel().getSelectedIndex() == 2){ // other selected
           txtClientIndex.setDisable(true);
           comboIncomeBank.setDisable(true);
           txtAreaCommentCaisse.setDisable(false);
       }
   }

    public void comboBoxReason(ActionEvent event){
        if (comboRaison.getSelectionModel().getSelectedIndex() == 0){ // salary selected
            comboSalaryBeneficial.setDisable(false);
            comboExpenseBank.setDisable(true);
            txtAreaComment.setDisable(true);
        }
        if (comboRaison.getSelectionModel().getSelectedIndex() == 1){ // bank selected
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
        }
        if (radioExpense.isSelected()) {
            vboxIncome.setDisable(true);
            vboxExpense.setDisable(false);
        }
    }
}
