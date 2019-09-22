package sample.Controller.CashRegister;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.sun.glass.ui.Window;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Controller.Global;

import java.io.IOException;
import java.util.List;

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

    private Double totalCaisseCount = 0.0;

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
            if (totalCaisseCount == Global.getCurrentCaisse().getMontant()){
                Global.showInfoMessage(
                        "Vérification Correcte !",
                        "La monnaie disponible en caisse correspond bien à celle du système");
            } else {
                boolean result = Global.showInfoMessageWithBtn(
                        "Vérification Incorrecte",
                        "Le montant compté n'est pas égal au montant enregistré dans le système. " +
                                "Voulez vous recompter ou signaler l'erreur ?",
                        "Compter à nouveau",
                        "Signaler l'erreur");

                if (!result){
                    addErrorToCurrentCaisse();
                }
            }
        });



    }
    /*-------------------------------------------------------------------------------*/

    private void addErrorToCurrentCaisse() {
        Double solde = Global.getCurrentCaisse().getMontant() - totalCaisseCount;

        // check if we have more or less cash
        if (solde < 0){
            Global.showErrorMessage(
                    "Une erreur de caisse vient d'être signaler dans la section 'commentaires' de cette caisse",
                    "Ceci à lieu car il y a " + solde * -1 +" € en plus dans la caisse.");

            closeAllAndCreaeCaisse();

            addErrorLineToCaisse();
        } else if (solde > 0){
            Global.showErrorMessage(
                    "Une erreur de caisse vient d'être signaler dans la section 'commentaires' de cette caisse",
                    "Ceci à lieu car il manque " + solde +" € dans la caisse.");

            closeAllAndCreaeCaisse();

            addErrorLineToCaisse();
        }



    }

    private void addErrorLineToCaisse() {
        // to do
    }

    public void closeAllAndCreaeCaisse() {
        // get all windows and close
        List<Window> windows = Window.getWindows();
        for (int i = windows.size() - 1; i >= 0; i--) {
            if (windows.get(i).getTitle() == Global.appName) {
                windows.get(i).close();
            }
        }

        // load login scène
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/View/CashRegister/createCaisse.fxml"));
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

        // navigate to new screen
        btnOK.getScene().getWindow().hide();
    }

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
        totalCaisseCount = (Double.valueOf(txtLessThanFiftyCents.getText()) / 100
        + txtBoxFiftyCents.getValue() * 0.5
        + txtBoxOne.getValue()
        + txtBoxTwo.getValue() * 2
        + txtBoxFive.getValue() * 5
        + txtBoxTen.getValue() * 10
        + txtBoxTwenty.getValue() * 20
        + txtBoxfiftyEuros.getValue() * 50
        + txtBoxOneHundred.getValue() * 100
        + txtBoxTwoHundred.getValue() * 200);

        countTotal.setText(totalCaisseCount + " €");
    }

}
