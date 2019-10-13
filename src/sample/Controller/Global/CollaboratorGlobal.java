package sample.Controller.Global;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.User;

public class CollaboratorGlobal {

    //region Collaborator App
    private static ObservableList<User> usersList = FXCollections.observableArrayList();
    private static String actionName;
    //endregion



    public static ObservableList<User> getUsersList() {
        return usersList;
    }

    public static void setUsersList(ObservableList<User> usersList) {
        CollaboratorGlobal.usersList = usersList;
    }

    public static String getActionName() {
        return actionName;
    }

    public static void setActionName(String actionName) {
        CollaboratorGlobal.actionName = actionName;
    }
}
