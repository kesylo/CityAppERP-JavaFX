package sample.Database;

import sample.Controller.Global.CashRegisterGlobal;
import sample.Controller.Global.Global;
import sample.Model.Caisse;
import sample.Model.CaisseIncExp;
import sample.Model.Cash;
import sample.Model.User;

import java.sql.*;

public class DBHandler extends DBConfig {

    private ResultSet rs;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://"
                + dbHost + ":"
                + dbPort + "/"
                + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(connectionString, dbUser, dbPass);
    }

    /*------------------------------ SELECT -------------------------------------*/

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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return count;
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

            } catch (SQLException | ClassNotFoundException e) {
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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getAllEmployees(){
        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.EMPLOYES_TABLE;
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            rs = ps.executeQuery();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getAllEmployeesNames() {

        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.EMPLOYES_TABLE;
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            rs = ps.executeQuery();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public User getEmployeObjByID(int idEmployes) {
        User user = null;
        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.EMPLOYES_TABLE + " WHERE id=?";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setInt(1, idEmployes);

            rs = ps.executeQuery();

            while (rs.next()){
                user = new User(
                        idEmployes,
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("CivilStatus"),
                        rs.getString("dept"),
                        rs.getString("email"),
                        rs.getString("firstName"),
                        rs.getInt("houseNum"),
                        rs.getString("inService"),
                        rs.getString("lastName"),
                        rs.getString("letterBoxNum"),
                        rs.getString("nationalRegisterNum"),
                        rs.getString("outService"),
                        rs.getString("password"),
                        rs.getInt("postalCode"),
                        rs.getString("pseudo"),
                        rs.getString("sex"),
                        rs.getInt("role")
                );
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return user;
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

        } catch (SQLException | ClassNotFoundException e) {
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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /*------------------------------ INSERT -------------------------------------*/

/*    public void addErrorLine(CaisseIncExp errorExpense) {
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
            ps.setString(4, errorExpense.getCreationDate());
            ps.setString(5, errorExpense.getTime());
            ps.setString(6, errorExpense.getComment());
            ps.setInt(7, errorExpense.getShiftNumber());
            ps.setString(8, errorExpense.getReason());
            ps.setInt(9, errorExpense.getIdUser());

            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public void createCaisse(Caisse caisse) {
        String query = "INSERT INTO " + Static.CAISSE_TABLE + " ( date, montant, numeroShift, closed, employees_id, numeroCaisse, remarque, has_error, error_amount) VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setString(1, caisse.getDate());
            ps.setDouble(2, caisse.getMontant());
            ps.setInt(3, caisse.getNumeroShift());
            ps.setInt(4, caisse.getClosed());
            ps.setInt(5, Global.getConnectedUser().getId());
            ps.setString(6, caisse.getNumeroCaisse());
            ps.setString(7, caisse.getRemarque());
            ps.setInt(8, caisse.getHasError());
            ps.setDouble(9, caisse.getError_amount());

            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addIncORExp(CaisseIncExp caisseAction) {
        String query = "INSERT INTO " + Static.CAISSE_RECETTES_TABLE + " ( montant, type, date, time, indexClient, remarque, numeroShift, reason, salaryBeneficial, employees_id, fk_idCaisse ) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setDouble(1, caisseAction.getAmount());
            ps.setInt(2, caisseAction.getType());
            ps.setString(3, caisseAction.getCreationDate());
            ps.setString(4, caisseAction.getTime());
            ps.setString(5, caisseAction.getClientIndex());
            ps.setString(6, caisseAction.getComment());
            ps.setInt(7, caisseAction.getShiftNumber());
            ps.setString(8, caisseAction.getReason());
            ps.setString(9, caisseAction.getSalaryBeneficial());
            ps.setInt(10, caisseAction.getIdUser());
            ps.setInt(11, caisseAction.getIdCaisse());

            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addCaisseCash(Cash caisseCash) {
        String query = "INSERT INTO " + Static.CAISSE_MONNAIE_TABLE + " ( caisse_idCaisse, numeroShift, lessThanOneEuro, fiftyCents, oneEuro," +
                "twoEuros, fiveEuros, tenEuros, twentyEuros, fiftyEuros, oneHundredEuros, twoHundredEuros) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setInt(1, caisseCash.getIdCaisse());
            ps.setInt(2, caisseCash.getNumShift());
            ps.setDouble(3, caisseCash.getLessThanFiftyCents());
            ps.setInt(4, caisseCash.getFiftyCents());
            ps.setInt(5, caisseCash.getOneEuro());
            ps.setInt(6, caisseCash.getTwoEuros());
            ps.setInt(7, caisseCash.getFiveEuros());
            ps.setInt(8, caisseCash.getTenEuros());
            ps.setInt(9, caisseCash.getTwentyEuros());
            ps.setInt(10, caisseCash.getFiftyEuros());
            ps.setInt(11, caisseCash.getOnehundredeuros());
            ps.setInt(12, caisseCash.getTwoHundredEuros());


            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*------------------------------ UPDATE -------------------------------------*/

    public void updateCaisseStatus(double montant, int statusToSet, String dateClosed, Caisse caisse, double errAmount){
        String query = "UPDATE " + Static.CAISSE_TABLE + " SET montant = ?, closed = ?, date_fermeture = ?, has_error = ?, error_amount = ? WHERE idCaisse = ?";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setDouble(1, montant);
            ps.setInt(2, statusToSet);
            ps.setString(3, dateClosed);
            ps.setInt(4, caisse.getHasError());
            ps.setDouble(5, errAmount);
            ps.setInt(6, caisse.getId());


            ps.executeUpdate();
            ps.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateErrorAmount(double amount, int idCaisse) {
        String query = "UPDATE " + Static.CAISSE_TABLE + " SET error_amount = ? WHERE idCaisse = ?";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setDouble(1, amount);
            ps.setInt(2, idCaisse);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createUser(User user) {

    }

    /*------------------------------ DELETE -------------------------------------*/
}
