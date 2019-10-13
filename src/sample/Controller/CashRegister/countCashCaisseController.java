package sample.Controller.CashRegister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import sample.Controller.Global.CashRegisterGlobal;
import sample.Controller.Global.Global;
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

    private double totalCaisseCount;
    private ObservableList<Integer> data = FXCollections.observableArrayList();
    private double solde;

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
           // check if txtLesThan50 is not empty
            if (!Objects.equals(txtLessThanFiftyCents.getText(), "") || txtLessThanFiftyCents.getText() != null){

                // check if we come from CloseCaisse or infos
                if (Objects.equals(Global.navFrom, "CloseCaisse"))
                {
                    comingFromClose();
                }
                else if (Objects.equals(Global.navFrom, "InfosCaisse"))
                {
                    comingFromInfo();
                }
            } else {
                Global.showErrorMessage("Erreur! Les données entrées sont incorrectes",
                        "Veuillez Vérifier tous les champs.");
            }
        });
    }





    /*-------------------------------------------------------------------------------*/



    private void comingFromInfo() {
        // check if solde caisse = counted cash
        if (CashRegisterGlobal.getCurrentCaisse().getMontant() == totalCaisseCount)
        {
            // set count cash result globally
            CashRegisterGlobal.setCountCashResult(Global.roundDouble(totalCaisseCount));

            countDone();

            // nav to creation
            URL navPath = getClass().getResource("/sample/View/CashRegister/createCaisse.fxml");
            Global.navigateTo(navPath,"Creation");

            // close windows
            btnOK.getScene().getWindow().hide();
        }else {
            // ask if user wants to count again or not
            boolean result = Global.showInfoMessageWithBtn(
                    "Vérification Incorrecte",
                    "Le montant compté n'est pas égal au montant enregistré dans le système. " +
                            "Voulez vous recompter ou signaler l'erreur ?",
                    "Compter à nouveau",
                    "Signaler l'erreur");

            if (result){
                System.out.println("compter a nouveau");
            }else {
                addErrorOnInfos();
            }
        }
        // tell system count has been done
        countDone();
    }

    private void addErrorOnInfos() {

        // compute solde
        solde = totalCaisseCount - CashRegisterGlobal.getCurrentCaisse().getMontant();

        // round solde before anything else
        solde = Global.roundDouble(solde);

        // check if we have more or less cash
        if (solde > 0){ // we have MORE money than expected
            // show message
            Global.showErrorMessage(
                    "Erreur de caisse signalée",
                    "Ceci a lieu car il y a " + solde +" € en plus dans la caisse.");
        }
        else if (solde < 0){ // we have LESS money than expected
            // show message
            Global.showErrorMessage(
                    "Erreur de caisse signalée",
                    "Ceci a lieu car on a " + solde +" € dans la caisse.");
        }

        // create Expense error line and set globally
        CashRegisterGlobal.setIncExpError(createExpenseErrorline());

        // set error amount globally
        CashRegisterGlobal.setErrorAmount(solde);

        // set count result globally
        CashRegisterGlobal.setCountCashResult(totalCaisseCount);

        // set caisse error count
        CashRegisterGlobal.getNewCaisse().setHasError(1);

        // set caisse cash even if error
        //countDone();

        // close window
        btnOK.getScene().getWindow().hide();

        // set the new caisse's amount since it has been edited // not current caisse amount
        CashRegisterGlobal.getNewCaisse().setMontant(totalCaisseCount);

        // nav to creation
        URL navPath = getClass().getResource("/sample/View/CashRegister/createCaisse.fxml");
        Global.navigateTo(navPath,"Creation");
    }

    private void comingFromClose() {
        // check if solde caisse = counted cash
        if (CashRegisterGlobal.getComputedSoldeCaisse() == totalCaisseCount)
        {
            // everything ok so close
            btnOK.getScene().getWindow().hide();

            countDone();

            // set count cash result globally
            CashRegisterGlobal.setCountCashResult(Global.roundDouble(totalCaisseCount));
            // close
            refreshCloseCaisse();
        }
        else {
            // ask if user wants to count again or not
            boolean result = Global.showInfoMessageWithBtn(
                    "Vérification Incorrecte",
                    "Le montant compté n'est pas égal au montant enregistré dans le système. " +
                            "Voulez vous recompter ou signaler l'erreur ?",
                    "Compter à nouveau",
                    "Signaler l'erreur");

            if (result){
                System.out.println("compter a nouveau");
            }else {
                addErrorOnClose();
            }
        }

        // tell system count has been done
        countDone();
    }

    private void countDone() {
        Cash countedCash = new Cash(
                CashRegisterGlobal.getCurrentCaisse().getId(),
                CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                Double.parseDouble(txtLessThanFiftyCents.getText()),
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

        // set globally
        CashRegisterGlobal.setCaisseCash(countedCash);
    }

    private void addErrorOnClose() {

        // compute solde
        solde = totalCaisseCount - CashRegisterGlobal.getComputedSoldeCaisse();

        // round solde before anything else
        solde = Global.roundDouble(solde);

        // check if we have more or less cash
        if (solde > 0){ // we have MORE money than expected
            // show message
            Global.showErrorMessage(
                    "Erreur de caisse signalée",
                    "Ceci a lieu car il y a " + solde +" € en plus dans la caisse.");
            // create Income error line and set globally
            CashRegisterGlobal.setIncExpError(createIncomeErrorline());
        }
        else if (solde < 0){ // we have LESS money than expected
            // show message
            Global.showErrorMessage(
                    "Erreur de caisse signalée",
                    "Ceci a lieu car on a " + solde +" € dans la caisse.");
            // create Expense error line and set globally
            CashRegisterGlobal.setIncExpError(createExpenseErrorline());
        }

        // set error amount globally
        if (CashRegisterGlobal.getCurrentCaisse().getError_amount() != 0){
            // add error amount don't override
            CashRegisterGlobal.setErrorAmount((CashRegisterGlobal.getCurrentCaisse().getError_amount()) + (solde));
        }else {
            CashRegisterGlobal.setErrorAmount(solde);
        }

        // set count result globally
        CashRegisterGlobal.setCountCashResult(totalCaisseCount);

        // set erro globally to compute balance better on close
        CashRegisterGlobal.setErrorOnClose(1);

        // set caisse error count
        CashRegisterGlobal.getCurrentCaisse().setHasError(1);

        // set caisse cash even if error
        countDone();

        // close window
        btnOK.getScene().getWindow().hide();

        refreshCloseCaisse();
    }

    private CaisseIncExp createIncomeErrorline() {

        return new CaisseIncExp(
               solde,
                Global.getSystemDate(),
                Global.getSystemTime(),
                Global.getConnectedUser().getId(),
               "Erreur dans la caisse. Il y a " + solde + " euros de trop.",
               CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
               CashRegisterGlobal.getCurrentCaisse().getId(),
               "Erreur de comptage",
               "",
               0,
               ""
       );
    }

    private CaisseIncExp createExpenseErrorline() {

        return new CaisseIncExp(
                solde,
                Global.getSystemDate(),
                Global.getSystemTime(),
                Global.getConnectedUser().getId(),
                "Erreur dans la caisse. On a " + solde + " euros.",
                CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                CashRegisterGlobal.getCurrentCaisse().getId(),
                "Erreur de comptage",
                "",
                1,
                ""
        );
    }

    private void refreshCloseCaisse() {
        // Nav back to Close caisse
        Global.closeWindow("Fermeture");
        URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
        Global.navigateTo(navPath,"Fermeture");
    }

    public void onValueChanged(){
        renderTotal();
    }

    public void clear(){
        txtLessThanFiftyCents.selectAll();
    }

    //region delete

    /*private void setCashForCurrentCaisse() {
        Cash cash = new Cash(
                CashRegisterGlobal.getCurrentCaisse().getId(),
                CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
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

        CashRegisterGlobal.setCaisseCash(cash);

        if (Double.parseDouble(countTotal.getText()) == CashRegisterGlobal.getComputedSoldeCaisse()){
            // caisse has no error
            CashRegisterGlobal.getCurrentCaisse().setHasError(0);
            //CashRegisterGlobal.closeWindow("Fermeture");
            URL navPath = getClass().getResource("/sample/View/CashRegister/closeCaisse.fxml");
            CashRegisterGlobal.navigateTo(navPath,"Fermeture");
            btnOK.getScene().getWindow().hide();
        } else {
            // add error to caisse
            boolean result = CashRegisterGlobal.showInfoMessageWithBtn(
                    "Vérification Incorrecte",
                    "Le montant compté n'est pas égal au montant enregistré dans le système. " +
                            "Voulez vous recompter ou signaler l'erreur ?",
                    "Compter à nouveau",
                    "Signaler l'erreur");

            if (result){
                System.out.println("compter a nouveau");
            }else {
                // caisse has error
                CashRegisterGlobal.getCurrentCaisse().setHasError(1);
                addErrorToCurrentCaisse();
            }
        }
    }





    private void addErrorLineToCaisse(double soldeCaisse) {

        // to do
        if (soldeCaisse > 0){
            // cash missing
            CaisseIncExp errorExpense = new CaisseIncExp(
                    soldeCaisse,
                    CashRegisterGlobal.getSystemDate(),
                    CashRegisterGlobal.getSystemTime(),
                    CashRegisterGlobal.getConnectedUser().getId(),
                    "Erreur dans la caisse. Il manque " + CashRegisterGlobal.roundDouble(soldeCaisse) + " euros.",
                    CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                    CashRegisterGlobal.getCurrentCaisse().getId(),
                    "Erreur de comptage",
                    "",
                    1,
                    ""
            );
            // set error in current caisse
            CashRegisterGlobal.getCurrentCaisse().setHasError(1);
            addErrorLineInDB(errorExpense);
        } else if (soldeCaisse < 0){
            // more cash
            CaisseIncExp errorIncome = new CaisseIncExp(
                    soldeCaisse * -1,
                    CashRegisterGlobal.getSystemDate(),
                    CashRegisterGlobal.getSystemTime(),
                    CashRegisterGlobal.getConnectedUser().getId(),
                    "Erreur dans la caisse. Il y a " + CashRegisterGlobal.roundDouble(soldeCaisse * -1) + " euros de plus que prévu.",
                    CashRegisterGlobal.getCurrentCaisse().getNumeroShift(),
                    CashRegisterGlobal.getCurrentCaisse().getId(),
                    "Erreur de comptage",
                    "",
                    0,
                    ""
            );
            // set error in current caisse
            CashRegisterGlobal.getCurrentCaisse().setHasError(1);
            addErrorLineInDB(errorIncome);
        }

    }

    private void addErrorLineInDB(CaisseIncExp errorExpense) {

        Platform.runLater(() ->{
            DBHandler dbHandler = new DBHandler();
            dbHandler.addErrorLine(errorExpense);
            // add error amount
            double amount = CashRegisterGlobal.getCurrentCaisse().getError_amount() + CashRegisterGlobal.roundDouble(solde);
            dbHandler.updateErrorAmount(amount , CashRegisterGlobal.getCurrentCaisse().getId());
        });

    }*/
    //endregion

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

            totalCaisseCount = Global.roundDouble(totalCaisseCount);

            countTotal.setText(totalCaisseCount + "");
        }else {
            Global.showErrorMessage("Valeurs entrées incorrectes!",
                    "Veuillez verifier vos entrées.");
        }
    }

}
