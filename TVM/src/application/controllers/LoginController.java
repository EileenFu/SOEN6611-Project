package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	String name;
	
	@FXML
	private TextField username;
	private PasswordField password;

	private Scene previousScene;
	
	public void login(ActionEvent event) {
		name = username.getText();
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
}