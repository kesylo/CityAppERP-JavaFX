package sample.Controller;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.Window;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Model.Caisse;
import sample.Model.User;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class Global {
    private static User connectedUser = new User();
    private static Caisse currentCaisse = new Caisse();
    private static Caisse beforeCurrentCaisse = new Caisse();
    private static int nberOfCaisses;

    private static Stage caisseDashStage = null;

    // directly accessible
    public static String appName = "City Apartments ERP";

    //region Getters and Setters

    public static int getNberOfCaisses() {
        return nberOfCaisses;
    }

    public static void setNberOfCaisses(int nberOfCaisses) {
        Global.nberOfCaisses = nberOfCaisses;
    }

    public static User getConnectedUser() {
        return connectedUser;
    }

    public static void setConnectedUser(User connectedUser) {
        Global.connectedUser = connectedUser;
    }

    public static Caisse getBeforeCurrentCaisse() {
        return beforeCurrentCaisse;
    }

    public static void setBeforeCurrentCaisse(Caisse beforeCurrentCaisse) {
        Global.beforeCurrentCaisse = beforeCurrentCaisse;
    }

    public static Caisse getCurrentCaisse() {
        return currentCaisse;
    }

    public static void setCurrentCaisse(Caisse currentCaisse) {
        Global.currentCaisse = currentCaisse;
    }

    //endregion

    /*--------------------------------- METHODS ---------------------------------------------------------------------*/
    public static void showErrorMessage(String header, String content) {
        Alert alertDialog = new Alert(Alert.AlertType.ERROR);
        alertDialog.setTitle(appName);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(content);
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

        if (result.orElse(no) == yes){
            return true;
        }
        return false;
    }

    public static void goToWindow (URL location, JFXButton anyBtn, String windowName, boolean closeWindow) {

        if (closeWindow){
            anyBtn.getScene().getWindow().hide();
        }

        /*List<Window> windows = Window.getWindows();
        Stage mywindow = new Stage();
        for (Window win : windows) {
            if (win.getTitle().contains(windowName)) {
                    mywindow= win
            }
        }*/


         Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(location);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(appName + " " + windowName);
            stage.setResizable(false);
            stage.show();

            // animate window
            //new FadeIn(root).play();

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
        stage.setResizable(false);
        stage.setTitle(appName + " - Login");
        stage.show();

        // navigate to new screen
        anyBtn.getScene().getWindow().hide();
    }

    public static String getSystemTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(cal.getTime());
    }
}
