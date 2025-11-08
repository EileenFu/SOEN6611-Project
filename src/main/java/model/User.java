package model;

import model.Ticket;
import utils.Enums.ZoneType;

public class User {
    final private String firstName;
    final private String lastName;
    final private String email;
    final private String password;

    public double price;


    public static Ticket ticket;

    public User(String firstName, String lastName, String email, String password, char zoneType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
