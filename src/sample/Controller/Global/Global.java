package sample.Controller.Global;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.sun.glass.ui.Window;
import com.sun.xml.internal.ws.util.StringUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Model.User;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class Global {

    //region variables

    private static User connectedUser = new User();
    public static String appName = "City Apartments ERP";
    public static String navFrom = "";
    private static Stage stage;

    //endregion

    //region Getters and Setters

    public static User getConnectedUser() {
        return connectedUser;
    }

    public static void setConnectedUser(User connectedUser) {
        Global.connectedUser = connectedUser;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        Global.stage = stage;
    }

    //endregion

    //region Methods

    public static void showErrorMessage(String header, String content) {
        Alert alertDialog = new Alert(Alert.AlertType.ERROR);
        alertDialog.setTitle(appName);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(content);
        //Set app logo
        Image image = new Image("/sample/Ressources/images/icon.png");
        stage.getIcons().add(image);

        alertDialog.showAndWait();
    }

    public static void showInfoMessage(String header, String content) {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle(appName);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(content);
        alertDialog.showAndWait();
    }

    public static Boolean showInfoMessageWithBtn(String header, String content, String btnYes, String btnNo) {
        // change button type text
        ButtonType yes = new ButtonType(btnYes, ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType(btnNo, ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alertDialog = new Alert(Alert.AlertType.CONFIRMATION, content, yes,  no);
        alertDialog.setTitle(appName);
        alertDialog.setHeaderText(header);

        Optional<ButtonType> result = alertDialog.showAndWait();

        return result.orElse(no) == yes;
    }

    public static void navigateModal (URL location, String windowName){
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(location);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(appName + " " + windowName);
            //Set app logo
            Image image = new Image("/sample/Ressources/images/icon.png");
            stage.getIcons().add(image);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void navigateTo(URL location, String windowName) {

        /*if (closeWindow){
            anyBtn.getScene().getWindow().hide();
        }*/
        /*List<Window> windows = Window.getWindows();
        Stage mywindow = new Stage();
        for (Window win : windows) {
            if (win.getTitle().contains(windowName)) {
                    mywindow= win
            }
        }*/


        try {
            Parent root = FXMLLoader.load(location);
            stage.setScene(new Scene(root));
            stage.setTitle(appName + " " + windowName);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setUserProfile(Label userName, ImageView logOutBtn){
        userName.setText(connectedUser.getFirstName() + " " + connectedUser.getLastName());
        // set disconnect tooltip
        Tooltip.install(logOutBtn, new Tooltip("Déconnexion"));
    }

    public static void logOut(URL location, JFXButton anyBtn) {
        // get all windows and close
        List<Window> windows = Window.getWindows();
        for (int i = windows.size() - 1; i >= 0; i--) {
            if (windows.get(i).getTitle().contains("City")) {
                windows.get(i).close();
            }
        }

        // load login scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image image = new Image("/sample/Ressources/images/icon.png");
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle(appName + " - Connexion");
        stage.show();

        // navigate to new screen
        anyBtn.getScene().getWindow().hide();
    }

    public static String getSystemTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(cal.getTime());
    }

    public static String getSystemDateTime(){
        Date date = new Date(); // this object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    public static void closeWindow(String windowName){
        List<Window> windows = Window.getWindows();
        for (Window win : windows) {
            if (win.getTitle().contains(windowName)) {
                win.close();
            }
        }
    }

    public static void successSystemNotif(String message, String color){
        TrayNotification tray = new TrayNotification();

        Image whatsAppImg = new Image("/sample/Ressources/images/icon.png");

        tray.setTitle(appName);
        tray.setMessage(message);
        tray.setRectangleFill(Paint.valueOf(color));
        tray.setAnimationType(AnimationType.POPUP);
        tray.setImage(whatsAppImg);
        tray.showAndDismiss(Duration.seconds(0.5));
    }

    public static String getSystemDate()  {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String formatDouble (double value){
        return String.format("%.2f", value);
    }

    public static double roundDouble(double value){
        return Math.round(value * 100D) / 100D;
    }

    public static void setProfileIcon(ImageView photo) {
        Image image;
        // set welcome text
        if (connectedUser.getSex().equals("Male")){
            // set profile photo
            image = new Image("/sample/Ressources/images/userMale.png");
            photo.setImage(image);
        }else{
            // set profile photo
            image = new Image("/sample/Ressources/images/userFemale.png");
            photo.setImage(image);
        }
    }

    public static void txtFormater(JFXTextField textField, int maxLength, int type, int stringCase){

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (type == 1){     // validate letters only
                if (!newValue.matches("^[a-zA-Z]*$?")) {
                    textField.setText(oldValue);
                }
            }

            if (type == 2){ // validate salary
                if (!newValue.matches("\\d{0,7}([.]\\d{0,2})?")) {
                    textField.setText(oldValue);
                }
            }

            if (type == 3){ // validate int
                if (!newValue.matches("\\d*$?")) {
                    textField.setText(oldValue);
                }
            }

            if (type == 0){ // validate all
                System.out.println("validate all");
            }




            if (textField.getLength() > maxLength){
                textField.setText(oldValue);
            }

            if (stringCase == 1){
                textField.focusedProperty().addListener((obs, oldVal, newVal) -> textField.setText(textField.getText().toUpperCase()));
            }

            if (stringCase == 2){
                textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    String cap = StringUtils.capitalize(textField.getText());
                    textField.setText(cap);
                });
            }

            if (stringCase == 3){
                System.out.println("do nothing");
            }



        });
    }

    //endregion
}
