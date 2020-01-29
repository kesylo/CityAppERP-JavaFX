package sample.Controller.Reports;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.Global;
import sample.Model.Payment;
import sample.Model.User;

import java.sql.Date;
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
    //endregion

    private DBHandler dbHandler = new DBHandler();
    private DialogController<String> wd = null;

    @FXML
    void initialize() {

        loadUserList();

        Global.inputTextFormater(txtAmount, 10,2, 3);

        btnCalculate.setOnAction(event -> {
            savePayment();

        });
    }

    private void savePayment() {
        long millis = System.currentTimeMillis();
        Date sqlDate = new java.sql.Date(millis);
        double amount ;
        if (txtAmount.getText().equals("") || txtAmount.getText() == null){
            amount = 0;
        } else {
            amount = Double.parseDouble(txtAmount.getText());
        }

        Payment payment = new Payment(
                amount,
                sqlDate,
                comboUser.getSelectionModel().getSelectedItem().getId(),
                txtDescription.getText()
        );

        System.out.println(payment);
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
