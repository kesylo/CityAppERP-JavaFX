package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import sample.Database.DBHandler;
import sample.Model.User;
import sample.Ressources.animation.Shaker;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {

    //region Description

    @FXML
    private Hyperlink lblRegister;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnConnection;

    private DBHandler dbHandler;
    //endregion

    @FXML
    void goToDashboard(ActionEvent event) {
        URL navPath = getClass().getResource("/sample/View/signup.fxml");
        Global.goToWindow(navPath, btnConnection," - SignUp", true);
    }

    @FXML
    void initialize() {

        dbHandler = new DBHandler();

        btnConnection.setOnAction(event -> {
            login();
        });

    }

    /*----------------------------------------------------------------------------------------*/

    private void login() {

        // create temp user
        User user = new User();

        String loginUserName = txtUsername.getText().trim();
        String loginPwd = txtPassword.getText().trim();

        if (!loginUserName.equals("") || !loginPwd.equals("")) {
            user.setPseudo(loginUserName);
            user.setPassword(loginPwd);

            ResultSet userRow = dbHandler.getUser(user);

            int counter = 0;

            try {
                while (userRow.next()) {
                    counter++;
                    user.setFirstName(userRow.getString("firstName"));
                    user.setLastName(userRow.getString("lastName"));
                    user.setRole(userRow.getInt("role"));
                    user.setId(userRow.getInt("id"));
                }

                // if we found a match
                if (counter == 1) {
                    System.out.println("login ok");
                    // define user variables globally
                    Global.setConnectedUser(user);
                    // goToDashboard
                    URL navPath = getClass().getResource("/sample/View/dashboard.fxml");
                    Global.goToWindow(navPath, btnConnection,"Dashboard", true);

                } else {
                    System.out.println("login failed");
                    // shake input text
                    showErrorAndShake();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showErrorAndShake();
        }
    }

    private void showErrorAndShake() {
        Shaker shakerUser = new Shaker(txtUsername);
        Shaker shakerPwd = new Shaker(txtPassword);
        Shaker shakerErrorMessage = new Shaker(lblErrorMessage);
        shakerUser.shake();
        shakerPwd.shake();
        shakerErrorMessage.shake();
        // show error message
        lblErrorMessage.setVisible(true);
    }
}
