package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.animation.Shaker;
import sample.database.DBHandler;
import sample.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView imgLogo;


    @FXML
    private Label lblErrorMessage;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnConnection;

    @FXML
    private JFXButton btnSignUp;

    private DBHandler dbHandler;

    @FXML
    void initialize() {

        dbHandler = new DBHandler();


        btnSignUp.setOnAction(event -> {
            // Simple nav to goToSignup screen
            goToSignup();
        });

        btnConnection.setOnAction(event -> {

            login();

        });

    }

    /*----------------------------------------------------------------------------------------*/

    private void goToSignup() {
        // navigate to new screen
        btnSignUp.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/view/signup.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("City Appartements ERP");
        stage.showAndWait();
    }

    private void login() {

        String loginUserName = txtUsername.getText().trim();
        String loginPwd = txtPassword.getText().trim();

        if (!loginUserName.equals("") || !loginPwd.equals("")) {
            // create temp user
            User user = new User();
            user.setPseudo(loginUserName);
            user.setPassword(loginPwd);

            ResultSet userRow = dbHandler.getUser(user);

            int counter = 0;

            try {
                while (userRow.next()) {
                    counter++;
                    //String test = userRow.getString("address");
                }

                // if we found a match
                if (counter == 1) {
                    System.out.println("login ok");
                    goToDashboard();
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

    private void goToDashboard() {
        // navigate to new screen
        btnConnection.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/view/dashboard.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("City Appartements ERP");
        stage.showAndWait();
    }
}
