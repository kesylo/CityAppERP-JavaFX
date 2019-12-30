package sample.Controller.Contracts;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Controller.DialogController;
import sample.Global.ContractGlobal;
import sample.Global.Global;
import sample.Database.DBHandler;
import sample.Model.User;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ContractsDashboardController {

    //region UI
    @FXML
    private Label lblConnectedUser;

    @FXML
    private JFXComboBox<User> comboUser;

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
    private JFXComboBox<String> comboCategory;

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

    @FXML
    void onChange(ActionEvent event) {
        String dept = comboUser.getSelectionModel().getSelectedItem().getDepartement();
        ObservableList<String> categories = FXCollections.observableArrayList();
        switch (dept) {
            case "Front Office":
                categories.clear();
                categories.add("306 - Réceptionniste/préposé service clientèle/chef de réception adjoint");
                categories.add("307 - Responsable de réception/chef des réceptionnistes/chef de la réception");
                categories.add("310 - Responsable des réservations/superviseur(euse) des réservations");
                categories.add("316 - Réceptionniste de nuit");
                categories.add("317 - Veilleur(euse) de nuit");
                categories.add("318A - Agent de sécurité/surveillant(e)");
                categories.add("318B - Chef de sécurité/surveillant(e)-chef");
                comboCategory.setItems(categories);
                comboCategory.getSelectionModel().selectFirst();
                break;
            case "Back Office":
                categories.clear();
                categories.add("806 - Assistant gérant");
                categories.add("807 - Front office manager/chef de réception");
                categories.add("818 - (Chef) comptable");
                categories.add("822 - Ingénieur de système (system-operator)");
                categories.add("824 - Collaborateur(trice) commercial(e)/représentant(e)/responsable de la promotion vente/responsable de marketing/agent commercial");
                categories.add("825 - Collaborateur(trice) PR marketing - publicité");
                comboCategory.setItems(categories);
                comboCategory.getSelectionModel().selectFirst();
                break;
            case "House Keeping":
                categories.clear();
                categories.add("500A - Femme/valet de chambre");
                categories.add("500B - Gouverneur/gouvernante d'étage");
                categories.add("501 - Nettoyeur(euse)");
                categories.add("502 - Responsable des nettoyeurs");
                categories.add("503 - Préposé(e) linge");
                categories.add("507 - Gouverneur/gouvernante générale");
                comboCategory.setItems(categories);
                comboCategory.getSelectionModel().selectFirst();
                break;
            case "Maintenance":
                categories.clear();
                categories.add("703 - Menuisier");
                categories.add("704 - Electricien(ne)");
                categories.add("705 - Plombier");
                categories.add("706 - Peintre");
                categories.add("709 - Responsable service technique");
                comboCategory.setItems(categories);
                comboCategory.getSelectionModel().selectFirst();
                break;
            default:
                categories.clear();
                comboCategory.setItems(categories);
                Global.showInfoMessage("Departement non précisé", "Cet utilisateur n'a pas de département valide. veuillez lui en attribuer un dans les profils");
                break;
        }
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

        // format fields
        Global.txtFormater(txtJobDescription, 100,0, 1);
        Global.txtFormater(txtHoursPerWeek, 10,2, 3);


        // radio buttons
        radioCDDEmp.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                txtJobDescription.setDisable(true);
                datePickerConvention.setVisible(false);
                // what i need to be active
                comboCategory.setDisable(false);
                txtHoursPerWeek.setDisable(false);
            }
        });

        radioCDDEtuEmpl.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                txtJobDescription.setDisable(true);
                datePickerConvention.setVisible(false);
                // what i need to be active
                comboCategory.setDisable(false);
                txtHoursPerWeek.setDisable(false);
            }
        });

        radioCDDFlexiEmpl.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                comboCategory.setDisable(true);
                txtHoursPerWeek.setDisable(true);
                // what i need to be active
                datePickerConvention.setVisible(true);
                txtJobDescription.setDisable(false);
            }
        });

        radioCDDFlexiOuvr.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                // what i don't need to be active
                comboCategory.setDisable(true);
                txtHoursPerWeek.setDisable(true);
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
        try {
            if (comboCategory.getSelectionModel().getSelectedItem()!= null
                    && !txtHoursPerWeek.getText().isEmpty() &&
                    datePickerStartContract.getValue() != null &&
                    datePickerEndContract.getValue() != null){

                // create contract folder on each os
                createContractDir();

                // create the pdf
                if (buildEtudiantEmplPDF()){
                    // success message
                    Global.showInfoMessage("Contrat généré!", "Votre contrat se trouve dans le répertoire "
                            + ContractGlobal.getContractsPath());
                }

            }else {
                Global.showErrorMessage("Erreur sur le formulaire", "Remplissez tous les champs disponibles");
            }
        } catch (Exception e){
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }

    private void createEmployeeContract() {
        try {
            if (comboCategory.getSelectionModel().getSelectedItem()!= null
                    && !txtHoursPerWeek.getText().isEmpty() &&
                    datePickerStartContract.getValue() != null &&
                    datePickerEndContract.getValue() != null){

                // create contract folder on each os
                createContractDir();

                // create the pdf
                if (buildEmplPDF()){
                    // success message
                    Global.showInfoMessage("Contrat généré!", "Votre contrat se trouve dans le répertoire "
                            + ContractGlobal.getContractsPath());
                }

            }else {
                Global.showErrorMessage("Erreur sur le formulaire", "Remplissez tous les champs disponibles");
            }
        } catch (Exception e){
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }

    private void createContractDir() {
        try {
            if (Global.isWindows()){
                File path = new File(myDocumentsPath + "\\" + contractsFolder);
                // set globally
                ContractGlobal.setContractsPath(path.toString());
                path.mkdirs();

            }else if (Global.isUnix()){
                System.out.println("linux");
            }
        } catch (Exception e) {
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }

    private void loadUserList(){

        Platform.runLater(() ->{
            wd = new DialogController<>(btnBack1.getScene().getWindow(), "Chargement des utilisateurs...");

            wd.exec("123", inputParam -> {

                ObservableList<User> userList = FXCollections.observableArrayList();
                ResultSet userRow = dbHandler.getActiveEmployees();

                try {
                    while (userRow.next()){
                        // create users list
                        User user = new User(
                                userRow.getInt("id"),
                                userRow.getString("address"),
                                userRow.getString("city"),
                                userRow.getString("CivilStatus"),
                                userRow.getString("dept"),
                                userRow.getString("email"),
                                userRow.getString("firstName"),
                                userRow.getInt("houseNum"),
                                userRow.getString("inService"),
                                userRow.getString("lastName"),
                                userRow.getString("letterBoxNum"),
                                userRow.getString("nationalRegisterNum"),
                                userRow.getString("outService"),
                                userRow.getString("password"),
                                userRow.getInt("postalCode"),
                                userRow.getString("pseudo"),
                                userRow.getString("sex"),
                                userRow.getInt("role"),
                                userRow.getString("phoneNumber"),
                                userRow.getDouble("salary1"),
                                userRow.getDouble("salary2"),
                                userRow.getString("status"),
                                userRow.getInt("employeeNumber"),
                                userRow.getString("birthday"),
                                userRow.getString("phoneCountry"),
                                userRow.getString("country"),
                                userRow.getString("iban")
                        );
                        userList.add(user);
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    comboUser.setItems(userList);
                    comboUser.getSelectionModel().selectFirst();
                });

                return 1;
            });
        });
    }

    /* ==============================================================================================================================*/

    private Boolean buildEmplPDF(){
        String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();

        //region Read txt file
        ArrayList<String> fileLines = new ArrayList<>();
        File file = new File(myDocumentsPath + "/ERPDocs/CDD_EMPLOYES_VARIABLE.txt");

        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                fileLines.add(line);
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }

        //endregion

        //region PDF Generation
        // variables
        User selectedUser = comboUser.getSelectionModel().getSelectedItem();
        String collaboratorName = selectedUser.getFirstName() + " " + selectedUser.getLastName() ;
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

            Paragraph p;
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA);
            headerFont.setStyle(Font.BOLD);
            headerFont.setSize(18);

            Font textBold = FontFactory.getFont(FontFactory.HELVETICA);
            textBold.setStyle(Font.BOLD);
            textBold.setSize(12);

            Font textNormal = FontFactory.getFont(FontFactory.HELVETICA);
            textNormal.setStyle(Font.NORMAL);
            textNormal.setSize(12);

            // ======================================================================================== HEADER
            p = new Paragraph(fileLines.get(0), headerFont);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(25);
            pdf.add(p);

            p = new Paragraph(fileLines.get(3), textNormal);
            pdf.add(p);

            PdfPTable table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(4), textNormal));
            table.addCell(new Phrase("TRANS TECHNICS SERVICES", textBold));
            table.addCell(new Phrase(fileLines.get(5), textNormal));
            table.addCell(new Phrase("Rue de la Fourche, 8", textBold));
            table.addCell(" ");
            table.addCell(new Phrase("1000 – BRUXELLES 1", textBold));
            table.addCell(" ");
            table.addCell(new Phrase("RPM: BE0.422.634.443", textBold));
            table.addCell(" ");
            table.addCell(new Phrase("ONSS: 0488.450-28", textBold));
            table.addCell(new Phrase(fileLines.get(9), textNormal));
            table.addCell(new Phrase("Christian Drappier", textBold));
            p.add(table);
            p.setIndentationLeft(20);
            pdf.add(p);

            p = new Paragraph(fileLines.get(11), textNormal);
            pdf.add(p);

            // add user data in this section
            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(12), textNormal));
            table.addCell(new Phrase(collaboratorName, textBold));
            table.addCell(new Phrase(fileLines.get(13), textNormal));
            table.addCell(new Phrase(selectedUser.getNationalRegistreryNumber(), textBold));
            table.addCell(new Phrase(fileLines.get(14), textNormal));
            table.addCell(new Phrase(selectedUser.getAddress() + " "
                    + selectedUser.getHouseNumber() + " "
                    + selectedUser.getPostalCode() + " "
                    + selectedUser.getCity(),
                    textBold));
            table.addCell(new Phrase(fileLines.get(15), textNormal));
            table.addCell(new Phrase(selectedUser.getPhoneNumber(), textBold));
            table.addCell(new Phrase(fileLines.get(16), textNormal));
            table.addCell(new Phrase(selectedUser.getEmail(), textBold));
            table.addCell(new Phrase(fileLines.get(17), textNormal));
            table.addCell(new Phrase(selectedUser.getIban(), textBold));
            table.addCell(new Phrase(fileLines.get(21), textNormal));
            table.addCell("");
            p.add(table);
            p.setIndentationLeft(20);
            pdf.add(p);
            pdf.add( Chunk.NEWLINE );

            p = new Paragraph(new Phrase(fileLines.get(22), textNormal));
            pdf.add(p);

            // ======================================================================================== BODY
            pdf.add( Chunk.NEWLINE );
            ZapfDingbatsList list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(25), textBold)));
            pdf.add(list);

            // add user data in this section
            String categoryValue = comboCategory.getSelectionModel().getSelectedItem();
            String cat[] = categoryValue.split(" - ");

            p = new Paragraph();
            p.add(new Phrase(fileLines.get(27), textNormal));
            p.add(new Phrase(" " + cat[1], textBold));
            pdf.add(p);

            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(28), textNormal));
            p.add(new Phrase(" " + cat[0], textBold));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(30), textNormal));
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(32), textBold)));
            pdf.add(list);

            // add user data in this section
            table = new PdfPTable(4);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(34), textNormal));
            table.addCell(new Phrase(datePickerStartContract.getValue().toString(), textBold));
            table.addCell(new Phrase("au", textNormal));
            table.addCell(new Phrase(datePickerEndContract.getValue().toString(), textBold));
            p.add(table);
            float[] columnWidths = new float[]{70f, 13f, 3.5f, 14.5f};
            table.setWidths(columnWidths);
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(37), textBold)));
            pdf.add(list);

            // add user data in this section
            table = new PdfPTable(3);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(39), textNormal));
            table.addCell(new Phrase(" " + selectedUser.getSalary1(), textBold));
            table.addCell(new Phrase("€ par mois.", textNormal));
            p.add(table);
            columnWidths = new float[]{47f, 10f, 43f};
            table.setWidths(columnWidths);
            pdf.add(p);

            p = new Paragraph(new Phrase(fileLines.get(41), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(43), textNormal));
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(45), textBold)));
            pdf.add(list);

            // add user data in this section
            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(47) + " ", textNormal));
            p.add(new Phrase(" " + txtHoursPerWeek.getText() , textBold));
            p.add(new Phrase(" heures sera réalisée sur base annuelle.", textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(49), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(51), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(53), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(55), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(57), textNormal));
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(60), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(62), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(64), textNormal));
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(66), textBold)));
            pdf.add(list);

            p = new Paragraph(new Phrase(fileLines.get(68), textNormal));
            pdf.add(p);

            // ======================================================================================== FOOTER

            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(73), textNormal));
            p.add(new Phrase(Global.getSystemDate() + ".", textBold));
            pdf.add(p);

            p = new Paragraph(new Phrase(fileLines.get(74), textNormal));
            p.setSpacingAfter(30);
            pdf.add(p);


            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(77), textBold));
            table.addCell(new Phrase(fileLines.get(78), textBold));
            table.addCell(new Phrase(fileLines.get(79), textNormal));
            table.addCell(new Phrase(fileLines.get(80), textNormal));
            table.addCell(new Phrase(fileLines.get(81), textNormal));
            table.addCell(new Phrase(fileLines.get(82), textNormal));
            p.add(table);
            columnWidths = new float[]{50f, 50f};
            table.setWidths(columnWidths);
            p.setSpacingAfter(50);
            pdf.add(p);

            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(83), textNormal));
            table.addCell(new Phrase(fileLines.get(83), textNormal));
            p.add(table);
            columnWidths = new float[]{50f, 50f};
            table.setWidths(columnWidths);
            pdf.add(p);

            pdf.close();
            writer.close();

        } catch (DocumentException e) {
            Global.showErrorMessage("Erreur lors de la création", "Impossible de créer le PDF");
            return false;
        } catch (FileNotFoundException e) {
            Global.showErrorMessage("Erreur lors de la génération", "Veuillez fermer le fichier pdf avant de générer.");
            return false;
        }
        //endregion

        return true;
    }

    private Boolean buildEtudiantEmplPDF(){
        String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();

        //region Read txt file
        ArrayList<String> fileLines = new ArrayList<>();
        File file = new File(myDocumentsPath + "/ERPDocs/CDD_ETUDIANTS_EMPLOYES_VARIABLE.txt");

        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                fileLines.add(line);
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }

        //endregion

        //region PDF Generation
        // variables
        // Change Here
        User selectedUser = comboUser.getSelectionModel().getSelectedItem();
        String collaboratorName = selectedUser.getFirstName() + " " + selectedUser.getLastName() ;
        String contractsFolder = "Contracts";
        String contractFileName = "Contrat-Etudiant-Employe-";
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

            Paragraph p;
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA);
            headerFont.setStyle(Font.BOLD);
            headerFont.setSize(18);

            Font textBold = FontFactory.getFont(FontFactory.HELVETICA);
            textBold.setStyle(Font.BOLD);
            textBold.setSize(12);

            Font textNormal = FontFactory.getFont(FontFactory.HELVETICA);
            textNormal.setStyle(Font.NORMAL);
            textNormal.setSize(12);

            // ======================================================================================== HEADER
            p = new Paragraph(fileLines.get(0), headerFont);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(25);
            pdf.add(p);

            p = new Paragraph(fileLines.get(3), textNormal);
            pdf.add(p);

            PdfPTable table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(4), textNormal));
            table.addCell(new Phrase("TRANS TECHNICS SERVICES", textBold));
            table.addCell(new Phrase(fileLines.get(5), textNormal));
            table.addCell(new Phrase("Rue de la Fourche, 8", textBold));
            table.addCell(" ");
            table.addCell(new Phrase("1000 – BRUXELLES 1", textBold));
            table.addCell(" ");
            table.addCell(new Phrase("RPM: BE0.422.634.443", textBold));
            table.addCell(" ");
            table.addCell(new Phrase("ONSS: 0488.450-28", textBold));
            table.addCell(new Phrase(fileLines.get(9), textNormal));
            table.addCell(new Phrase("Christian Drappier", textBold));
            p.add(table);
            p.setIndentationLeft(20);
            pdf.add(p);

            p = new Paragraph(fileLines.get(11), textNormal);
            pdf.add(p);

            // add user data in this section
            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(12), textNormal));
            table.addCell(new Phrase(collaboratorName, textBold));
            table.addCell(new Phrase(fileLines.get(13), textNormal));
            table.addCell(new Phrase(selectedUser.getNationalRegistreryNumber(), textBold));
            table.addCell(new Phrase(fileLines.get(14), textNormal));
            table.addCell(new Phrase(selectedUser.getAddress() + " "
                    + selectedUser.getHouseNumber() + " "
                    + selectedUser.getPostalCode() + " "
                    + selectedUser.getCity(),
                    textBold));
            table.addCell(new Phrase(fileLines.get(15), textNormal));
            table.addCell(new Phrase(selectedUser.getPhoneNumber(), textBold));
            table.addCell(new Phrase(fileLines.get(16), textNormal));
            table.addCell(new Phrase(selectedUser.getEmail(), textBold));
            table.addCell(new Phrase(fileLines.get(17), textNormal));
            table.addCell(new Phrase(selectedUser.getIban(), textBold));
            table.addCell(new Phrase(fileLines.get(21), textNormal));
            table.addCell("");
            p.add(table);
            p.setIndentationLeft(20);
            pdf.add(p);
            pdf.add( Chunk.NEWLINE );

            p = new Paragraph(new Phrase(fileLines.get(22), textNormal));
            pdf.add(p);

            // ======================================================================================== BODY
            // Article 1
            pdf.add( Chunk.NEWLINE );
            ZapfDingbatsList list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(25), textBold)));
            pdf.add(list);

            // Change Here
            String categoryValue = comboCategory.getSelectionModel().getSelectedItem();
            String cat[] = categoryValue.split(" - ");

            p = new Paragraph();
            p.add(new Phrase(fileLines.get(27), textNormal));
            p.add(new Phrase(" " + cat[1], textBold));
            pdf.add(p);

            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(28), textNormal));
            p.add(new Phrase(" " + cat[0], textBold));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(30), textNormal));
            pdf.add(p);

            // Article 2
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(44), textBold)));
            pdf.add(list);

            // Change Here
            // add user data in this section
            table = new PdfPTable(4);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(46), textNormal));
            table.addCell(new Phrase(datePickerStartContract.getValue().toString(), textBold));
            table.addCell(new Phrase("au", textNormal));
            table.addCell(new Phrase(datePickerEndContract.getValue().toString(), textBold));
            p.add(table);
            float[] columnWidths = new float[]{70f, 13f, 3.5f, 14.5f};
            table.setWidths(columnWidths);
            pdf.add(p);

            // Article 3
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(61), textBold)));
            pdf.add(list);

            // add user data in this section
            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(63), textNormal)));
            p.add((new Phrase(fileLines.get(64), textNormal)));
            p.add((new Phrase(fileLines.get(65), textNormal)));
            p.add((new Phrase(fileLines.get(66), textNormal)));
            pdf.add(p);

            // Article 4
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(80), textBold)));
            pdf.add(list);

            // Change Here
            table = new PdfPTable(4);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(82), textNormal));
            table.addCell(new Phrase(selectedUser.getSalary2() + "", textBold));
            table.addCell(new Phrase(fileLines.get(83), textNormal));
            p.add(table);
            columnWidths = new float[]{70f, 13f, 3.5f, 14.5f};
            table.setWidths(columnWidths);
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(85), textNormal)));
            p.add((new Phrase(fileLines.get(86), textNormal)));
            p.add((new Phrase(fileLines.get(87), textNormal)));
            pdf.add(p);

            // Article 5
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(101), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(103), textNormal));
            p.add(new Phrase(fileLines.get(105), textNormal));
            pdf.add(p);

            // Change Here
            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(106), textNormal));
            p.add(new Phrase(" " + txtHoursPerWeek.getText(), textNormal));
            p.add(new Phrase(" heures " + fileLines.get(107), textBold));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(108), textNormal)));
            p.add((new Phrase(fileLines.get(109), textNormal)));
            p.add((new Phrase(fileLines.get(110), textNormal)));
            p.add((new Phrase(fileLines.get(111), textNormal)));
            p.add((new Phrase(fileLines.get(112), textNormal)));
            p.add((new Phrase(fileLines.get(113), textNormal)));
            p.add((new Phrase(fileLines.get(114), textNormal)));
            pdf.add(p);

            // Article 6
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(128), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(130), textNormal)));
            p.add((new Phrase(fileLines.get(131), textNormal)));
            pdf.add(p);

            // Article 7
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(145), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(147), textNormal)));
            p.add((new Phrase(fileLines.get(148), textNormal)));
            pdf.add(p);

            // Article 8
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(162), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.add((new Phrase(fileLines.get(164), textNormal)));
            pdf.add(p);

            // Article 9
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(178), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(180), textNormal)));
            p.add((new Phrase(fileLines.get(181), textNormal)));
            pdf.add(p);

            // Article 10
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(195), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.add((new Phrase(fileLines.get(197), textNormal)));
            pdf.add(p);

            // Article 11
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(211), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(213), textNormal)));
            p.add((new Phrase(fileLines.get(214), textNormal)));
            pdf.add(p);

            // ======================================================================================== FOOTER

            pdf.add( Chunk.NEWLINE );
            pdf.add( Chunk.NEWLINE );
            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(244), textNormal));
            p.add(new Phrase(Global.getSystemDate() + ".", textBold));
            pdf.add(p);

            p = new Paragraph(new Phrase(fileLines.get(245), textNormal));
            p.setSpacingAfter(30);
            pdf.add(p);


            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(248), textBold));
            table.addCell(new Phrase(fileLines.get(249), textBold));
            table.addCell(new Phrase(fileLines.get(250), textNormal));
            table.addCell(new Phrase(fileLines.get(251), textNormal));
            table.addCell(new Phrase(fileLines.get(252), textNormal));
            table.addCell(new Phrase(fileLines.get(253), textNormal));
            p.add(table);
            columnWidths = new float[]{50f, 50f};
            table.setWidths(columnWidths);
            p.setSpacingAfter(50);
            pdf.add(p);

            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(254), textNormal));
            table.addCell(new Phrase(fileLines.get(254), textNormal));
            p.add(table);
            columnWidths = new float[]{50f, 50f};
            table.setWidths(columnWidths);
            pdf.add(p);

            pdf.close();
            writer.close();

        } catch (DocumentException e) {
            Global.showErrorMessage("Erreur lors de la création", "Impossible de créer le PDF");
            return false;
        } catch (FileNotFoundException e) {
            Global.showErrorMessage("Erreur lors de la génération", "Veuillez fermer le fichier pdf avant de générer.");
            return false;
        }

        return true;
    }
}
