package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import services.FareService;

import java.io.IOException;

import static application.controllers.NavigationUtil.switchScene;
import static utils.Enums.ZoneType.*;

public class BuyMultipleTicketsController {

    @FXML
    private Spinner<Integer> quantitySpinner;

    private FareService fareService = new FareService();
    private String selectedZone;

    @FXML
    public void initialize() {
        // Set up spinner with values from 2 to 10
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10, 2);
        quantitySpinner.setValueFactory(valueFactory);
    }

    public void setSelectedZone(String zone) {
        this.selectedZone = zone;
    }

    @FXML
    public void handleConfirm(ActionEvent event) {
        int quantity = quantitySpinner.getValue();
        navigateToTicketSummary(event, selectedZone, quantity);
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        switchScene("", event, "/fxml/MainScreen.fxml");
    }

    /**
     * Navigate to ticket summary with quantity
     */
    private void navigateToTicketSummary(ActionEvent event, String zone, int quantity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TicketSummaryScreen.fxml"));
            Parent newScreen = loader.load();

            // Get the price from FareService based on zone
            double price = 0;
            switch (zone.toUpperCase()) {
                case "A":
                    price = fareService.getTicketFare(A, 1, false);
                    break;
                case "AB":
                    price = fareService.getTicketFare(AB, 1, false);
                    break;
                case "ABC":
                    price = fareService.getTicketFare(ABC, 1, false);
                    break;
                case "ABCD":
                    price = fareService.getTicketFare(ABCD, 1, false);
                    break;
            }

            // Get controller and initialize with data
            TicketSummaryController controller = loader.getController();
            controller.initializeSummary("BUY_MULTIPLE_TICKETS", zone, quantity, price);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Ticket Summary Screen");
            e.printStackTrace();
        }
    }

}