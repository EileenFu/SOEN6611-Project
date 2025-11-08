package application;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Load startup screen
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartupScreen.fxml"));
            Scene scene = new Scene(root, Color.BLACK);

            // Load icon
            Image icon = new Image(getClass().getResource("/Images/iGo_icon.png").toExternalForm());
            stage.getIcons().add(icon);
            stage.setTitle("iGo - TVM System");

            // Apply stylesheet
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Wait 2 seconds then load main application wrapper
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                try {
                    // Load BorderContainer which includes header/footer and a mainContainer
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BorderContainer.fxml"));
                    System.out.println(getClass().getResource("/fxml/BorderContainer.fxml"));
                    Parent borderRoot = loader.load();

                    Scene mainScene = new Scene(borderRoot);

                    // Apply CSS
                    mainScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

                    stage.setScene(mainScene);
                    stage.setTitle("iGo - TVM System");
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
