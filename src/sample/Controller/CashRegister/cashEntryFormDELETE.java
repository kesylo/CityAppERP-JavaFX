package sample.Controller.CashRegister;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Controller.Global;

import java.io.IOException;

public class cashEntryFormDELETE {

    @FXML
    private JFXTextField txtLessthanOneEuro;

    @FXML
    private JFXTextField txtOneEuro;

    @FXML
    private VBox vboxRigth;

    @FXML
    private VBox vboxLeft;

    @FXML
    private JFXTextField txtFiveEuros;

    @FXML
    private JFXTextField txtTwentyEuros;

    @FXML
    private JFXTextField txtOneHundredEuros;

    @FXML
    private JFXTextField txtFiftyCents;

    @FXML
    private JFXTextField txtTwoEuros;

    @FXML
    private JFXTextField txtTenEuros;

    @FXML
    private JFXTextField txtFiftyEuros;

    @FXML
    private JFXTextField txtTwoHundredEuros;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnCreate;

    public boolean isFormValid;


    @FXML
    void initialize() {

        btnCreate.setOnAction(event -> {
            addCashToCaisse();
        });

        btnCancel.setOnAction(event -> {
            goToWindow("/sample/View/CashRegister/addCash.fxml");
        });

    }



    /*----------------------------------------------------------------------------------------------------*/

    private void addCashToCaisse() {
        if (validate()) {
            System.out.println("form ok");
        }


    }


    private boolean validate() {
        // match float
        if (txtLessthanOneEuro.getText().matches("[-+]?([0-9]*\\.[0-9]+|[0-9]+)")) {
            // math int
            if (txtOneEuro.getText().matches("\\d+")) {
                if (txtFiveEuros.getText().matches("\\d+")) {
                    if (txtTwentyEuros.getText().matches("\\d+")) {
                        if (txtOneHundredEuros.getText().matches("\\d+")) {
                            if (txtFiftyCents.getText().matches("\\d+")) {
                                if (txtTwoEuros.getText().matches("\\d+")) {
                                    if (txtTenEuros.getText().matches("\\d+")) {
                                        if (txtFiftyEuros.getText().matches("\\d+")) {
                                            if (txtTwoHundredEuros.getText().matches("\\d+")) {
                                                return true;
                                            }
                                            txtTwoHundredEuros.setStyle("-fx-background-color: #F44336");
                                        }
                                        txtFiftyEuros.setStyle("-fx-background-color: #F44336");
                                    }
                                    txtTenEuros.setStyle("-fx-background-color: #F44336");
                                }
                                txtTwoEuros.setStyle("-fx-background-color: #F44336");
                            }
                            txtFiftyCents.setStyle("-fx-background-color: #F44336");
                        }
                        txtOneHundredEuros.setStyle("-fx-background-color: #F44336");
                    }
                    txtTwentyEuros.setStyle("-fx-background-color: #F44336");
                }
                txtFiveEuros.setStyle("-fx-background-color: #F44336");
            }
            txtOneEuro.setStyle("-fx-background-color: #F44336");
        }
        txtLessthanOneEuro.setStyle("-fx-background-color: #F44336");
        return false;
    }

    private void goToWindow(String windowPath) {
        // navigate to new screen
        btnCancel.getScene().getWindow().hide();

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
        stage.setTitle(Global.appName);
        stage.show();
        // animate window
        new FadeIn(root).play();
    }
}
