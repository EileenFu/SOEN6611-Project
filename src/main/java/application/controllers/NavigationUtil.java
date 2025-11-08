package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class NavigationUtil {

    public static void switchScene(String stageTitle, ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            Parent newScreen = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setFullScreenExitHint("");
            stage.setScene(scene);
            stage.setTitle(stageTitle);
            stage.setFullScreen(true);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Navigate to Zone Type selection screen with action context
     */
    static void navigateToZoneType(ActionEvent event, String action) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource("/fxml/ZoneTypeScreen.fxml"));
            Parent zoneTypeScreen = loader.load();

            // Get the controller and pass the action
            ZoneTypeController controller = loader.getController();
            controller.setPreviousAction(action);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(zoneTypeScreen);
            stage.setTitle("Select Zone Type");
            stage.setFullScreenExitHint("");
            stage.setScene(scene);
            stage.setFullScreen(true);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
