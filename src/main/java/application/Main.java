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
            // Load startup screen from resources/fxml
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartupScreen.fxml"));
            Scene scene = new Scene(root, Color.BLACK);

            // Load icon from resources/Images
            Image icon = new Image(getClass().getResource("/Images/iGo_icon.png").toExternalForm());
            stage.getIcons().add(icon);
            stage.setTitle("iGo - TVM System");

            // Load stylesheet from resources root
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Wait for 2 seconds, then load main screen
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                try {
                    Parent mainRoot = FXMLLoader.load(getClass().getResource("/fxml/MainScreen.fxml"));
                    Scene mainScene = new Scene(mainRoot);

                    // Apply the same CSS if needed
                    mainScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

                    stage.setScene(mainScene);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
