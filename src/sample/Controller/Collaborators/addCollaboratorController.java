package sample.Controller.Collaborators;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import sample.Controller.DialogController;
import sample.Global.CollaboratorGlobal;
import sample.Global.Global;
import sample.Database.DBHandler;
import sample.Model.Country;
import sample.Model.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class addCollaboratorController {

    //region UI
    @FXML
    private Label lblConnectedUser;

    @FXML
    private Pane badgeUserArchived;

    @FXML
    private Label lblHeadingText;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXTextField txtPseudo;

    @FXML
    private JFXTextField txtIBAN;

    @FXML
    private JFXTextField txtPassword;

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

    //endregion

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnBack);
    }

    private List<String> formErrorsList = new ArrayList<>();
    private User user = new User();
    private DBHandler dbHandler;
    private ObservableList<String> countriesNamesList = FXCollections.observableArrayList();
    private ObservableList<String> countriesCodeList = FXCollections.observableArrayList();
    private boolean saveDone = false;

    @FXML
    void initialize() {
        // set profile photo
        Global.setProfileIcon(photo);

        // set focus on name
        txtName.requestFocus();

        // set user profile
        Global.setUserProfile(lblConnectedUser, btnLogOut);

        if (Objects.equals(CollaboratorGlobal.getActionName(), "details")){
            // set header Text
            lblHeadingText.setText("Détails du collaborateur");

            fillComboCountry();
            fillComboPhoneCountry();
            loadCurrentUserData();
            makeFieldsNotEditable();
            // disable Outdate dateTime if user is already archived
            if (CollaboratorGlobal.getPreviewUser().getDateOutService() != null){
                // show archived badge
                badgeUserArchived.setVisible(true);
            }

        }else if (Objects.equals(CollaboratorGlobal.getActionName(), "add")){
            // set header Text
            lblHeadingText.setText("Ajouter un collaborateur");

            fillComboCountry();
            fillComboPhoneCountry();

        }else if (Objects.equals(CollaboratorGlobal.getActionName(), "edit")){
            // set header Text
            lblHeadingText.setText("Modifier un collaborateur");

            fillComboCountry();
            fillComboPhoneCountry();
            loadCurrentUserData();
        }

        //region Fields Constraints
        // types: 1 : string,       2 : float,       3 : int,       0 : all
        // String case: 1 : toUpper,       2 : one letter Caps,       3 : nothing

        Global.txtFormater(txtName, 20,0, 1);
        Global.txtFormater(txtSurname, 20,0, 1);
        Global.txtFormater(txtEmail, 40,0, 2);
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

        // set datepicker font
        datePicker.setStyle("-fx-font: 14px Poppins;");
        datePickerEndService.setStyle("-fx-font: 14px Poppins;");
        datePickerStartService.setStyle("-fx-font: 14px Poppins;");
        // set comboBox font
        comboPhoneCountry.setStyle("-fx-font: 14px Poppins;");
        comboCountry.setStyle("-fx-font: 14px Poppins;");


        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            txtRegisterNumber.clear();
            txtRegisterNumber.setText(Global.convertBirthdayToRegisterNber(newValue));
        });

        btnSave.setOnAction(event -> {
            saveUser();

            // reset error list
            formErrorsList.clear();
        });

        btnBack.setOnAction(event -> {
            if (saveDone){
                // nav with refresh
                Global.closeWindow("Collaborateurs");
                URL navPath = getClass().getResource("/sample/View/Collaborators/usersDashboard.fxml");
                Global.navigateTo(navPath,"Collaborateurs");
                btnBack.getScene().getWindow().hide();
            }else {
                btnBack.getScene().getWindow().hide();
            }
        });

    }

    /*--------------------------------------------------------------------------------*/

    private void loadCurrentUserData() {
        User previewUser = CollaboratorGlobal.getPreviewUser();

        txtName.setText(previewUser.getFirstName());
        txtSurname.setText(previewUser.getLastName());
        if (previewUser.getBirthday() != null){
            datePicker.setValue(Global.stringToLocalDate(previewUser.getBirthday()));
        }
        txtEmail.setText(previewUser.getEmail());
        txtNumEmployee.setText(previewUser.getEmployeeNumber() + "");
        txtAddress.setText(previewUser.getAddress());
        comboCountry.getSelectionModel().select(previewUser.getCountry());
        txtCity.setText(previewUser.getCity());
        comboPhoneCountry.getSelectionModel().select(previewUser.getPhoneCountry());
        txtPhoneNumber.setText(previewUser.getPhoneNumber());
        // Sex
        if (Objects.equals(previewUser.getSex(), "Masculin")){
            radioSexeMale.setSelected(true);
        }else {
            radioSexeFemale.setSelected(true);
        }
        txtSalaryMonth.setText(previewUser.getSalary1() + "");
        txtSalaryHour.setText(previewUser.getSalary2() + "");
        txtNumeroRue.setText(previewUser.getHouseNumber() + "");
        txtNumeroBoite.setText(previewUser.getLetterBoxNumber());
        txtZipCode.setText(previewUser.getPostalCode() + "");
        txtRegisterNumber.setText(previewUser.getNationalRegistreryNumber());
        // Dept
        if (Objects.equals(previewUser.getDepartement(), "Front Office")){
            radioDeptFO.setSelected(true);
        }else if (Objects.equals(previewUser.getDepartement(), "Back Office")){
            radioDeptBO.setSelected(true);
        }else if (Objects.equals(previewUser.getDepartement(), "House Keeping")){
            radioDeptHK.setSelected(true);
        }else if (Objects.equals(previewUser.getDepartement(), "Maintenance")){
            radioDeptMN.setSelected(true);
        }
        // Status
        if (Objects.equals(previewUser.getStatus(), "Ouvrier")){
            radioStOvrier.setSelected(true);
        }else if (Objects.equals(previewUser.getStatus(), "Employé")){
            radioStEmploye.setSelected(true);
        }else if (Objects.equals(previewUser.getStatus(), "Etudiant Ouvrier")){
            radioStEtudiantOuvrier.setSelected(true);
        }else if (Objects.equals(previewUser.getStatus(), "Etudiant Employé")){
            radioStEtudiantEmploye.setSelected(true);
        }else if (Objects.equals(previewUser.getStatus(), "Ouvrier Extra")){
            radioStOuvrierExtra.setSelected(true);
        }else if (Objects.equals(previewUser.getStatus(), "Etudiant Extra")){
            radioStEtudiantExtra.setSelected(true);
        }else if (Objects.equals(previewUser.getStatus(), "Ouvrier Flexi")){
            radioStOuvrierFlexi.setSelected(true);
        }else if (Objects.equals(previewUser.getStatus(), "Employé Flexi")){
            radioStEmployeFlexi.setSelected(true);
        }
        // Matrimonial
        if (Objects.equals(previewUser.getMaritalStatus(), "Célibataire")){
            radioSingle.setSelected(true);
        }else {
            radioMaried.setSelected(true);
        }
        datePickerStartService.setValue(Global.stringToLocalDate(previewUser.getDateInService()));
        if (previewUser.getDateOutService()!= null){
            datePickerEndService.setValue(Global.stringToLocalDate(previewUser.getDateOutService()));
        }
        txtPseudo.setText(previewUser.getPseudo());
        txtPassword.setText(previewUser.getPassword());
        txtIBAN.setText(previewUser.getIban());

    }

    private void makeFieldsNotEditable() {
        txtName.setEditable(false);
        txtSurname.setEditable(false);
        //datePicker.setDisable(true);
        txtEmail.setEditable(false);
        txtNumEmployee.setEditable(false);
        txtAddress.setEditable(false);
        comboCountry.setEditable(false);
        txtCity.setEditable(false);
        comboPhoneCountry.setEditable(false);
        txtPhoneNumber.setEditable(false);
        //radioSexeFemale.setDisable(true);
        //radioSexeMale.setDisable(true);
        txtSalaryMonth.setEditable(false);
        txtSalaryHour.setEditable(false);
        txtNumeroRue.setEditable(false);
        txtNumeroBoite.setEditable(false);
        txtZipCode.setEditable(false);
        txtRegisterNumber.setEditable(false);
        txtPseudo.setEditable(false);
        txtPassword.setEditable(false);
        btnSave.setDisable(true);
        txtIBAN.setEditable(false);
    }

    private void fillComboCountry() {
        List<Country> countriesList = Global.getCountriesList();

        for (Country c : countriesList) {
            countriesNamesList.add(c.getName());
            countriesCodeList.add(c.getCode());
        }
        // set Country elements
        countriesNamesList.sort(Comparator.naturalOrder());
        comboCountry.setItems(countriesNamesList);
        comboCountry.getSelectionModel().selectFirst();
    }

    private void fillComboPhoneCountry() {
        // order alphabetic order Collections.sort(names);  list.sort(Comparator.naturalOrder());
        countriesCodeList.sort(Comparator.naturalOrder());
        comboPhoneCountry.setItems(countriesCodeList);
        comboPhoneCountry.getSelectionModel().selectFirst();
    }

    private void saveUser() {
        // if button pressed name if details, add or edit
        if (Objects.equals(CollaboratorGlobal.getActionName(), "add")){

            // check if fields are well filled
            boolean areFieldsCorrect = checkAllFields();
            if (areFieldsCorrect){
                addCollaboratorToDB();
                // Back to userDashboard
                if (saveDone){
                    btnBack.getScene().getWindow().hide();
                    // nav with refresh
                    Global.closeWindow("Collaborateurs");
                    URL navPath = getClass().getResource("/sample/View/Collaborators/usersDashboard.fxml");
                    Global.navigateTo(navPath,"Collaborateurs");
                }else {
                    btnBack.getScene().getWindow().hide();
                }
            }

        }else if (Objects.equals(CollaboratorGlobal.getActionName(), "edit")){

            // check if fields are well filled
            boolean areFieldsCorrect = checkAllFields();
            if (areFieldsCorrect){
                updateCollaboratorInDB();
                // Back to userDashboard
                if (saveDone){
                    btnBack.getScene().getWindow().hide();
                    // nav with refresh
                    Global.closeWindow("Collaborateurs");
                    URL navPath = getClass().getResource("/sample/View/Collaborators/usersDashboard.fxml");
                    Global.navigateTo(navPath,"Collaborateurs");
                }else {
                    btnBack.getScene().getWindow().hide();
                }
            }
        }
    }

    private boolean checkAllFields() {
        boolean fieldsAreOk = false;

        //region Name
        if (txtName.getLength() == 0 || txtName.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Nom' entré est incorrect !");
        }
        //endregion

        //region Surname
        if (txtSurname.getLength() == 0 || txtSurname.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Prénom' entré est incorrect !");
        }
        //endregion

        //region Date
        if (datePicker.getValue() == null){
            // add error to list
            formErrorsList.add("La 'Date anniversaire' entrée est incorrecte !");
        }
        //endregion

        //region DateIn
        if (datePickerStartService.getValue() == null){
            // add error to list
            formErrorsList.add("La 'Date en service' entrée est incorrecte !");
        }
        //endregion

        //region Email
        if (txtEmail.getLength() == 0 || txtEmail.getText() == null ){
            // add error to list
            formErrorsList.add("L'adresse 'E-mail' entrée est incorrecte !");
        }
        //endregion

        //region Employee number
        if (txtNumEmployee.getLength() == 0 || txtNumEmployee.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Numéro Employé' entré est incorrect !");
        }
        //endregion

        //region Address
        if (txtAddress.getLength() == 0 || txtAddress.getText() == null){
            // add error to list
            formErrorsList.add("L' 'adresse' entrée est incorrecte !");
        }
        //endregion

        //region street Number
        if (txtNumeroRue.getLength() == 0 || txtNumeroRue.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Numéro Rue' entré est incorrect !");
        }
        //endregion

        //region Box number
        /*if (txtNumeroBoite.getLength() == 0 || txtNumeroBoite.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Numéro Boite' entré est incorrect !");
        }*/
        //endregion

        //region Ville
        if (txtCity.getLength() == 0 || txtCity.getText() == null){
            // add error to list
            formErrorsList.add("La 'ville' entrée est incorrecte !");
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
            txtSalaryMonth.setText(0 + "");
        }
        //endregion

        //region salary Hour
        if (txtSalaryHour.getLength() == 0 || txtSalaryHour.getText() == null){
            txtSalaryHour.setText(0 + "");
        }
        //endregion

        //region Zip code
        if (txtZipCode.getLength() == 0 || txtZipCode.getText() == null){
            // add error to list
            formErrorsList.add("Le 'Code Postal' entré est incorrect !");
        }
        //endregion

        //region National Registry
        if (txtRegisterNumber.getLength() == 0 || txtRegisterNumber.getText() == null ||
                !txtRegisterNumber.getText().matches("([0-9]{2})\\.([0-9]{2})\\.([0-9]{2})-([0-9]{3})\\.([0-9]{2})")){
            // add error to list
            formErrorsList.add("Le 'Numero National' entré est incorrect!");
            formErrorsList.add("Format souhaité: XX.XX.XX-XXX.XX");
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
        //endregion

        //region iban
        if (txtIBAN.getLength() == 0 || txtIBAN.getText() == null){
            // add error to list
            formErrorsList.add("Le 'numero IBAN' entré est incorrect !");
        }
        //endregion

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

        user = new User();
        dbHandler = new DBHandler();

        //region get all data from fields
        // name
        user.setFirstName(txtName.getText());
        // surname
        user.setLastName(txtSurname.getText());
        // birthday
        user.setBirthday(Global.localDateToString(datePicker.getValue()));
        // check
        user.setEmail(txtEmail.getText());
        user.setEmployeeNumber(Integer.parseInt(txtNumEmployee.getText()));
        user.setAddress(txtAddress.getText());
        // combo country
        user.setCountry(comboCountry.getSelectionModel().getSelectedItem());
        // city
        user.setCity(txtCity.getText());
        // telephone
        user.setPhoneCountry(comboPhoneCountry.getSelectionModel().getSelectedItem());
        user.setPhoneNumber(txtPhoneNumber.getText());
        // radio sex
        RadioButton selectedSex = (RadioButton) sexGroup.getSelectedToggle();
        String selectedItemName = selectedSex.getText();
        user.setSex(selectedItemName);
        // salary 1
        user.setSalary1(Global.roundDouble(Float.parseFloat(txtSalaryMonth.getText())));
        // salary 2
        user.setSalary2(Global.roundDouble(Float.parseFloat(txtSalaryHour.getText())));
        // house number
        user.setHouseNumber(Integer.parseInt(txtNumeroRue.getText()));
        // letterBox
        user.setLetterBoxNumber(txtNumeroBoite.getText());
        // zip Code
        user.setPostalCode(Integer.parseInt(txtZipCode.getText()));
        // check registry number
        user.setNationalRegistreryNumber(txtRegisterNumber.getText());
        // radio department
        RadioButton selectedDepartment = (RadioButton) departementGroup.getSelectedToggle();
        selectedItemName = selectedDepartment.getText();
        user.setDepartement(selectedItemName);
        // radio status
        RadioButton selectedStatus = (RadioButton) statusGroup.getSelectedToggle();
        selectedItemName = selectedStatus.getText();
        user.setStatus(selectedItemName);
        // radio matrimonial
        RadioButton selectedMatrimonial = (RadioButton) statusMaritaireGroup.getSelectedToggle();
        selectedItemName = selectedMatrimonial.getText();
        user.setMaritalStatus(selectedItemName);
        // inService
        user.setDateInService(Global.localDateToString(datePickerStartService.getValue()));
        // OutService
        if (datePickerEndService.getValue() != null){
            user.setDateOutService(Global.localDateToString(datePickerEndService.getValue()));
        }
        // pseudo
        user.setPseudo(txtPseudo.getText());
        // password
        user.setPassword(txtPassword.getText());
        // iban
        user.setIban(txtIBAN.getText());
        //endregion

        DialogController wd = new DialogController(btnBack.getScene().getWindow(), "Enrégistrement...");

        wd.exec("123", inputParam -> {

            // send to DB
            dbHandler.createUser(user);

            Platform.runLater(() ->{
                // show notification
                Global.successSystemNotif(
                        "Opération réussie!",
                        "#f7a631");
            });

            return 1;
        });

        // Tell app save is made
        saveDone = true;
    }

    private void updateCollaboratorInDB() {
        dbHandler = new DBHandler();
        user = new User();

        //region get all data from fields
        // name
        user.setFirstName(txtName.getText());
        // surname
        user.setLastName(txtSurname.getText());
        // birthday
        user.setBirthday(Global.localDateToString(datePicker.getValue()));
        // check
        user.setEmail(txtEmail.getText());
        user.setEmployeeNumber(Integer.parseInt(txtNumEmployee.getText()));
        user.setAddress(txtAddress.getText());
        // combo country
        user.setCountry(comboCountry.getSelectionModel().getSelectedItem());
        // city
        user.setCity(txtCity.getText());
        // telephone
        user.setPhoneCountry(comboPhoneCountry.getSelectionModel().getSelectedItem());
        user.setPhoneNumber(txtPhoneNumber.getText());
        // radio sex
        RadioButton selectedSex = (RadioButton) sexGroup.getSelectedToggle();
        String selectedItemName = selectedSex.getText();
        user.setSex(selectedItemName);
        // salary 1
        user.setSalary1(Global.roundDouble(Float.parseFloat(txtSalaryMonth.getText())));
        // salary 2
        user.setSalary2(Global.roundDouble(Float.parseFloat(txtSalaryHour.getText())));
        // house number
        user.setHouseNumber(Integer.parseInt(txtNumeroRue.getText()));
        // letterBox
        user.setLetterBoxNumber(txtNumeroBoite.getText());
        // zip Code
        user.setPostalCode(Integer.parseInt(txtZipCode.getText()));
        // check registry number
        user.setNationalRegistreryNumber(txtRegisterNumber.getText());
        // radio department
        RadioButton selectedDepartment = (RadioButton) departementGroup.getSelectedToggle();
        selectedItemName = selectedDepartment.getText();
        user.setDepartement(selectedItemName);
        // radio status
        RadioButton selectedStatus = (RadioButton) statusGroup.getSelectedToggle();
        selectedItemName = selectedStatus.getText();
        user.setStatus(selectedItemName);
        // radio matrimonial
        RadioButton selectedMatrimonial = (RadioButton) statusMaritaireGroup.getSelectedToggle();
        selectedItemName = selectedMatrimonial.getText();
        user.setMaritalStatus(selectedItemName);
        // inService
        user.setDateInService(Global.localDateToString(datePickerStartService.getValue()));
        // OutService
        if (datePickerEndService.getValue() != null){
            user.setDateOutService(Global.localDateToString(datePickerEndService.getValue()));
        }

        // pseudo
        user.setPseudo(txtPseudo.getText());
        // password
        user.setPassword(txtPassword.getText());
        // iban
        user.setIban(txtIBAN.getText());
        //endregion

        DialogController wd = new DialogController(btnBack.getScene().getWindow(), "Mise à jour...");

        wd.exec("123", inputParam -> {

            // send to DB
            dbHandler.updateUser(user);

            Platform.runLater(() ->{
                // show notification
                Global.successSystemNotif(
                        "Opération réussie!",
                        "#f7a631");
            });

            return 1;
        });

        // Tell app save is made
        saveDone = true;
    }
}
