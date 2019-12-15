package sample.Controller.Settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sample.Controller.DialogController;
import sample.Database.DBHandler;
import sample.Global.CollaboratorGlobal;
import sample.Global.Global;
import sample.Global.SettingsGlobal;
import sample.Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EditPermissionsController {

    //region UI
    @FXML
    private JFXComboBox<User> comboUser;

    @FXML
    private JFXRadioButton radioAdmin;

    @FXML
    private JFXRadioButton radioAdvUser;

    @FXML
    private JFXRadioButton radioNormalUser;

    @FXML
    private JFXRadioButton radioGuest;

    @FXML
    private JFXButton btnSave;
    //endregion

    private DBHandler dbHandler = new DBHandler();
    private DialogController<String> wd = null;
    private User selectedUser = new User();

    @FXML
    void comboAction() {
        setCurrentUserData();
    }

    @FXML
    void initialize() {

        // get users list
        loadUserList();

        // save new permissions
        btnSave.setOnAction(event -> {
            // save desired permissions
            savePermissionToDB();
            // show notification
            Global.showInfoMessage("Droits et Permissions",
                    "La mise à jour a été effectuée avec succès!");

            btnSave.getScene().getWindow().hide();
        });

    }

    private ObservableList<User> longTask(){
        ObservableList<User> data = FXCollections.observableArrayList();
        ResultSet userRow = dbHandler.getActiveEmployees();
        try {
            while (userRow.next()){
                // create users list
                User user = new User();
                user.setId(userRow.getInt("id"));
                user.setRole( userRow.getInt("role"));
                user.setFirstName( userRow.getString("firstName"));
                user.setLastName(userRow.getString("lastName"));

                data.add(user);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return data;
    }

    private void savePermissionToDB() {
        wd = new DialogController<>(btnSave.getScene().getWindow(), "Mise à Jour en Cours...");
        wd.exec("123", inputParam -> {
            //dbHandler.archiveCollaborator(CollaboratorGlobal.getPreviewUser());
            int role = getSelectedRole();
            dbHandler.updateRole(selectedUser, role);

            return 1;
        });
    }

    private int getSelectedRole() {
        if (radioAdmin.isSelected()){
            return 5;
        }else if (radioAdvUser.isSelected()){
            return 4;
        }else if (radioNormalUser.isSelected()){
            return 3;
        }else {
            return 2;
        }
    }

    private void loadUserList() {
        Platform.runLater(() ->{
            // prepare loading screen
            wd = new DialogController<>(btnSave.getScene().getWindow(), "Chargement des collaborateurs...");

            wd.exec("123", inputParam -> {
                final ObservableList<User> data = longTask();
                // set users Globally
                SettingsGlobal.setUsersList(data);

                Platform.runLater(() ->{
                    comboUser.setItems(data);
                    comboUser.getSelectionModel().selectFirst();
                });
                return 1;
            });
        });
    }

    private void setCurrentUserData() {
       selectedUser = comboUser.getSelectionModel().getSelectedItem();

        switch(selectedUser.getRole()) {
            case 5:
                radioAdmin.setSelected(true);
                break;
            case 4:
                radioAdvUser.setSelected(true);
                break;
            case 3:
                radioNormalUser.setSelected(true);
                break;
            case 2:
                radioGuest.setSelected(true);
                break;
            default:
                radioGuest.setSelected(true);
        }
    }
}
