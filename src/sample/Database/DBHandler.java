package sample.Database;

import sample.Model.User;

import java.sql.*;

public class DBHandler extends DBConfig {
    Connection dbConnection;
    ResultSet rs;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://"
                + dbHost + ":"
                + dbPort + "/"
                + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    /*------------------------------ Methods -------------------------------------*/

    public ResultSet getLastAmountByDate(String date) {
        rs = null;
        // prepare the query
        String query = "SELECT J_prec FROM " + Static.CAISSE_TABLE + " WHERE date=?";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setString(1, date);
            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getLastAmountCaisse() {
        rs = null;
        // prepare the query
        String query = "SELECT J_prec FROM " + Static.CAISSE_TABLE + " ORDER BY idCaisse DESC LIMIT 1";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getUser(User user) {

        rs = null;

        if (!user.getPseudo().equals("") || !user.getPassword().equals("")) {
            // prepare the query
            String query = "SELECT * FROM " + Static.EMPLOYES_TABLE + " WHERE pseudo=?" + " AND password=?";
            // run it
            try {
                PreparedStatement ps = getDbConnection().prepareStatement(query);
                ps.setString(1, user.getPseudo());
                ps.setString(2, user.getPassword());

                rs = ps.executeQuery();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("retry");

        }

        return rs;
    }

    public ResultSet getAllFromCaisse() {
        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.CAISSE_TABLE;
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public int getCaisseIdByDate(String date) {
        rs = null;
        int id = 0;

        // prepare the query
        String query = "SELECT idCaisse FROM " + Static.CAISSE_TABLE + " WHERE date=?";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setString(1, date);

            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) {
                id = rs.getInt("idCaisse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }


    public void createCaisse(String toString, Float lastCaisseAmount) {
        // INSERT INTO `cityappdatabase`.`caisse` (`idCaisse`, `date`, `J_prec`) VALUES ('128', '2019-09-14', '10.12');
        // prepare the query
        String query = "INSERT INTO " + Static.CAISSE_TABLE + " WHERE date=?";

        // to complete
    }
}
