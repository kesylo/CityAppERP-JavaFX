package sample.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Global.CollaboratorGlobal;
import sample.Global.Global;
import sample.Model.*;

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
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }

        return count;
    }

    public ResultSet getUserServicesInMonth(int idUser, int month, int year) {
        rs = null;
        String status = "Accepté";

        // prepare the query
        String query = "SELECT startTime, endTime, date FROM planning where id_user=? " +
                "and extract(year from str_to_date(date, '%d-%m-%Y')) =? " +
                "and extract(month from str_to_date(date, '%d-%m-%Y')) =? " +
                "and status =? ";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setInt(1, idUser);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ps.setString(4, status);

            rs = ps.executeQuery();

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
        return rs;
    }

    public ResultSet getUserServicesInYear(int idUser, int year) {
        rs = null;
        String status = "Accepté";

        // prepare the query
        String query = "SELECT startTime, endTime, date FROM planning where id_user=? " +
                "and extract(year from str_to_date(date, '%d-%m-%Y')) =? " +
                "and status =? ";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setInt(1, idUser);
            ps.setInt(2, year);
            ps.setString(3, status);

            rs = ps.executeQuery();

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
        return rs;
    }

    public ResultSet getUserIDInSelectedDept(String selectedService) {
        rs = null;

        // prepare the query
        String query = "SELECT id FROM employees where dept=? " +
                "and outService is null or outService = '' ";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setString(1, selectedService);

            rs = ps.executeQuery();

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
        return rs;
    }

    public ObservableList<Planning> getDeptServiceInMonth(ObservableList<Integer> usersInSameDept, int seletedMonth, int selectedYear) {

        ObservableList<Planning> planningList = FXCollections.observableArrayList();
        String status = "Accepté";
        rs = null;

        for (Integer id : usersInSameDept) {

            String query = "SELECT startTime, endTime, date FROM planning where id_user=? " +
                    "and extract(year from str_to_date(date, '%d-%m-%Y')) =? " +
                    "and extract(month from str_to_date(date, '%d-%m-%Y')) =? " +
                    "and status =? ";
            // run it
            try {
                PreparedStatement ps = getDbConnection().prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, selectedYear);
                ps.setInt(3, seletedMonth);
                ps.setString(4, status);

                rs = ps.executeQuery();

                while (rs.next()){
                    Planning planning = new Planning(
                            rs.getString("date"),
                            rs.getString("startTime"),
                            rs.getString("endTime")
                    );
                    planningList.add(planning);
                }

            } catch (Exception e) {
                //e.printStackTrace();
                Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                        "Voici les détails sur l'erreur ", e);
            }
        }

        return planningList;
    }

    public ObservableList<Planning> getDeptServiceInYear(ObservableList<Integer> usersInSameDept, int selectedYear) {

        ObservableList<Planning> planningList = FXCollections.observableArrayList();
        String status = "Accepté";
        rs = null;

        for (Integer id : usersInSameDept) {

            String query = "SELECT startTime, endTime, date FROM planning where id_user=? " +
                    "and extract(year from str_to_date(date, '%d-%m-%Y')) =? "+
                    "and status =? ";
            // run it
            try {
                PreparedStatement ps = getDbConnection().prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, selectedYear);
                ps.setString(3, status);

                rs = ps.executeQuery();

                while (rs.next()){
                    Planning planning = new Planning(
                            rs.getString("date"),
                            rs.getString("startTime"),
                            rs.getString("endTime")
                    );
                    planningList.add(planning);
                }

            } catch (Exception e) {
                //e.printStackTrace();
                Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                        "Voici les détails sur l'erreur ", e);
            }
        }

        return planningList;
    }

    public ResultSet getUser(User user)  {

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

            } catch (Exception ex) {
                Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                        "Voici les détails sur l'erreur ", ex);
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

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
        return rs;
    }

    public Caisse getLastCaisse() {
        rs = null;
        Caisse caisse = new Caisse();

        // prepare the query
        String query = "SELECT * FROM " + Static.CAISSE_TABLE + " ORDER BY idCaisse DESC LIMIT 1";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()){
              caisse.setId(rs.getInt("idCaisse"));
              caisse.setDate(rs.getString("date"));
              caisse.setMontant(rs.getDouble("montant"));
              caisse.setRemarque(rs.getString("remarque"));
              caisse.setNumeroShift(rs.getInt("numeroShift"));
              caisse.setClosed(rs.getInt("closed"));
              caisse.setIdEmployes(rs.getInt("employees_id"));
              caisse.setNumeroCaisse(rs.getString("numeroCaisse"));
              caisse.setHasError(rs.getInt("has_error"));
              caisse.setError_amount(rs.getDouble("error_amount"));
            }

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
        return caisse;
    }

    public ResultSet getAllEmployees(){
        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.EMPLOYES_TABLE;
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            rs = ps.executeQuery();

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
        return rs;
    }

    public ResultSet getActiveEmployees() {
        rs = null;

        // prepare the query
        String query = "SELECT * FROM " + Static.EMPLOYES_TABLE + " WHERE outService is null or outService = ''";
        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            rs = ps.executeQuery();

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
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

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
        return rs;
    }

    public ResultSet getPlanningServerAdress() {
        String query = "SELECT * FROM erp_tools WHERE id_erp_tools=1";

        // run it
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            rs = ps.executeQuery();

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
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
                        rs.getInt("role"),
                        rs.getString("phoneNumber"),
                        rs.getDouble("salary1"),
                        rs.getDouble("salary2"),
                        rs.getString("status"),
                        rs.getInt("employeeNumber"),
                        rs.getString("birthday"),
                        rs.getString("phoneCountry"),
                        rs.getString("country"),
                        rs.getString("iban")
                );
            }

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
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

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
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

        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
        return rs;
    }

    /*------------------------------ INSERT -------------------------------------*/

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
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
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
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }

    public void addPayment(Payment payment) {
        String query = "INSERT INTO payments (amount, date, userId, description) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setDouble(1, payment.getAmount());
            ps.setString(2, payment.getDate());
            ps.setInt(3, payment.getUserId());
            ps.setString(4, payment.getDescription());

            ps.executeUpdate();
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
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
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }

    public void createUser(User user) {

        String query = "INSERT INTO " + Static.EMPLOYES_TABLE + " (firstName, lastName, inService, outService, nationalRegisterNum," +
                " address, houseNum, letterBoxNum, postalCode, city, CivilStatus, sex, pseudo, email, password, dept, role," +
                " phoneNumber, salary1, salary2, status, employeeNumber, country, phoneCountry, birthday, iban) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getDateInService());
            ps.setString(4, user.getDateOutService());
            ps.setString(5, user.getNationalRegistreryNumber());
            ps.setString(6, user.getAddress());
            ps.setInt(7, user.getHouseNumber());
            ps.setString(8, user.getLetterBoxNumber());
            ps.setInt(9, user.getPostalCode());
            ps.setString(10, user.getCity());
            ps.setString(11, user.getMaritalStatus());
            ps.setString(12, user.getSex());
            ps.setString(13, user.getPseudo());
            ps.setString(14, user.getEmail());
            ps.setString(15, user.getPassword());
            ps.setString(16, user.getDepartement());
            ps.setInt(17, user.getRole());
            ps.setString(18, user.getPhoneNumber());
            ps.setDouble(19, user.getSalary1());
            ps.setDouble(20, user.getSalary2());
            ps.setString(21, user.getStatus());
            ps.setInt(22, user.getEmployeeNumber());
            ps.setString(23, user.getCountry());
            ps.setString(24, user.getPhoneCountry());
            ps.setString(25, user.getBirthday());
            ps.setString(26, user.getIban());

            ps.executeUpdate();
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
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
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }

    public void updateUser(User user) {
        String query = "UPDATE " + Static.EMPLOYES_TABLE + " SET firstName = ?, lastName = ?, inService = ?, outService = ?," +
                " nationalRegisterNum = ?, address = ?, houseNum = ?, letterBoxNum = ?, postalCode = ?, city = ?," +
                " CivilStatus = ?, sex = ?, pseudo = ?, email = ?, password = ?, dept = ?, role = ?, phoneNumber = ?," +
                " salary1 = ?, salary2 = ?, status = ?, employeeNumber = ?, country = ?, phoneCountry = ?, birthday = ?," +
                " iban = ?" + " WHERE id = ?";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getDateInService());
            ps.setString(4, user.getDateOutService());
            ps.setString(5, user.getNationalRegistreryNumber());
            ps.setString(6, user.getAddress());
            ps.setInt(7, user.getHouseNumber());
            ps.setString(8, user.getLetterBoxNumber());
            ps.setInt(9, user.getPostalCode());
            ps.setString(10, user.getCity());
            ps.setString(11, user.getMaritalStatus());
            ps.setString(12, user.getSex());
            ps.setString(13, user.getPseudo());
            ps.setString(14, user.getEmail());
            ps.setString(15, user.getPassword());
            ps.setString(16, user.getDepartement());
            ps.setInt(17, user.getRole());
            ps.setString(18, user.getPhoneNumber());
            ps.setDouble(19, user.getSalary1());
            ps.setDouble(20, user.getSalary2());
            ps.setString(21, user.getStatus());
            ps.setInt(22, user.getEmployeeNumber());
            ps.setString(23, user.getCountry());
            ps.setString(24, user.getPhoneCountry());
            ps.setString(25, user.getBirthday());
            ps.setString(26, user.getIban());
            ps.setInt(27, CollaboratorGlobal.getPreviewUser().getId());

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }

    public void archiveCollaborator(User previewUser) {
        String query = "UPDATE " + Static.EMPLOYES_TABLE + " SET outService = ? WHERE id = ?";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setString(1, previewUser.getDateOutService());
            ps.setInt(2, previewUser.getId());

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }

    public void updateRole(User selectedUser, int newRole) {
        String query = "UPDATE " + Static.EMPLOYES_TABLE + " SET role = ? WHERE id = ?";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setInt(1, newRole);
            ps.setInt(2, selectedUser.getId());

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }

    public void updateServerAddress(String address) {
        String query = "UPDATE erp_tools SET planningServerAddress = ? WHERE id_erp_tools = 1";

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(query);

            ps.setString(1, address);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            //e.printStackTrace();
            Global.showExceptionMessage("Une erreur est survenue lors de l'exécution de la tâche précedente",
                    "Voici les détails sur l'erreur ", e);
        }
    }



    /*------------------------------ DELETE -------------------------------------*/
}
