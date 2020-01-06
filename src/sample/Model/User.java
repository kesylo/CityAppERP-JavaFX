package sample.Model;

public class User implements Comparable{
    private int id;
    private String address;
    private String city;
    private String maritalStatus;
    private String departement;
    private String email;
    private String firstName;
    private int houseNumber;
    private String dateInService;
    private String lastName;
    private String letterBoxNumber;
    private String nationalRegistreryNumber;
    private String dateOutService;
    private String password;
    private String country;
    private String phoneCountry;
    private int postalCode;
    private String pseudo;
    private String birthday;
    private String sex;
    private int role;
    private String phoneNumber;
    private double salary1;
    private double salary2;
    private String status;
    private int employeeNumber;
    private String iban;


    //region controller
    public User() {
    }

    @Override
    public int compareTo(Object o) {
        return this.getFirstName().compareTo(((User) o).getFirstName());
    }

    // new constructor
    public User(int id, String address, String city, String maritalStatus, String departement, String email, String firstName, int houseNumber, String dateInService, String lastName, String letterBoxNumber, String nationalRegistreryNumber, String dateOutService, String password, int postalCode, String pseudo, String sex, int role, String phoneNumber, double salary1, double salary2, String status, int employeeNumber, String birthday, String phoneCountry, String country, String iban) {
        this.id = id;
        this.address = address;
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
        this.phoneNumber = phoneNumber;
        this.salary1 = salary1;
        this.salary2 = salary2;
        this.status = status;
        this.employeeNumber = employeeNumber;
        this.birthday = birthday;
        this.country = country;
        this.phoneCountry = phoneCountry;
        this.iban = iban;
    }

    //endregion

    @Override
    public String toString() {
        return firstName.toUpperCase() + " " + lastName.toUpperCase();
    }




    //region getters and setters


    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneCountry() {
        return phoneCountry;
    }

    public void setPhoneCountry(String phoneCountry) {
        this.phoneCountry = phoneCountry;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDateInService() {
        return dateInService;
    }

    public void setDateInService(String dateInService) {
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

    public String getDateOutService() {
        return dateOutService;
    }

    public void setDateOutService(String dateOutService) {
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getSalary1() {
        return salary1;
    }

    public void setSalary1(double salary1) {
        this.salary1 = salary1;
    }

    public double getSalary2() {
        return salary2;
    }

    public void setSalary2(double salary2) {
        this.salary2 = salary2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }


    //endregion
}
