package sample;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.Controller.Global;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View/dashboard.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("View/login.fxml"));

        //Set app logo
        Image image = new Image("/sample/Ressources/images/icon.png");
        primaryStage.getIcons().add(image);

        primaryStage.setTitle(Global.appName + " - Login");
        //primaryStage.setScene(new Scene(root, 478, 628));
        primaryStage.setScene(new Scene(root, 986, 609));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // set stage globally for faster navigation
        Global.setStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
