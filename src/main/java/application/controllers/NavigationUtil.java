package application.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class NavigationUtil {

    /**
     * Load an FXML screen into a StackPane dynamically.
     *
     * @param container       StackPane where the new content will be injected
     * @param fxmlPath        Path to the FXML file (e.g. "/fxml/MainScreen.fxml")
     * @param controllerSetup Optional lambda to configure the controller after loading
     */
    public static void setContent(StackPane container, String fxmlPath, Consumer<Object> controllerSetup) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            Parent screen = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                // Inject main container if controller has setMainContainer(StackPane)
                try {
                    Method method = controller.getClass().getMethod("setMainContainer", StackPane.class);
                    method.invoke(controller, container);
                } catch (NoSuchMethodException ignored) {
                    // Controller does not define setMainContainer, skip
                }

                // Run optional controller setup
                if (controllerSetup != null) {
                    controllerSetup.accept(controller);
                }
            }

            container.getChildren().setAll(screen);

        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error during controller setup for: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Overload for cases where no controller configuration is needed.
     */
    public static void setContent(StackPane container, String fxmlPath) {
        setContent(container, fxmlPath, null);
    }

    /**
     * Utility to load controller only, without injecting into the UI.
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
