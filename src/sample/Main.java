package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.Controller.Global.CashRegisterGlobal;
import sample.Controller.Global.Global;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("View/CashRegister/caisseDashboard.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("View/Collaborators/addCollaborator.fxml"));

        //Set app logo
        Image image = new Image("/sample/Ressources/images/icon.png");
        primaryStage.getIcons().add(image);

        primaryStage.setTitle(Global.appName + " - Login");
        Scene scene = new Scene(root, 1160, 760);
        primaryStage.setScene(scene);

        //scene.getStylesheets().add(getClass().getResource("/sample/Ressources/css/gradient.css").toExternalForm());
        //primaryStage.setScene(new Scene(root, 875, 823));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        // set stage globally for faster navigation
        Global.setStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
