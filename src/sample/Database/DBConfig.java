package sample.Database;

class DBConfig {
    // app version
    private static Double appVersion = 3.0;

    // my local Server
    /*String dbHost = "localhost";
    String dbPort = "3306";
    String dbUser = "root";
    String dbPass = "azerty";
    String dbName = "cityappdberp";*/


    // deployment server SITEGROUND
    String dbHost = "gnld1010.siteground.eu";
    String dbPort = "3306";
    String dbUser = "un6hxtz8jfccu";
    String dbPass = "7duzpdu9km6k";
    String dbName = "db2gy8y9y58had";


    //region Getters and setters
    public static Double getAppVersion() {
        return appVersion;
    }

    public static void setAppVersion(Double appVersion) {
        DBConfig.appVersion = appVersion;
    }
    //endregion
}
