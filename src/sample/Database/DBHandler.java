package sample.Database;

import sample.Controller.Global;
import sample.Model.Caisse;
import sample.Model.CaisseIncExp;
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

    public int getNbrCaisseWithSameDate (String date) {
        String query = "SELECT * FROM " + Static.CAISSE_TABLE + " WHERE date=?";
        int count = 0;
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setString(1, date);
            rs = ps.executeQuery();

            while (rs.next()) {
                count ++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return count;
    }

    public double getLastAmountCaisse() {
        Double amount = 0.0;
        // prepare the query
        String query = "SELECT montant FROM " + Static.CAISSE_TABLE + " ORDER BY idCaisse DESC LIMIT 1";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                amount = rs.getDouble("montant");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return amount;
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

    public ResultSet getCaisseDataByNumShiftAndDate(int numeroShift, Date date) {
        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.CAISSE_TABLE + " WHERE date =? AND numeroShift=?";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setDate(1, date);
            ps.setInt(2, numeroShift);

            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public Boolean checkIfDateExists(String date) {
        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.CAISSE_TABLE + " WHERE date=?";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setString(1, date);

            int cnt = 0;

            rs = ps.executeQuery();

            try {
                while (rs.next()) {
                    cnt++;
                }

                if (cnt >= 1) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
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

    public void createCaisse(Caisse caisse) {
        // INSERT INTO `cityappdatabase`.`caisse` ( `date`, `montant`, `numeroShift`, `closed`, `employees_id`) VALUES ('128', '2019-09-11', '321.52', '1', '1', '7');
        // prepare the query
        String query = "INSERT INTO " + Static.CAISSE_TABLE + " ( date, montant, numeroShift, closed, employees_id ) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setDate(1, caisse.getDate());
            ps.setDouble(2, caisse.getMontant());
            ps.setInt(3, caisse.getNumeroShift());
            ps.setInt(4, caisse.getClosed());
            ps.setInt(5, Global.getConnectedUser().getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getEmployeByID(int idEmployes) {

        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.EMPLOYES_TABLE + " WHERE id=?";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setInt(1, idEmployes);

            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getIncomeExpense(int idCaisse, int numeroShift, int type) {
        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.CAISSE_RECETTES_TABLE + " WHERE fk_idCaisse=? and numeroShift=? and type=?";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setInt(1, idCaisse);
            ps.setInt(2, numeroShift);
            // type = 0 => Income
            ps.setInt(3, type);

            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getCash(int idCaisse, int numeroShift) {
        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.CAISSE_MONNAIE_TABLE + " WHERE caisse_idCaisse=? and numeroShift=?";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setInt(1, idCaisse);
            ps.setInt(2, numeroShift);

            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void addErrorLine(CaisseIncExp errorExpense) {
        //INSERT INTO `cityappdatabase`.`caisse_recettes` (`id_caisse_recettes`, `fk_idCaisse`, `montant`,
        // `type`, `date`, `time`, `indexClient`, `remarque`, `numeroShift`, `reason`, `employees_id`) VALUES ('231', '1', '256', '1', '2019-09-11', '23:12', '158-256-2322', 'ccc', '2', 'ccc', '1');
        String query = "INSERT INTO " + Static.CAISSE_RECETTES_TABLE + " ( " +
                "fk_idCaisse," +
                " montant," +
                " type," +
                " date, " +
                " time, " +
                " remarque, " +
                " numeroShift, " +
                " reason, " +
                "employees_id ) VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setInt(1, errorExpense.getIdCaisse());
            ps.setDouble(2, errorExpense.getAmount());
            ps.setInt(3, errorExpense.getType());
            ps.setDate(4, errorExpense.getCreationDate());
            ps.setString(5, errorExpense.getTime());
            ps.setString(6, errorExpense.getComment());
            ps.setInt(7, errorExpense.getShiftNumber());
            ps.setString(8, errorExpense.getReason());
            ps.setInt(9, errorExpense.getIdUser());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
