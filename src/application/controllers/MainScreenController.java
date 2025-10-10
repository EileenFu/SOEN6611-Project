package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.User;

public class MainScreenController {

    @FXML
    private Label welcomeLabel; // Add this to your FXML

    private User currentUser;

    // Call this method from LoginController after successful login
    public void setUserData(User user) {
        this.currentUser = user;

        // Display user's name
        String fullName = user.getFirstName() + " " + user.getLastName();
        welcomeLabel.setText("Welcome, " + fullName + "!");

        // You can now access all user data:
        System.out.println("Current user email: " + user.getEmail());
    }
}