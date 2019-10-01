package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Model.Caisse;
import sample.Model.Cash;
import sample.Model.User;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Global {

    //region Global variables
    private static User connectedUser = new User();
    public static String appName = "City Apartments ERP";
    public static String navFrom = "";
    public static Stage stage;
    public Scene scene = null;
    //endregion

    //region Caisse App
    private static Caisse currentCaisse = new Caisse();
    private static Caisse previewCaisse = new Caisse();
    private static Caisse beforeCurrentCaisse = new Caisse();
    private static int nberOfCaisses;
    private static Double computedSoldeCaisse;
    private static Cash caisseCash;
    private static Double countCashResult = 0.0;
    private static ObservableList<Caisse> caisseList = FXCollections.observableArrayList();
    private static int nberOfCaissesWithSameDate;
    //endregion

    //region Getters and Setters


    public static int getNberOfCaissesWithSameDate() {
        return nberOfCaissesWithSameDate;
    }

    public static void setNberOfCaissesWithSameDate(int nberOfCaissesWithSameDate) {
        Global.nberOfCaissesWithSameDate = nberOfCaissesWithSameDate;
    }

    public static Stage getStage() {
        return stage;
    }

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        Global.appName = appName;
    }

    public static void setStage(Stage stage) {
        Global.stage = stage;
    }

    public static ObservableList<Caisse> getCaisseList() {
        return caisseList;
    }

    public static void setCaisseList(ObservableList<Caisse> caisseList) {
        Global.caisseList = caisseList;
    }

    public static Caisse getPreviewCaisse() {
        return previewCaisse;
    }

    public static void setPreviewCaisse(Caisse previewCaisse) {
        Global.previewCaisse = previewCaisse;
    }

    public static Double getCountCashResult() {
        return countCashResult;
    }

    public static void setCountCashResult(Double countCashResult) {
        Global.countCashResult = countCashResult;
    }

    public static Cash getCaisseCash() {
        return caisseCash;
    }

    public static Double getComputedSoldeCaisse() {
        return computedSoldeCaisse;
    }

    public static void setComputedSoldeCaisse(Double computedSoldeCaisse) {
        Global.computedSoldeCaisse = computedSoldeCaisse;
    }

    public static void setCaisseCash(Cash caisseCash) {
        Global.caisseCash = caisseCash;
    }

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

    //region Methods

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

    public static void closeAndGoToWindow(URL location, String windowName) {

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
            stage.toFront();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stayButGoToWindow(URL location, String windowName, boolean toback){
        Stage mystage = new Stage();
        try {
            Parent root = FXMLLoader.load(location);
            //Scene scene = new Scene(root);
            //stage.setScene(scene);
            mystage.setScene(new Scene(root));
            mystage.setTitle(appName + " " + windowName);
            mystage.setResizable(false);
            mystage.centerOnScreen();

            mystage.show();
            if (toback){
                mystage.toBack();
                stage = mystage;

            }


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
        Stage mywindow = new Stage();
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
        return formatter.format(date);
    }

    public static String formatDouble (Double value){
        String format = String.format("%.2f", value);
        return format;
    }

    public static int generateCaisseID() {

        return 1;
    }

    /*public static int generateCaisseNumber(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int number;



        return number;
    }*/

    //endregion

}
