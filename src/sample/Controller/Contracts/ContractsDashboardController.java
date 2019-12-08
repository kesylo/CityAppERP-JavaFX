package sample.Controller.Contracts;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Controller.DialogController;
import sample.Controller.Global.ContractGlobal;
import sample.Controller.Global.Global;
import sample.Database.DBHandler;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContractsDashboardController {

    //region UI
    @FXML
    private Label lblConnectedUser;

    @FXML
    private JFXComboBox<String> comboUser;

    @FXML
    private JFXButton btnhide;

    @FXML
    private JFXRadioButton radioCDDEmp;

    @FXML
    private JFXRadioButton radioCDDEtuEmpl;

    @FXML
    private JFXRadioButton radioCDDFlexiEmpl;

    @FXML
    private JFXRadioButton radioCDDFlexiOuvr;

    @FXML
    private JFXTextField txtJobTitle;

    @FXML
    private JFXTextField txtJobCategory;

    @FXML
    private JFXDatePicker datePickerStartContract;

    @FXML
    private JFXDatePicker datePickerEndContract;

    @FXML
    private JFXTextField txtHoursPerWeek;

    @FXML
    private JFXDatePicker datePickerConvention;

    @FXML
    private JFXTextField txtJobDescription;

    @FXML
    private JFXButton btnBack1;

    @FXML
    private JFXButton btnGenerate;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;
    //endregion

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnhide);
    }

    // some variables
    private String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    private String contractsFolder = "Contracts";
    private DBHandler dbHandler = new DBHandler();
    private DialogController<String> wd = null;

    @FXML
    void initialize() {

        Global.setUserProfile(lblConnectedUser, btnLogOut);

        // set profile photo
        Global.setProfileIcon(photo);

        // get users list
        loadUserList();


        // radio buttons
        radioCDDEmp.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                txtJobDescription.setDisable(true);
                datePickerConvention.setVisible(false);
                // what i need to be active
                txtJobCategory.setDisable(false);
                txtHoursPerWeek.setDisable(false);
                txtJobTitle.setDisable(false);
            }
        });

        radioCDDEtuEmpl.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                txtJobDescription.setDisable(true);
                datePickerConvention.setVisible(false);
                // what i need to be active
                txtJobCategory.setDisable(false);
                txtHoursPerWeek.setDisable(false);
                txtJobTitle.setDisable(false);
            }
        });

        radioCDDFlexiEmpl.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                txtJobCategory.setDisable(true);
                txtHoursPerWeek.setDisable(true);
                txtJobTitle.setDisable(true);
                // what i need to be active
                datePickerConvention.setVisible(true);
                txtJobDescription.setDisable(false);
            }
        });

        radioCDDFlexiOuvr.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                txtJobCategory.setDisable(true);
                txtHoursPerWeek.setDisable(true);
                txtJobTitle.setDisable(true);
                // what i need to be active
                datePickerConvention.setVisible(true);
                txtJobDescription.setDisable(false);
            }
        });

        // action buttons
        btnGenerate.setOnAction(event -> {
            if (radioCDDEmp.isSelected()){
                createEmployeeContract();
            }else if (radioCDDEtuEmpl.isSelected()){
                createStudentEmplContract();
            }else if (radioCDDFlexiEmpl.isSelected()){
                createFlexiEmployeeContract();
            }else if (radioCDDFlexiOuvr.isSelected()){
                createFlexiWorkerContract();
            }
        });

        btnBack1.setOnAction(event -> {
            URL navPath = getClass().getResource("/sample/View/dashboard.fxml");
            Global.navigateTo(navPath,"Dashboard");
        });

    }

    private void createFlexiEmployeeContract() {

    }

    private void createFlexiWorkerContract() {

    }

    private void createStudentEmplContract() {

    }

    private void createEmployeeContract() {

        if (!txtJobCategory.getText().isEmpty() && !txtHoursPerWeek.getText().isEmpty() && !txtJobTitle.getText().isEmpty()){

            // create contract folder on each os
            createContractDir();

            // create the pdf
            buildEmplPDF();

            // success message
            Global.showInfoMessage("Contrat généré!", "Votre contrat se trouve dans le répertoire "
                    + ContractGlobal.getContractsPath());

        }else {
            Global.showErrorMessage("Erreur sur le formulaire", "Remplissez tous les champs disponibles");
        }
    }

    private void createContractDir() {
        if (Global.isWindows()){
            File path = new File(myDocumentsPath + "\\" + contractsFolder);
            // set globally
            ContractGlobal.setContractsPath(path.toString());
            path.mkdirs();

        }else if (Global.isUnix()){
            System.out.println("linux");
        }
    }

    private void loadUserList(){

        Platform.runLater(() ->{
            wd = new DialogController<>(btnBack1.getScene().getWindow(), "Chargement des utilisateurs...");

            wd.exec("123", inputParam -> {

                ObservableList<String> usersList = FXCollections.observableArrayList();
                String firstName;
                String lastName;
                String fullName;
                ResultSet userRow = dbHandler.getActiveEmployeesNames();

                try {
                    while (userRow.next()){
                        firstName = userRow.getString("firstName");
                        lastName = userRow.getString("lastName");
                        fullName = firstName + " " + lastName;
                        usersList.add(fullName);
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    comboUser.setItems(usersList);
                    comboUser.getSelectionModel().selectFirst();
                });

                return 1;
            });
        });
    }

    /* ==============================================================================================================================*/

    private void buildEmplPDF(){
        // variables
        String collaboratorName = comboUser.getSelectionModel().getSelectedItem();
        String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        String contractsFolder = "Contracts";
        String contractFileName = "Contrat-Employe-";
        // pdf generation
        Document pdf = new Document();
        try {

            PdfWriter writer = PdfWriter.getInstance(pdf,
                    new FileOutputStream(myDocumentsPath
                            +"\\"
                            + contractsFolder
                            +"\\"
                            + contractFileName
                            + collaboratorName
                            + ".pdf"));
            pdf.open();

            // ========================================================================== PDF START
            Font font = new Font();
            font.setStyle(Font.BOLD);
            font.setSize(8);

            // Header
            pdf.add(new Paragraph("CONTRAT DE TRAVAIL A DUREE DETERMINEE POUR EMPLOYES", font));

            pdf.close();
            writer.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
