package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import sample.Controller.Global;
import sample.Database.DBHandler;
import sample.Model.CaisseIncExp;
import sample.Model.Cash;

import java.net.URL;
import java.util.Objects;

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
    private ObservableList<Integer> data = FXCollections.observableArrayList();
    private Double solde;

    @FXML
    void initialize() {
        setComboBoxValues();

        // force the field to be float only
        txtLessThanFiftyCents.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,3}([.]\\d{0,2})?")) {
                txtLessThanFiftyCents.setText(oldValue);
            }else {
                if (!newValue.equals("")){
                    renderTotal();
                }
            }
        });

        btnOK.setOnAction(event -> {
            if (!Objects.equals(txtLessThanFiftyCents.getText(), "")){

                // set globally
                Global.setCountCashResult(totalCaisseCount);

                if (Global.navFrom.equals("CloseCaisse")){
                    setCashForCurrentCaisse();
                } else {
                    // check if counted cash = caisse cash
                    if (Global.formatDouble(totalCaisseCount).equals(Global.formatDouble(Global.getCurrentCaisse().getMontant()))){
                        Global.showInfoMessage(
                                "Vérification Correcte !",
                                "La monnaie disponible en caisse correspond bien à celle du système");

                        // caisse has no error
                        Global.getCurrentCaisse().setHasError(0);
                    } else {

                        boolean result = Global.showInfoMessageWithBtn(
                                "Vérification Incorrecte",
                                "Le montant compté n'est pas égal au montant enregistré dans le système. " +
                                        "Voulez vous recompter ou signaler l'erreur ?",
                                "Compter à nouveau",
                                "Signaler l'erreur");

                        if (!result){
                            // caisse has error
                            Global.getCurrentCaisse().setHasError(1);
                            addErrorToCurrentCaisse();
                            btnOK.getScene().getWindow().hide();
                        }
                    }
                }
            }else {
                Global.showErrorMessage("Valeurs entrées incorrectes!",
                        "Veuillez verifier vos entrées.");
            }
        });
    }

    /*-------------------------------------------------------------------------------*/

    public void onValueChanged(){
        renderTotal();
    }

    public void clear(){
        txtLessThanFiftyCents.selectAll();
    }

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
            // caisse has no error
            Global.getCurrentCaisse().setHasError(0);
            //Global.closeWindow("Fermeture");
            URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
            Global.navigateTo(navPath,"Fermeture");
            btnOK.getScene().getWindow().hide();
        } else {
            // add error to caisse
            boolean result = Global.showInfoMessageWithBtn(
                    "Vérification Incorrecte",
                    "Le montant compté n'est pas égal au montant enregistré dans le système. " +
                            "Voulez vous recompter ou signaler l'erreur ?",
                    "Compter à nouveau",
                    "Signaler l'erreur");

            if (!result){
                // caisse has error
                Global.getCurrentCaisse().setHasError(1);
                addErrorToCurrentCaisse();
            }
        }
    }

    private void addErrorToCurrentCaisse() {

        if (Global.navFrom.equals("CloseCaisse")){
            solde = Global.getComputedSoldeCaisse() - totalCaisseCount;
        }else {
            solde = Global.getCurrentCaisse().getMontant() - totalCaisseCount;
        }

        // check if we have more or less cash
        if (solde < 0){
            Global.showErrorMessage(
                    "Une erreur de caisse vient d'être signalée dans la section 'commentaires' de cette caisse",
                    "Ceci a lieu car il y a " + solde * -1 +" € en plus dans la caisse.");

            addErrorLineToCaisse(solde);
            // add error to error list
            Global.getErrorList().add(solde);
            // make solde positive
            //solde = solde * -1;

            if (Global.navFrom.equals("CloseCaisse")){
                Global.closeWindow("Fermeture");
                URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
                Global.navigateTo(navPath,"Fermeture");
                // error sent

                // update amount caisse
                Global.getCurrentCaisse().setMontant(Global.getCurrentCaisse().getMontant() + solde);
            } else {
                // error sent

                // update amount caisse
                Global.getCurrentCaisse().setMontant(Global.getCurrentCaisse().getMontant() + solde);

                URL navPath = getClass().getResource("/sample/View/CashRegister/createCaisse.fxml");
                Global.navigateTo(navPath,"Creation");
            }


        } else if (solde > 0){
            Global.showErrorMessage(
                    "Une erreur de caisse vient d'être signalée dans la section 'commentaires' de cette caisse",
                    "Ceci à lieu car il manque " + solde +" € dans la caisse.");

            addErrorLineToCaisse(solde);

            if (Global.navFrom.equals("CloseCaisse")){
                Global.closeWindow("Fermeture");
                URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
                Global.navigateTo(navPath,"Fermeture");
                // error sent

                // update amount caisse
                Global.getCurrentCaisse().setMontant(Global.getCurrentCaisse().getMontant() - solde);
            } else {
                // error sent

                // update amount caisse
                Global.getCurrentCaisse().setMontant(Global.getCurrentCaisse().getMontant() - solde);
                URL navPath = getClass().getResource("/sample/View/CashRegister/createCaisse.fxml");
                Global.navigateTo(navPath,"Creation");
            }


        }
    }

    private void addErrorLineToCaisse(Double soldeCaisse) {

        // to do
        if (soldeCaisse > 0){
            // cash missing
            CaisseIncExp errorExpense = new CaisseIncExp(
                    soldeCaisse,
                    Global.getSystemDate(),
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
            // set error in current caisse
            Global.getCurrentCaisse().setHasError(1);
            addErrorLineInDB(errorExpense);
        } else if (soldeCaisse < 0){
            // more cash
            CaisseIncExp errorIncome = new CaisseIncExp(
                    soldeCaisse * -1,
                    Global.getSystemDate(),
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
            // set error in current caisse
            Global.getCurrentCaisse().setHasError(1);
            addErrorLineInDB(errorIncome);
        }

    }

    private void addErrorLineInDB(CaisseIncExp errorExpense) {

        Platform.runLater(() ->{
            DBHandler dbHandler = new DBHandler();
            dbHandler.addErrorLine(errorExpense);
            // add error amount
            Double amount = Global.getCurrentCaisse().getError_amount() + solde;
            dbHandler.updateErrorAmount(amount , Global.getCurrentCaisse().getId());
        });

    }

    private void setComboBoxValues() {
        for (int i = 0; i < 50; i++){
            data.add(i);
        }

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
        comboBox.setItems(data);
        comboBox.getSelectionModel().select(0);
    }

    private void renderTotal() {

        if (!Objects.equals(txtLessThanFiftyCents.getText(), "")){
            totalCaisseCount = (Double.valueOf(txtLessThanFiftyCents.getText())
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
        }else {
            Global.showErrorMessage("Valeurs entrées incorrectes!",
                    "Veuillez verifier vos entrées.");
        }
    }

}
