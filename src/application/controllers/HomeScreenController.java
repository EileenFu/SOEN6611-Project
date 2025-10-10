package application.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeScreenController{
	
	@FXML
	private Button login;
	
	public void switchToLogin(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
		Parent loginRoot = loader.load();
		
		LoginController loginController = loader.getController();
        loginController.setPreviousScene(((Node) event.getSource()).getScene());
        
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(loginRoot);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSignup(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
		Parent loginRoot = loader.load();
		
		SignupController signupController = loader.getController();
        signupController.setPreviousScene(((Node) event.getSource()).getScene());
        
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(loginRoot);
		stage.setScene(scene);
		stage.show();
	}
}

