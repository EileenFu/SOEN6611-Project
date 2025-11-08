package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.StackPane;
import services.FareService;

import java.io.IOException;

import static utils.Enums.ZoneType.*;

public class MultipleTicketsController {

    @FXML
    private Spinner<Integer> quantitySpinner;

    private FareService fareService = new FareService();
    private String selectedZone;

    // Reference to the main container in MainScreen
    private StackPane mainContainer;

    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void setSelectedZone(String zone) {
        this.selectedZone = zone;
    }

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10, 2);
        quantitySpinner.setValueFactory(valueFactory);
    }

    @FXML
    public void handleConfirm() {
        if (mainContainer == null || selectedZone == null) return;

        int quantity = quantitySpinner.getValue();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TicketSummaryScreen.fxml"));
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(loader.load());

            TicketSummaryController controller = loader.getController();
            double price = calculatePrice(selectedZone, quantity);
            controller.initializeSummary("BUY_MULTIPLE_TICKETS", selectedZone, quantity, price);
            controller.setMainContainer(mainContainer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel() {
        if (mainContainer != null) {
            NavigationUtil.setContent(mainContainer, "/fxml/MainScreen.fxml");
        }
    }

    private double calculatePrice(String zone, int quantity) {
        return switch (zone.toUpperCase()) {
            case "A" -> fareService.getTicketFare(A, quantity, false);
            case "AB" -> fareService.getTicketFare(AB, quantity, false);
            case "ABC" -> fareService.getTicketFare(ABC, quantity, false);
            case "ABCD" -> fareService.getTicketFare(ABCD, quantity, false);
            default -> 0;
        };
    }
}
