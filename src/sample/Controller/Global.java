package sample.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import sample.Model.Caisse;

import java.util.Optional;

public class Global {
    // variables to share between the whole App
    private static int role = 0;
    private static String connectedUserName = "";
    private static Caisse currentCaisse = new Caisse();
    private static Caisse beforeCurrentCaisse = new Caisse();

    // directly accessible
    public static String appName = "City Appartements ERP";

    /*----------------------------------------------------------------------------------------------------------*/

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

    public static int getRole() {
        return role;
    }

    public static String getConnectedUserName() {
        return connectedUserName;
    }

    public static void setRole(int role) {
        Global.role = role;
    }

    public static void setConnectedUserName(String connectedUserName) {
        Global.connectedUserName = connectedUserName;
    }

    /*------------------------------------------------------------------------------------------------------*/
    public static void showErrorMessage(String title, String header, String content) {
        Alert alertDialog = new Alert(Alert.AlertType.ERROR);
        alertDialog.setTitle(title);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(content);
        alertDialog.show();
    }

    public static void showInfoMessage(String title, String header, String content) {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle(title);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(content);
        alertDialog.show();
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
}
