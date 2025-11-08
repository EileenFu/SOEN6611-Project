package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import java.io.IOException;

/**
 * Controller for BorderContainer.fxml
 * This wraps the header, footer, and contains a dynamic mainContainer
 * where all inner screens (like MainScreen) will be loaded.
 */
public class BorderContainerController {

    @FXML
    private StackPane mainContainer; // dynamic content area

    @FXML
    public void initialize() {
        // Load the main screen content into the container at startup
        loadMainScreenContent();
    }

    private void loadMainScreenContent() {
        try {
            // Load the inner MainScreen FXML (service selection content)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScreen.fxml"));
            Parent mainScreenContent = loader.load();

            // Get the controller for MainScreenContent and pass the mainContainer reference
            MainScreenController controller = loader.getController();
            controller.setMainContainer(mainContainer);

            // Inject the content into the dynamic container
            mainContainer.getChildren().setAll(mainScreenContent);

        } catch (IOException e) {
            System.err.println("Failed to load MainScreen.fxml");
            e.printStackTrace();
        }
    }

    /**
     * Public method to allow other screens to navigate via this mainContainer
     */
    public StackPane getMainContainer() {
        return mainContainer;
    }
}
