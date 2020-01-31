package sample.Controller.Reports;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.Global;
import sample.Model.Caisse;
import sample.Model.CaisseIncExp;
import sample.Model.Payment;
import sample.Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Collections.sort;

public class PaymentController {

    //region UI
    @FXML
    private JFXComboBox<User> comboUser;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private JFXButton btnCalculate;

    @FXML
    private JFXRadioButton radioBank;

    @FXML
    private JFXRadioButton radioFromCaisse;
    //endregion

    private DBHandler dbHandler = new DBHandler();
    private Caisse lastCaisse = new Caisse();
    private DialogController<String> wd = null;

    @FXML
    void initialize() {

        loadUserList();

        getCurrentCaisse();

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
            }
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
                    user.getFirstName() + " " + user.getLastName()
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

    private void getCurrentCaisse() {
        // run async
        Platform.runLater(() ->{
            wd = new DialogController<>(btnCalculate.getScene().getWindow(), "Enrégistrement...");
            wd.exec("123", inputParam -> {

                Platform.runLater(() -> lastCaisse = dbHandler.getLastCaisse());
                return 1;
            });
        });
    }

    private Payment createOutput() {
        String date = Global.getSystemDateDMY();
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

        Platform.runLater(() ->{
            wd = new DialogController<>(btnCalculate.getScene().getWindow(), "Chargement des utilisateurs...");

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
}
