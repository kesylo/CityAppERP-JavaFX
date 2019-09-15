package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("View/CashRegister/cashRegister.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("View/login.fxml"));

        //Set app logo
        Image image = new Image("/sample/Ressources/images/icon.png");
        primaryStage.getIcons().add(image);

        primaryStage.setTitle("City Appartements ERP - Login");
        //primaryStage.setScene(new Scene(root, 728, 400));
        primaryStage.setScene(new Scene(root, 986, 609));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
