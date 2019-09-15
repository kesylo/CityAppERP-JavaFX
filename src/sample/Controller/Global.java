package sample.Controller;

public class Global {
    // variables to share between the whole App
    private static int role = 0;
    private static String connectedUserName = "";

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


}
