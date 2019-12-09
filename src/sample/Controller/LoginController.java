package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Global.Global;
import sample.Database.DBHandler;
import sample.Model.User;
import sample.Ressources.animation.Shaker;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    //region Description
    @FXML
    private Label lblAppVersion;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private ImageView pwdIcon;

    @FXML
    private ImageView userIcon;


    @FXML
    private JFXButton btnConnection;

    private DBHandler dbHandler;
    //endregion

    private DialogController<String> wd = null;
    private int counter = 0;

    @FXML
    void initialize() {

        dbHandler = new DBHandler();

        lblAppVersion.setText(Global.appVersion.toString());

        btnConnection.setOnAction(event -> login());

    }

    /*----------------------------------------------------------------------------------------*/

    private void login() {

        // create temp user
        User user = new User();

        // hide error text
        lblErrorMessage.setVisible(false);

        String loginUserName = txtUsername.getText().trim();
        String loginPwd = txtPassword.getText().trim();


        if (!loginUserName.equals("") || !loginPwd.equals("")) {
            user.setPseudo(loginUserName);
            user.setPassword(loginPwd);


            Platform.runLater(() ->{
                wd = new DialogController<>(btnConnection.getScene().getWindow(), "Connexion...");

                wd.exec("123", inputParam -> {

                    ResultSet userRow = dbHandler.getUser(user);

                    try {
                        while (userRow.next()) {
                            counter++;
                            user.setFirstName(userRow.getString("firstName"));
                            user.setLastName(userRow.getString("lastName"));
                            user.setRole(userRow.getInt("role"));
                            user.setId(userRow.getInt("id"));
                            user.setSex(userRow.getString("sex"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }





                    Platform.runLater(() ->{

                        // if we found a match
                        if (counter == 1) {
                            // define user variables globally
                            Global.setConnectedUser(user);
                            System.out.println("login ok");
                            // show notification
                            Global.successSystemNotif(
                                    "Connexion réussie!",
                                    "#f7a631");
                            // close windows
                            btnConnection.getScene().getWindow().hide();
                            // goToDashboard
                            URL navPath = getClass().getResource("/sample/View/dashboard.fxml");
                            Global.navigateTo(navPath,"Dashboard");

                        } else {
                            System.out.println("login failed");
                            // shake input text
                            showErrorAndShake();
                        }

                    });

                    return 1;
                });
            });
        } else {
            showErrorAndShake();
        }


    }

    private void showErrorAndShake() {
        Shaker shakerUser = new Shaker(txtUsername);
        Shaker shakerPwd = new Shaker(txtPassword);
        Shaker shakerUserIcon = new Shaker(userIcon);
        Shaker shakerPwdIcon = new Shaker(pwdIcon);
        Shaker shakerErrorMessage = new Shaker(lblErrorMessage);
        shakerUser.shake();
        shakerPwd.shake();
        shakerUserIcon.shake();
        shakerPwdIcon.shake();
        shakerErrorMessage.shake();
        // show error message
        lblErrorMessage.setVisible(true);
    }
}
