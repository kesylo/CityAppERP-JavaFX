package sample.Controller.Collaborators;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Controller.Global.CollaboratorGlobal;
import sample.Controller.Global.Global;

import java.net.URL;
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
    private JFXComboBox<String> comboPhoneCountry;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private JFXTextField txtSalaryHour;

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

    List<String> formErrorsList = new ArrayList<>();

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
        Global.txtFormater(txtEmail, 50,0, 2);
        Global.txtFormater(txtNumEmployee, 4,3, 3);
        Global.txtFormater(txtAddress, 50,0, 2);
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
        if (!Objects.equals(txtName.getText(), "") || txtName.getText() != null){
            fieldsAreOk = true;
        }else {
            // add error to list
            formErrorsList.add("Le 'Nom' entré est incorrect !");
        }
        //endregion

        //region Surname
        if (!Objects.equals(txtSurname.getText(), "") || txtSurname.getText() != null){
            fieldsAreOk = true;
        }else {
            // add error to list
            formErrorsList.add("Le 'Prenom' entré est incorrect !");
        }
        //endregion


        // show all errors if exists
        showAllErrors();

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
    }
}
