package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import sample.Controller.Global;

public class countCashCaisseController {

    //region UI elements
    @FXML
    private ComboBox<Integer> txtBoxTwoHundred;

    @FXML
    private ComboBox<Integer> txtBoxOneHundred;

    @FXML
    private ComboBox<Integer> txtBoxfiftyEuros;

    @FXML
    private ComboBox<Integer> txtBoxTwenty;

    @FXML
    private ComboBox<Integer> txtBoxTen;

    @FXML
    private ComboBox<Integer> txtBoxFive;

    @FXML
    private ComboBox<Integer> txtBoxTwo;

    @FXML
    private ComboBox<Integer> txtBoxOne;

    @FXML
    private ComboBox<Integer> txtBoxFiftyCents;

    @FXML
    private JFXTextField txtLessThanFiftyCents;

    @FXML
    private Label countTotal;

    @FXML
    private JFXButton btnOK;
    //endregion

    Double total = 0.0;

    @FXML
    void computeTotal(ActionEvent event) {
        //System.out.println("changed");
        renderTotal();
    }

    @FXML
    void selectText(MouseEvent event) {
        txtLessThanFiftyCents.selectAll();
    }

    @FXML
    void initialize() {
        setComboBoxValues();

        // force the field to be float only
        txtLessThanFiftyCents.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    txtLessThanFiftyCents.setText(oldValue);
                }
            }
        });

        btnOK.setOnAction(event -> {
            // check if counted cash = caisse cash
            if (total == Global.getCurrentCaisse().getMontant()){
                Global.showInfoMessage(Global.appName,
                        "Vérification Correcte !",
                        "La monnaie disponible en caisse correspond bien à celle du système");
            } else {
                boolean result = Global.showInfoMessageWithBtn(
                        "Vérification Incorrecte",
                        "Le montant compté n'est pas égal au montant enregistré dans le système. " +
                                "Voulez vous recompter ou signaler l'erreur ?",
                        "Compter à nouveau",
                        "Signaler l'erreur");

                if (result){

                }
            }
        });

    }




    /*-------------------------------------------------------------------------------*/

    private void setComboBoxValues() {
        setValuesFor(txtBoxTwoHundred);
        setValuesFor(txtBoxOneHundred);
        setValuesFor(txtBoxfiftyEuros);
        setValuesFor(txtBoxTwenty);
        setValuesFor(txtBoxTen);
        setValuesFor(txtBoxFive);
        setValuesFor(txtBoxTwo);
        setValuesFor(txtBoxOne);
        setValuesFor(txtBoxFiftyCents);
    }

    private void setValuesFor(ComboBox<Integer> comboBox) {
        ObservableList<Integer> data = FXCollections.observableArrayList();
        for (int i = 0; i < 70; i++){
            data.add(i);
        }
        comboBox.setItems(data);
        comboBox.getSelectionModel().select(0);
    }

    private void renderTotal() {
        total = (Double.valueOf(txtLessThanFiftyCents.getText()) / 100
        + txtBoxFiftyCents.getValue() * 0.5
        + txtBoxOne.getValue()
        + txtBoxTwo.getValue() * 2
        + txtBoxFive.getValue() * 5
        + txtBoxTen.getValue() * 10
        + txtBoxTwenty.getValue() * 20
        + txtBoxfiftyEuros.getValue() * 50
        + txtBoxOneHundred.getValue() * 100
        + txtBoxTwoHundred.getValue() * 200);

        countTotal.setText(total + " €");
    }

}
