package sample.Model;

import java.sql.Date;

public class User {
    private int id;
    private String adress;
    private String city;
    private String maritalStatus;
    private String departement;
    private String email;
    private String firstName;
    private int houseNumber;
    private Date dateInService;
    private String lastName;
    private String letterBoxNumber;
    private String nationalRegistreryNumber;
    private Date dateOutService;
    private String password;
    private int postalCode;
    private String pseudo;
    private String sex;
    private int role;


    //region controller
    public User() {
    }

    public User(int id, String adress, String city, String maritalStatus, String departement, String email, String firstName, int houseNumber, Date dateInService, String lastName, String letterBoxNumber, String nationalRegistreryNumber, Date dateOutService, String password, int postalCode, String pseudo, String sex, int role) {
        this.id = id;
        this.adress = adress;
        this.city = city;
        this.maritalStatus = maritalStatus;
        this.departement = departement;
        this.email = email;
        this.firstName = firstName;
        this.houseNumber = houseNumber;
        this.dateInService = dateInService;
        this.lastName = lastName;
        this.letterBoxNumber = letterBoxNumber;
        this.nationalRegistreryNumber = nationalRegistreryNumber;
        this.dateOutService = dateOutService;
        this.password = password;
        this.postalCode = postalCode;
        this.pseudo = pseudo;
        this.sex = sex;
        this.role = role;
    }
    //endregion


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", adress='" + adress + '\'' +
                ", city='" + city + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", departement='" + departement + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", houseNumber=" + houseNumber +
                ", dateInService=" + dateInService +
                ", lastName='" + lastName + '\'' +
                ", letterBoxNumber='" + letterBoxNumber + '\'' +
                ", nationalRegistreryNumber='" + nationalRegistreryNumber + '\'' +
                ", dateOutService=" + dateOutService +
                ", password='" + password + '\'' +
                ", postalCode=" + postalCode +
                ", pseudo='" + pseudo + '\'' +
                ", sex='" + sex + '\'' +
                ", role=" + role +
                '}';
    }

    //region getters and setters
    public int getId() {
        return id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Date getDateInService() {
        return dateInService;
    }

    public void setDateInService(Date dateInService) {
        this.dateInService = dateInService;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLetterBoxNumber() {
        return letterBoxNumber;
    }

    public void setLetterBoxNumber(String letterBoxNumber) {
        this.letterBoxNumber = letterBoxNumber;
    }

    public String getNationalRegistreryNumber() {
        return nationalRegistreryNumber;
    }

    public void setNationalRegistreryNumber(String nationalRegistreryNumber) {
        this.nationalRegistreryNumber = nationalRegistreryNumber;
    }

    public Date getDateOutService() {
        return dateOutService;
    }

    public void setDateOutService(Date dateOutService) {
        this.dateOutService = dateOutService;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    //endregion
}
