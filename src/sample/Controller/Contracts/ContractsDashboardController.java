package sample.Controller.Contracts;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
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
import sample.Model.User;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

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

        // format fields
        Global.txtFormater(txtJobTitle, 20,0, 1);
        Global.txtFormater(txtJobDescription, 20,0, 1);
        Global.txtFormater(txtJobCategory, 20,0, 1);
        Global.txtFormater(txtHoursPerWeek, 10,2, 3);


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

        if (!txtJobCategory.getText().isEmpty() &&
                !txtHoursPerWeek.getText().isEmpty() &&
                !txtJobTitle.getText().isEmpty() &&
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

        //region Read txt file
        ArrayList<String> fileLines = new ArrayList<>();
        try {
            FileInputStream fis=new FileInputStream("src/sample/Ressources/documents/CDD_EMPLOYES_VARIABLE.txt");
            Scanner sc=new Scanner(fis);    //file to be scanned
            //returns true if there is another line to read
            while(sc.hasNextLine()){
                fileLines.add(sc.nextLine());
            }
            sc.close();     //closes the scanner
        }
        catch(IOException e){
            e.printStackTrace();
        }
        //endregion

        //region PDF Generation
        // variables
        User selectedUser = comboUser.getSelectionModel().getSelectedItem();
        String collaboratorName = selectedUser.getFirstName() + " " + selectedUser.getLastName() ;
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
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(27), textNormal));
            p.add(new Phrase(" " + txtJobTitle.getText(), textBold));
            pdf.add(p);

            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(28), textNormal));
            p.add(new Phrase(" " + txtJobCategory.getText(), textBold));
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
}
