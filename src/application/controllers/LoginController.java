package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import model.User;
import utils.UserManager;

public class LoginController {

    private User loggedInUser; // Store the logged in user

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private Scene previousScene;

    @FXML
    public void login(ActionEvent event) throws Exception {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter email and password!");
            return;
        }

        // Fetch user from database
        User user = UserManager.getUserByLogin(email, password);

        if (user != null) {
            // Login successful - store the user
            loggedInUser = user;

            // Display welcome message with user's name
            String fullName = user.getFirstName() + " " + user.getLastName();
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Welcome back, " + fullName + "!");

            // You can access user data anytime now:
            System.out.println("Logged in user: " + user);
            System.out.println("First Name: " + user.getFirstName());
            System.out.println("Last Name: " + user.getLastName());
            System.out.println("Email: " + user.getEmail());

            // Navigate to next screen here (pass the user object if needed)
            // navigateToMainScreen(event, loggedInUser);

        } else {
            // Login failed
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Invalid email or password!");
        }

        navigateToMainScreen(event, user);
    }

    // Getter for logged in user (can be used by other controllers)
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    @FXML
    private void goBack(ActionEvent event) {
        if (previousScene != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(previousScene);
            stage.show();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToMainScreen(ActionEvent event, User user) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScreen.fxml"));
        Parent root = loader.load();

        // Get the controller and pass user data
        MainScreenController controller = loader.getController();
        controller.setUserData(user);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}