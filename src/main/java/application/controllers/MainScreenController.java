package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

/**
 * Controller for the main screen content (service selection).
 * All navigation is done via the mainContainer inside BorderContainer.
 */
public class MainScreenController {

    @FXML
    private StackPane mainContainer; // injected from BorderContainer
    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    /**
     * Generic helper to navigate to a screen and initialize its controller.
     */
    private <T> void navigateTo(String fxmlPath, Class<T> controllerClass, ControllerInitializer<T> initializer) {
        NavigationUtil.setContent(mainContainer, fxmlPath, controller -> {
            if (controllerClass.isInstance(controller)) {
                initializer.setup(controllerClass.cast(controller));
            }
        });
    }

    /** Navigate to ZoneTypeScreen for single ticket purchase */
    @FXML
    private void handleBuySingleTicket() {
        navigateTo("/fxml/ZoneTypeScreen.fxml", ZoneTypeController.class, ztc -> {
            ztc.setPreviousAction("BUY_SINGLE_TICKET");
            ztc.setMainContainer(mainContainer);
        });
    }

    /** Navigate to ZoneTypeScreen for multiple ticket purchase */
    @FXML
    private void handleBuyMultipleTickets() {
        navigateTo("/fxml/ZoneTypeScreen.fxml", ZoneTypeController.class, ztc -> {
            ztc.setPreviousAction("BUY_MULTIPLE_TICKETS");
            ztc.setMainContainer(mainContainer);
        });
    }

    /** Navigate to OPUS card recharge screen */
    @FXML
    private void handleRechargeOpusCard() {
        navigateTo("/fxml/OpusCardScreen.fxml", OpusCardController.class, opc -> {
            opc.setMainContainer(mainContainer);
        });
    }

    /** Navigate to unlimited pass purchase screen */
    @FXML
    private void handleBuyUnlimitedPass() {
        navigateTo("/fxml/UnlimitedPassScreen.fxml", UnlimitedPassController.class, upc -> {
            upc.setMainContainer(mainContainer);
        });
    }

    /**
     * Functional interface for controller initialization.
     * Allows setting the mainContainer or other properties dynamically.
     */
    @FunctionalInterface
    private interface ControllerInitializer<T> {
        void setup(T controller);
    }
}
