package sample.Global;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.sun.glass.ui.Window;
import com.sun.xml.internal.ws.util.StringUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;
import sample.Model.Country;
import sample.Model.CountryComparator;
import sample.Model.User;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Global {


    //region variables
    public static String PlanningServerAddress;
    private static User connectedUser = new User();
    public static String appName = "City Apartments ERP";
    public static String navFrom = "";
    public static Double appVersion = 1.2;
    private static Stage stage;

    //endregion

    //region Getters and Setters


    public static String getPlanningServerAddress() {
        return PlanningServerAddress;
    }

    public static void setPlanningServerAddress(String planningServerAddress) {
        PlanningServerAddress = planningServerAddress;
    }

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

    public static ArrayList<String> readTxtFile (String path){
        InputStream inputStream = Main.class.getResourceAsStream(path);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        ArrayList<String> fileLines = new ArrayList<>();
        try {

            /*BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), StandardCharsets.UTF_8));*/

            BufferedReader in = new BufferedReader(inputStreamReader);
            String str;

            while ((str = in.readLine()) != null) {
                fileLines.add(str);
            }

            in.close();
        }catch (Exception e){
            e.fillInStackTrace();
        }

        return fileLines;
    }

    public static boolean isUnix() {
        String OS = System.getProperty("os.name");
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public static boolean isWindows() {
        String OS = System.getProperty("os.name");
        return OS.contains("Win");
    }

    public static void showInfoMessage(String header, String content) {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle(appName);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(content);
        alertDialog.showAndWait();
    }

    public static void showExceptionMessage(String header, String content, Exception ex) {
        Alert alertDialog = new Alert(Alert.AlertType.ERROR);
        alertDialog.setTitle(appName);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(content);


        //ex = new FileNotFoundException("Could not find file blabla.txt");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alertDialog.getDialogPane().setExpandableContent(expContent);


        alertDialog.showAndWait();
    }

    public static String localDateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = date.format(formatter);
        return formattedString;
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

    public static List<LocalDate> getYearsBetween(LocalDate startDate, LocalDate endDate) {

        long numOfDaysBetween = ChronoUnit.YEARS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(startDate::plusYears)
                .collect(Collectors.toList());
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

    public static String minutesToTime(long minutes){
        int h = 0;
        while (minutes >=60){
            minutes -=60;
            h++;
        }
        return String.format("%02d", h) + " : " + String.format("%02d", minutes);
    }

    public static String formatDouble (double value){
        return String.format("%.2f", value);
    }

    public static String formatDouble3 (double value){
        return String.format("%.3f", value);
    }

    public static double roundDouble(double value){
        return Math.round(value * 100D) / 100D;
    }

    public static void setProfileIcon(ImageView photo) {
        Image image;
        // set welcome text
        if (connectedUser.getSex().equals("Masculin")){
            // set profile photo
            image = new Image("/sample/Ressources/images/userMale.png");
            photo.setImage(image);
        }else{
            // set profile photo
            image = new Image("/sample/Ressources/images/userFemale.png");
            photo.setImage(image);
        }
    }

    public static void inputTextFormater(JFXTextField textField, int maxLength, int type, int stringCase){

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

            if (type == 4){ // validate salary
                if (!newValue.matches("\\d{0,7}([.]\\d{0,4})?")) {
                    textField.setText(oldValue);
                }
            }

            if (type == 0){ // validate all
                System.out.print("");
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
                System.out.print("");
            }



        });
    }

    public static List<Country> getCountriesList(){
        List<Country> countries = new ArrayList<Country>();

        String[] isoCountries = Locale.getISOCountries();
        for (String country : isoCountries) {
            Locale locale = new Locale("en", country);
            String iso = locale.getISO3Country();
            String code = locale.getCountry();
            String name = locale.getDisplayCountry();

            if (!"".equals(iso) && !"".equals(code) && !"".equals(name)) {
                countries.add(new Country(iso, code, name));
            }
        }

        // Sort the country by their name and then display the content
        // of countries collection object.
        Collections.sort(countries, new CountryComparator());

        return countries;
    }

    public static LocalDate stringToLocalDate(String stringDate){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(stringDate, dtf);
    }

    public static String convertBirthdayToRegisterNber(LocalDate birthDate) {
        String date = localDateToString(birthDate);
        String arr[] = date.split("-");
        StringBuilder stringBuilder = new StringBuilder();

        Year firstYear = Year.of(Integer.parseInt(arr[0]));

        // Create a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");

        // rebuild string
        stringBuilder.append(firstYear.format(formatter));
        stringBuilder.append(".");
        stringBuilder.append(arr[1]);
        stringBuilder.append(".");
        stringBuilder.append(arr[2]);
        stringBuilder.append("-");

        return stringBuilder.toString();
    }

    private static boolean openWebPage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean openInBrowser(String address) {
        try {

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(address));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }

    public static int monthToInt(String monthValue) {
        switch (monthValue) {
            case "Janvier":
                return 1;
            case "Février":
                return 2;
            case "Mars":
                return 3;
            case "Avril":
                return 4;
            case "Mai":
                return 5;
            case "Juin":
                return 6;
            case "Juillet":
                return 7;
            case "Aout":
                return 8;
            case "Septembre":
                return 9;
            case "Octobre":
                return 10;
            case "Novembre":
                return 11;
            case "Decembre":
                return 12;

                default: return 0;
        }
    }

    public static void openInExplorer(String myDocumentsPath) {
        Desktop desktop = null;
        File file = new File(myDocumentsPath);
        if (Desktop.isDesktopSupported()){
            desktop = Desktop.getDesktop();
        }
        try {
            Objects.requireNonNull(desktop).open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //endregion
}
