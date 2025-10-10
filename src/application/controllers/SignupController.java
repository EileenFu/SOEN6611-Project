package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.UserManager;


public class SignupController {

    @FXML
    public TextField nameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public DatePicker birthdatePicker;
    @FXML
    private Scene previousScene;


    public void signup(ActionEvent event) {
        String firstName = nameField.getText().split(" ")[0];
        String lastName = nameField.getText().split(" ")[1];

        User user = new User(firstName, lastName, emailField.getText(), passwordField.getText());
        if (UserManager.addUser(user)) {
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Account created successfully for " + user.getFirstName() + "!");

            // Clear form
            clearForm();

            // Go back to home screen
            goBack(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Username '" + user.getFirstName() + "' already exists!");
        }
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
    public void goBack(ActionEvent event) {
        if (previousScene != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(previousScene);
            stage.show();
        }
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        birthdatePicker.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
