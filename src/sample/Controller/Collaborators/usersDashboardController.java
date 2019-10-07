package sample.Controller.Collaborators;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class usersDashboardController {

    @FXML
    private JFXTextField searchBar;

    @FXML
    private ComboBox<?> comboDepartement;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXRadioButton radioMale;

    @FXML
    private JFXRadioButton radioFemale;

    @FXML
    private TableView<?> tableUsers;

    @FXML
    private TableColumn<?, ?> clmName;

    @FXML
    private TableColumn<?, ?> clmSurname;

    @FXML
    private TableColumn<?, ?> clmSex;

    @FXML
    private TableColumn<?, ?> clmDept;

    @FXML
    private TableColumn<?, ?> clmPhone;

    @FXML
    private TableColumn<?, ?> clmNationalNumber;

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

    @FXML
    void logOut(MouseEvent event) {

    }

    @FXML
    void initialize() {

    }
}
