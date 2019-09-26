package sample.Controller.CashRegister;

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
import sample.Database.DBHandler;
import sample.Model.CaisseIncExp;
import sample.Model.Cash;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
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
    Double solde;

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
            // set globally
            Global.setCountCashResult(totalCaisseCount);

            if (Global.navFrom == "CloseCaisse"){
                setCashForCurrentCaisse();
            } else {
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
            }
        });

    }

    /*-------------------------------------------------------------------------------*/

    private void setCashForCurrentCaisse() {
        Cash cash = new Cash(
                Global.getCurrentCaisse().getId(),
                Global.getCurrentCaisse().getNumeroShift(),
                Double.valueOf(txtLessThanFiftyCents.getText()),
                txtBoxFiftyCents.getValue(),
                txtBoxOne.getValue(),
                txtBoxTwo.getValue(),
                txtBoxFive.getValue(),
                txtBoxTen.getValue(),
                txtBoxTwenty.getValue(),
                txtBoxfiftyEuros.getValue(),
                txtBoxOneHundred.getValue(),
                txtBoxTwoHundred.getValue()
        );

        Global.setCaisseCash(cash);

        if (Double.parseDouble(countTotal.getText()) == Global.getComputedSoldeCaisse()){
            Global.closeWindow("Fermeture");
            URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
            Global.goToWindow(navPath, btnOK,"Fermeture", true);
        } else {
            // add error to caisse
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
    }

    private void addErrorToCurrentCaisse() {

        if (Global.navFrom == "CloseCaisse"){
            solde = Global.getComputedSoldeCaisse() - totalCaisseCount;
        }else {
            solde = Global.getCurrentCaisse().getMontant() - totalCaisseCount;
        }

        // check if we have more or less cash
        if (solde < 0){
            Global.showErrorMessage(
                    "Une erreur de caisse vient d'être signalée dans la section 'commentaires' de cette caisse",
                    "Ceci à lieu car il y a " + solde * -1 +" € en plus dans la caisse.");

            addErrorLineToCaisse(solde);
            // make solde positive
            solde = solde * -1;

            if (Global.navFrom == "CloseCaisse"){
                Global.closeWindow("Fermeture");
                URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
                Global.goToWindow(navPath, btnOK,"Fermeture", true);
                // error sent

                // update amount caisse
                Global.getCurrentCaisse().setMontant(Global.getCurrentCaisse().getMontant() + solde);
            } else {
                // error sent

                // update amount caisse
                Global.getCurrentCaisse().setMontant(Global.getCurrentCaisse().getMontant() + solde);

                URL navPath = getClass().getResource("/sample/View/CashRegister/createCaisse.fxml");
                Global.goToWindow(navPath, btnOK,"Creation", true);
            }


        } else if (solde > 0){
            Global.showErrorMessage(
                    "Une erreur de caisse vient d'être signalée dans la section 'commentaires' de cette caisse",
                    "Ceci à lieu car il manque " + solde +" € dans la caisse.");

            addErrorLineToCaisse(solde);

            if (Global.navFrom == "CloseCaisse"){
                Global.closeWindow("Fermeture");
                URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
                Global.goToWindow(navPath, btnOK,"Fermeture", true);
                // error sent

                // update amount caisse
                Global.getCurrentCaisse().setMontant(Global.getCurrentCaisse().getMontant() - solde);
            } else {
                // error sent

                // update amount caisse
                Global.getCurrentCaisse().setMontant(Global.getCurrentCaisse().getMontant() - solde);
                URL navPath = getClass().getResource("/sample/View/CashRegister/createCaisse.fxml");
                Global.goToWindow(navPath, btnOK,"Creation", true);
            }


        }
    }

    private void addErrorLineToCaisse(Double soldeCaisse) {

        // to do
        if (soldeCaisse > 0){
            // cash missing
            CaisseIncExp errorExpense = new CaisseIncExp(
                    soldeCaisse,
                    Date.valueOf(LocalDate.now()),
                    Global.getSystemTime(),
                    Global.getConnectedUser().getId(),
                    "Erreur dans la caisse. Il manque " + soldeCaisse + " euros.",
                    Global.getCurrentCaisse().getNumeroShift(),
                    Global.getCurrentCaisse().getId(),
                    "Erreur de comptage",
                    "",
                    1,
                    ""
            );
            addErrorLineInDB(errorExpense);
        } else if (soldeCaisse < 0){
            // more cash
            CaisseIncExp errorIncome = new CaisseIncExp(
                    soldeCaisse * -1,
                    Date.valueOf(LocalDate.now()),
                    Global.getSystemTime(),
                    Global.getConnectedUser().getId(),
                    "Erreur dans la caisse. Il y a " + soldeCaisse * -1 + " euros de plus que prévu.",
                    Global.getCurrentCaisse().getNumeroShift(),
                    Global.getCurrentCaisse().getId(),
                    "Erreur de comptage",
                    "",
                    0,
                    ""
            );
            addErrorLineInDB(errorIncome);
        }

    }

    private void addErrorLineInDB(CaisseIncExp errorExpense) {
        DBHandler dbHandler = new DBHandler();
        dbHandler.addErrorLine(errorExpense);
    }

/*    public void closeAllAndCreateCaisse() {
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
    }*/

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

        countTotal.setText(totalCaisseCount + "");

    }

}
