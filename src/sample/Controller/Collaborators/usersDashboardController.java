package sample.Controller.Collaborators;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sample.Controller.DialogController;
import sample.Controller.Global.CollaboratorGlobal;
import sample.Controller.Global.Global;
import sample.Database.DBHandler;
import sample.Model.User;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class usersDashboardController {

    //region UI
    @FXML
    private JFXTextField searchBar;

    @FXML
    private TableView<User> tableUsers;

    @FXML
    private TableColumn<User, String> clmName;

    @FXML
    private TableColumn<User, String> clmSurname;

    @FXML
    private TableColumn<User, String> clmSex;

    @FXML
    private TableColumn<User, String> clmDept;

    @FXML
    private TableColumn<User, String> clmPhone;

    @FXML
    private TableColumn<User, String> clmNationalNumber;

    @FXML
    private JFXButton btnCreate;

    @FXML
    private JFXButton btnDetails;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXButton btnBack;

    @FXML
    private Label lblConnectedUser;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private ImageView photo;
    //endregion

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnBack);
    }

    @FXML
    void selectAll() {
        searchBar.selectAll();
    }

    private DBHandler dbHandler = new DBHandler();
    private DialogController<String> wd = null;
    private ResultSet rs = null;
    FilteredList<User> filteredUser = null;

    @FXML
    void initialize() {

        // set profile
        Global.setUserProfile(lblConnectedUser, btnLogOut);

        // set profile photo
        Global.setProfileIcon(photo);

        // add users to table
        getAllUsers();

        // AutoSearch feature
        searchBar.setOnKeyReleased(event -> {
            searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredUser.setPredicate(user -> {
                    if (newValue == null || newValue.isEmpty()){
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (user.getFirstName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if (user.getLastName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else {
                        return false;
                    }
                });
            });

            SortedList<User> sortedUsers = new SortedList<>(filteredUser);

            sortedUsers.comparatorProperty().bind(tableUsers.comparatorProperty());

            tableUsers.setItems(sortedUsers);
        });

        btnBack.setOnAction(event -> {
            URL url = getClass().getResource("/sample/View/dashboard.fxml");
            Global.navigateTo(url, "Dashboard");
        });

        btnRefresh.setOnAction(event -> {
            getAllUsers();
        });

        btnCreate.setOnAction(event -> {
            // check if user is admin first
            if (Global.getConnectedUser().getRole() > 3){
                // specify which button is pressed
                CollaboratorGlobal.setActionName("add");

                URL url = getClass().getResource("/sample/View/Collaborators/addCollaborator.fxml");
                Global.navigateModal(url, "Ajouter-Collaborateur");
            } else {
                Global.showErrorMessage("Problème de droits",
                        "Vous n'avez pas le droit de créer un profil.");
            }
        });

        btnDetails.setOnAction(event -> {
            // set preview user globally
            CollaboratorGlobal.setPreviewUser(tableUsers.getSelectionModel().getSelectedItem());
            // specify which button is pressed
            CollaboratorGlobal.setActionName("details");

            URL url = getClass().getResource("/sample/View/Collaborators/addCollaborator.fxml");
            Global.navigateModal(url, "Details-Collaborateur");
        });

        btnEdit.setOnAction(event -> {
            // check if user is admin first
            if (Global.getConnectedUser().getRole() > 3){
                // set preview user globally
                CollaboratorGlobal.setPreviewUser(tableUsers.getSelectionModel().getSelectedItem());

                // load edit if user is not archived
                if (CollaboratorGlobal.getPreviewUser().getDateOutService() == null){
                    // user not archived
                    // specify which button is pressed
                    CollaboratorGlobal.setActionName("edit");

                    URL url = getClass().getResource("/sample/View/Collaborators/addCollaborator.fxml");
                    Global.navigateModal(url, "Modifier-Collaborateur");
                }else {
                    // user archived
                    Global.showErrorMessage("Modification du collaborateur impossible.",
                            "Ce collaborateur a déja été archivé !");
                }
            } else {
                Global.showInfoMessage("Problème de droits",
                        "Vous n'avez pas le droit de modifier un profil.");
            }
        });

        btnDelete.setOnAction(event -> {
            // check if user is admin first
            if (Global.getConnectedUser().getRole() > 4){
                // set preview user globally
                CollaboratorGlobal.setPreviewUser(tableUsers.getSelectionModel().getSelectedItem());
                boolean action = Global.showInfoMessageWithBtn(
                        "Archivage d'un collaborateur",
                        "Etes vous sûre de vouloir archiver le profil de " + CollaboratorGlobal.getPreviewUser().getFirstName() + " ?",
                        "Oui",
                        "Non");

                if (action){
                    archiveCollaborator();
                    // show notification
                    Global.successSystemNotif(
                            "Opération réussie!",
                            "#f7a631");
                    // refresh
                    getAllUsers();
                }
            } else {
                Global.showErrorMessage("Problème de droits",
                        "Vous n'avez pas le droit d'archiver un profil.");
            }
        });


    }

    /*-------------------------------------------------------------------------------*/

    public void clickItem() {
        tableUsers.setOnMouseClicked(event1 -> {
            if (event1.getClickCount() == 2){
                //System.out.println("Clicked on " + (tableUsers.getSelectionModel().getSelectedCells().get(0)).getRow());

                // set preview user globally
                CollaboratorGlobal.setPreviewUser(tableUsers.getSelectionModel().getSelectedItem());
                // specify which button is pressed
                CollaboratorGlobal.setActionName("details");

                URL url = getClass().getResource("/sample/View/Collaborators/addCollaborator.fxml");
                Global.navigateModal(url, "Details-Collaborateur");
            }
        });
    }

    private void getAllUsers() {
        Platform.runLater(() ->{
            // prepare loading screen
            wd = new DialogController<>(btnBack.getScene().getWindow(), "Chargement des collaborateurs...");

            wd.exec("123", inputParam -> {
                final ObservableList<User> data = longTask();
                // set users Globally
                CollaboratorGlobal.setUsersList(data);
                // turn users list in filtered list for search
                filteredUser = new FilteredList<>(data, b -> true);

                Platform.runLater(() ->{
                    fillTable(CollaboratorGlobal.getUsersList());
                });
                return 1;
            });
        });

    }

    private void fillTable(ObservableList<User> users) {
        clmDept.setCellValueFactory(new PropertyValueFactory<>("Departement"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        clmNationalNumber.setCellValueFactory(new PropertyValueFactory<>("NationalRegistreryNumber"));
        clmPhone.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        clmSex.setCellValueFactory(new PropertyValueFactory<>("Sex"));
        clmSurname.setCellValueFactory(new PropertyValueFactory<>("LastName"));

        // add list to table
        tableUsers.setItems(users);

        // select first
        tableUsers.getSelectionModel().selectFirst();
    }

    private ObservableList<User> longTask() {
        ObservableList<User> data = FXCollections.observableArrayList();
        rs = dbHandler.getAllEmployees();

        try {
            while (rs.next()){
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("CivilStatus"),
                        rs.getString("dept"),
                        rs.getString("email"),
                        rs.getString("firstName"),
                        rs.getInt("houseNum"),
                        rs.getString("inService"),
                        rs.getString("lastName"),
                        rs.getString("letterBoxNum"),
                        rs.getString("nationalRegisterNum"),
                        rs.getString("outService"),
                        rs.getString("password"),
                        rs.getInt("postalCode"),
                        rs.getString("pseudo"),
                        rs.getString("sex"),
                        rs.getInt("role"),
                        rs.getString("phoneNumber"),
                        rs.getDouble("salary1"),
                        rs.getDouble("salary2"),
                        rs.getString("status"),
                        rs.getInt("employeeNumber"),
                        rs.getString("birthday"),
                        rs.getString("phoneCountry"),
                        rs.getString("country")
                );
                data.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void archiveCollaborator() {
        dbHandler = new DBHandler();
        CollaboratorGlobal.getPreviewUser().setDateOutService(Global.getSystemDate());

        wd = new DialogController<>(btnEdit.getScene().getWindow(), "Archivage...");

        wd.exec("123", inputParam -> {

            // archive user
            dbHandler.archiveCollaborator(CollaboratorGlobal.getPreviewUser());

            return 1;
        });

    }
}
