package sample.Global;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.User;

public class SettingsGlobal {

    private static ObservableList<User> usersList = FXCollections.observableArrayList();

    public static ObservableList<User> getUsersList() {
        return usersList;
    }

    public static void setUsersList(ObservableList<User> usersList) {
        SettingsGlobal.usersList = usersList;
    }
}
