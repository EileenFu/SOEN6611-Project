package application.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.function.Consumer;

public class NavigationUtil {

    /**
     * Load an FXML screen into a StackPane dynamically
     *
     * @param container StackPane where the new content will be injected
     * @param fxmlPath  Path to the FXML file
     * @param controllerSetup Optional lambda to configure the controller after loading
     */
    public static void setContent(StackPane container, String fxmlPath, Consumer<Object> controllerSetup) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            Parent screen = loader.load();

            if (controllerSetup != null) {
                Object controller = loader.getController();
                controllerSetup.accept(controller);
            }

            container.getChildren().setAll(screen);

        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Overload for cases where you don't need to configure the controller
     */
    public static void setContent(StackPane container, String fxmlPath) {
        setContent(container, fxmlPath, null);
    }

    /**
     * Utility to load controller only, without injecting into UI
     */
    public static Object loadFXMLController(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            System.err.println("Error loading controller for FXML: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }
}
