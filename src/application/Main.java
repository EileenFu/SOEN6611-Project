package application;
	
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartupScreen.fxml"));
			Scene scene = new Scene(root, Color.BLACK);
			
			Image icon = new Image(getClass().getResource("/Images/iGo_icon.png").toExternalForm());
	
			stage.getIcons().add(icon);
			stage.setTitle("iGo - TVM System");
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
			
			// Wait for 5 seconds, then show main stage
	        PauseTransition delay = new PauseTransition(Duration.seconds(2));
	        delay.setOnFinished(event -> {
	            try {
	                // Load main window
	                Parent mainRoot = FXMLLoader.load(getClass().getResource("/fxml/MainScreen.fxml"));
	                
	                stage.setScene(new Scene(mainRoot));
	                stage.setTitle("Welcome!");
					stage.setResizable(true);
					stage.centerOnScreen();
					stage.setMaximized(true);
	                stage.show();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
	        delay.play();
	    
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
