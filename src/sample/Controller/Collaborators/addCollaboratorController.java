package sample.Controller.Collaborators;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import sample.Controller.Global.CollaboratorGlobal;
import sample.Controller.Global.Global;
import sample.Database.DBHandler;
import sample.Model.User;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class addCollaboratorController {

    //region UI
    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;

    @FXML
    private JFXButton btnhide;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXTextField txtPseudo;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXCheckBox ckcAddManyEntries;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtNumeroRue;

    @FXML
    private JFXTextField txtNumeroBoite;

    @FXML
    private JFXRadioButton radioSexeMale;

    @FXML
    private JFXRadioButton radioSexeFemale;

    @FXML
    private JFXTextField txtSurname;

    @FXML
    private JFXTextField txtNumEmployee;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtSalaryMonth;

    @FXML
    private JFXTextField txtZipCode;

    @FXML
    private JFXTextField txtRegisterNumber;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXDatePicker datePickerStartService;

    @FXML
    private JFXDatePicker datePickerEndService;

    @FXML
    private JFXComboBox<String> comboPhoneCountry;

    @FXML
    private JFXComboBox<String> comboCountry;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private JFXTextField txtSalaryHour;

    @FXML
    private JFXRadioButton radioMaried;

    @FXML
    private JFXRadioButton radioSingle;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private ImageView photo1;

    @FXML
    private ImageView photo11;

    @FXML
    private JFXRadioButton radioStOvrier;

    @FXML
    private JFXRadioButton radioStOuvrierExtra;

    @FXML
    private JFXRadioButton radioStEmploye;

    @FXML
    private JFXRadioButton radioStEtudiantExtra;

    @FXML
    private JFXRadioButton radioStEtudiantOuvrier;

    @FXML
    private JFXRadioButton radioStOuvrierFlexi;

    @FXML
    private JFXRadioButton radioStEtudiantEmploye;

    @FXML
    private ToggleGroup departementGroup;

    @FXML
    private ToggleGroup statusMaritaireGroup;

    @FXML
    private ToggleGroup sexGroup;

    @FXML
    private ToggleGroup statusGroup;

    @FXML
    private JFXRadioButton radioStEmployeFlexi;

    @FXML
    private JFXRadioButton radioDeptFO;

    @FXML
    private JFXRadioButton radioDeptBO;

    @FXML
    private JFXRadioButton radioDeptHK;

    @FXML
    private JFXRadioButton radioDeptMN;

    @FXML
    private ImageView photoDownUser;
    //endregion

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnBack);
    }

    private List<String> formErrorsList = new ArrayList<>();
    private User user = new User();
    private DBHandler dbHandler;

    @FXML
    void initialize() {
        // set profile photo
        //Global.setProfileIcon(photo);

        // set user profile
        Global.setUserProfile(lblConnectedUser, btnLogOut);

        //region Fields Constraints
        // types: 1 : string,       2 : float,       3 : int,       0 : all
        // String case: 1 : toUpper,       2 : one letter Caps,       3 : nothing

        Global.txtFormater(txtName, 20,1, 1);
        Global.txtFormater(txtSurname, 20,1, 1);
        Global.txtFormater(txtEmail, 30,0, 2);
        Global.txtFormater(txtNumEmployee, 4,3, 3);
        Global.txtFormater(txtAddress, 30,0, 2);
        Global.txtFormater(txtNumeroRue, 6,3, 3);
        Global.txtFormater(txtNumeroBoite, 3,0, 1);
        Global.txtFormater(txtCity, 15,1, 2);
        Global.txtFormater(txtPhoneNumber, 15,3, 3);
        Global.txtFormater(txtSalaryMonth, 10,2, 3);
        Global.txtFormater(txtSalaryHour, 10,2, 3);
        Global.txtFormater(txtZipCode, 5,2, 3);
        Global.txtFormater(txtRegisterNumber, 15,0, 1);
        Global.txtFormater(txtPseudo, 15,0, 3);
        Global.txtFormater(txtPassword, 20,0, 3);
        //endregion

        // check which radio is checked
        sexGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (radioSexeMale.isSelected()){
                user.setMaritalStatus("Masculin");
            }
            if (radioSexeFemale.isSelected()){
                user.setMaritalStatus("Feminin");
            }
        });

        departementGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (radioDeptFO.isSelected()){
                user.setDepartement("FO");
            }
            if (radioDeptBO.isSelected()){
                user.setDepartement("BO");
            }
            if (radioDeptHK.isSelected()){
                user.setDepartement("HK");
            }
            if (radioDeptMN.isSelected()){
                user.setDepartement("MN");
            }
        });

        statusGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (radioStOvrier.isSelected()){
                user.setStatus("OV");
            }
            if (radioStEmploye.isSelected()){
                user.setStatus("EM");
            }
            if (radioStEtudiantOuvrier.isSelected()){
                user.setStatus("EOV");
            }
            if (radioStEtudiantEmploye.isSelected()){
                user.setStatus("EEM");
            }
            if (radioStOuvrierExtra.isSelected()){
                user.setStatus("OEX");
            }
            if (radioStEtudiantExtra.isSelected()){
                user.setStatus("EEX");
            }
            if (radioStOuvrierFlexi.isSelected()){
                user.setStatus("OFL");
            }
            if (radioStEmployeFlexi.isSelected()){
                user.setStatus("EFL");
            }
        });

        statusMaritaireGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (radioMaried.isSelected()){
                user.setMaritalStatus("Marie");
            }
            if (radioSingle.isSelected()){
                user.setMaritalStatus("Celibataire");
            }
        });

        // set datepicker font
        datePicker.setStyle("-fx-font: 14px Poppins;");
        datePickerEndService.setStyle("-fx-font: 14px Poppins;");
        datePickerStartService.setStyle("-fx-font: 14px Poppins;");


        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            System.out.println("date changed");
        });

        btnSave.setOnAction(event -> {
            saveUser();

            // reset error list
            formErrorsList.clear();
        });

        btnBack.setOnAction(event -> {
            btnBack.getScene().getWindow().hide();
        });
    }


    /*--------------------------------------------------------------------------------*/
    private void saveUser() {
        // if button pressed name if details, add or edit
        if (Objects.equals(CollaboratorGlobal.getActionName(), "details")){

            // gray out save btn

        }else if (Objects.equals(CollaboratorGlobal.getActionName(), "add")){

            // check if fields are well filled
            boolean areFieldsCorrect = checkAllFields();
            if (areFieldsCorrect){
                addCollaboratorToDB();
            }

        }else if (Objects.equals(CollaboratorGlobal.getActionName(), "edit")){

        }
    }

    private boolean checkAllFields() {
        boolean fieldsAreOk = false;

        //region Name
        if (txtName.getLength() == 0 || txtName.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Nom' entré est incorrect !");
        }else {
            fieldsAreOk = true;
        }

        if (txtName.getLength() != 0 || txtName.getText() != null){
            fieldsAreOk = true;
        }

        //endregion

        //region Surname
        if (txtSurname.getLength() == 0 || txtSurname.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Prénom' entré est incorrect !");
        }else {
            fieldsAreOk = true;
        }
        //endregion

        //region Date
        if (datePicker.getValue() == null){
            // add error to list
            formErrorsList.add("La 'Date anniversaire' entrée est incorrecte !");
        }else {
            fieldsAreOk = true;
        }
        //endregion

        //region DateIn
        if (datePickerStartService.getValue() == null){
            // add error to list
            formErrorsList.add("La 'Date en service' entrée est incorrecte !");
        }else {
            fieldsAreOk = true;
        }
        //endregion

        //region Email
        if (txtEmail.getLength() == 0 || txtEmail.getText() == null){
            // add error to list
            formErrorsList.add("L'adresse 'E-mail' entrée est incorrecte !");
        }else {
            fieldsAreOk = true;
        }
        //endregion

        //region Employee number
        if (txtNumEmployee.getLength() == 0 || txtNumEmployee.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Numéro Employé' entré est incorrect !");
        }else {
            fieldsAreOk = true;
        }
        //endregion

        //region Address
        if (txtAddress.getLength() == 0 || txtAddress.getText() == null){
            // add error to list
            formErrorsList.add("L' 'adresse' entrée est incorrecte !");
        }else {
            fieldsAreOk = true;
        }
        //endregion

        //region street Number
        if (txtNumeroRue.getLength() == 0 || txtNumeroRue.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Numéro Rue' entré est incorrect !");
        }else {
            fieldsAreOk = true;
        }
        //endregion

        //region Box number
        if (txtNumeroBoite.getLength() == 0 || txtNumeroBoite.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Numéro Boite' entré est incorrect !");
        }else {
            fieldsAreOk = true;
        }
        //endregion

        //region Ville
        if (txtCity.getLength() == 0 || txtCity.getText() == null){
            // add error to list
            formErrorsList.add("La 'ville' entrée est incorrecte !");
        }else {
            fieldsAreOk = true;
        }
        //endregion

        //region phone
        if (txtPhoneNumber.getLength() == 0 || txtPhoneNumber.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Numéro Téléphone' entré est incorrect !");
        }
        //endregion

        //region salary Month
        if (txtSalaryMonth.getLength() == 0 || txtSalaryMonth.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Salaire Mensuel' entré est incorrect !");
        }
        //endregion

        //region salary Hour
        if (txtSalaryHour.getLength() == 0 || txtSalaryHour.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Salaire Horaire' entré est incorrect !");
        }
        //endregion

        //region Zip code
        if (txtZipCode.getLength() == 0 || txtZipCode.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Code Postal' entré est incorrect !");
        }
        //endregion

        //region National Registry
        if (txtRegisterNumber.getLength() == 0 || txtRegisterNumber.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Numero National' entré est incorrect !");
        }
        //endregion

        //region Pseudo
        if (txtPseudo.getLength() == 0 || txtPseudo.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Pseudo' entré est incorrect !");
        }
        //endregion

        //region Password
        if (txtPassword.getLength() == 0 || txtPassword.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Mot de passe' entré est incorrect !");
        }

        if (formErrorsList.size() > 0){
            // show all errors if exists
            showAllErrors();
        }else {
            fieldsAreOk = true;
        }

        return fieldsAreOk;
    }

    private void showAllErrors() {
        // show error if exist
        if (formErrorsList.size() > 0 ){
            StringBuilder allErrors = new StringBuilder();

            for (String item : formErrorsList){
                allErrors.append(item).append("\n");
            }

            Global.showErrorMessage("Les champs suivants sont incorrects !",
                    allErrors.toString());
        }
    }

    private void addCollaboratorToDB() {
        dbHandler = new DBHandler();

        // Date
        String birthDate = Global.localDateToString(datePicker.getValue());
        String dateInService = Global.localDateToString(datePickerStartService.getValue());
        if (datePickerEndService.getValue() != null){
            String dateOutService = Global.localDateToString(datePickerEndService.getValue());
        }




        // get all data from fields
        user.setFirstName(txtName.getText());
        user.setLastName(txtSurname.getText());
        user.setBirthday(birthDate);
        user.setDateInService(dateInService);
        user.setDateOutService(null);
        // check
        user.setEmail(txtEmail.getText());
        user.setEmployeeNumber(Integer.parseInt(txtNumEmployee.getText()));
        user.setAdress(txtAddress.getText());
        user.setHouseNumber(Integer.parseInt(txtNumeroRue.getText()));
        user.setLetterBoxNumber(txtNumeroBoite.getText());
        user.setCity(txtCity.getText());
        user.setPhoneCountry(comboPhoneCountry.getSelectionModel().getSelectedItem());
        user.setPhoneNumber(txtPhoneNumber.getText());
        user.setSalary1(Float.parseFloat(txtSalaryMonth.getText()));
        user.setSalary2(Float.parseFloat(txtSalaryHour.getText()));
        user.setPostalCode(Integer.parseInt(txtZipCode.getText()));
        // check
        user.setNationalRegistreryNumber(txtRegisterNumber.getText());
        user.setPseudo(txtPseudo.getText());
        user.setPassword(txtPassword.getText());


        // send to DB
        System.out.println(user.toString());
        dbHandler.createUser(user);

    }

}
