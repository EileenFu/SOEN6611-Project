package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.FareService;
import utils.Enums;

import java.io.IOException;

import static application.controllers.NavigationUtil.switchScene;
import static utils.Enums.ZoneType.*;

public class ZoneTypeController {

    FareService fareService = new FareService();
    private String previousAction;
    private Enums.PassDuration selectedPassDuration;

    public void setPreviousAction(String action) {
        this.previousAction = action;
        System.out.println("Previous action set to: " + action);
    }

    public void setPassDuration(Enums.PassDuration duration) {
        this.selectedPassDuration = duration;
        System.out.println("Pass duration set to: " + duration);
        System.out.println("selectedPassDuration is now: " + this.selectedPassDuration);
    }

    /**
     * Validates that previousAction is set before processing
     */
    private boolean isValidState() {
        if (previousAction == null) {
            System.err.println("Error: previousAction is not set!");
            return false;
        }
        return true;
    }

    @FXML
    public void handleZoneA(ActionEvent event) {
        if (!isValidState()) return;

        switch (previousAction){
            case "BUY_SINGLE_TICKET":
                System.out.println("Zone A selected");
                double price = fareService.getTicketFare(A, 1, false);
                System.out.println("Price: " + price);
                navigateToTicketSummary(event, "A", 1, price);
                break;
            case "BUY_MULTIPLE_TICKETS":
                navigateToQuantitySelection(event, "A");
                break;
            case "BUY_UNLIMITED_PASS":
                navigateToPassSummary(event, A);
                break;
            case "ADD_TICKET_TO_OPUS":
                navigateToQuantitySelection(event, "A");
                break;
            default:
                System.err.println("Unknown action: " + previousAction);
                break;
        }
    }

    @FXML
    public void handleZoneAB(ActionEvent event) {
        if (!isValidState()) return;

        switch (previousAction){
            case "BUY_SINGLE_TICKET":
                System.out.println("Zone AB selected");
                double price = fareService.getTicketFare(AB, 1, false);
                System.out.println("Price: " + price);
                navigateToTicketSummary(event, "AB", 1, price);
                break;
            case "BUY_MULTIPLE_TICKETS":
                navigateToQuantitySelection(event, "AB");
                break;
            case "BUY_UNLIMITED_PASS":
                navigateToPassSummary(event, AB);
                break;
            case "ADD_TICKET_TO_OPUS":
                navigateToQuantitySelection(event, "AB");
                break;
            default:
                System.err.println("Unknown action: " + previousAction);
                break;
        }
    }

    @FXML
    public void handleZoneABC(ActionEvent event) {
        if (!isValidState()) return;

        switch (previousAction){
            case "BUY_SINGLE_TICKET":
                System.out.println("Zone ABC selected");
                double price = fareService.getTicketFare(ABC, 1, false);
                System.out.println("Price: " + price);
                navigateToTicketSummary(event, "ABC", 1, price);
                break;
            case "BUY_MULTIPLE_TICKETS":
                navigateToQuantitySelection(event, "ABC");
                break;
            case "BUY_UNLIMITED_PASS":
                navigateToPassSummary(event, ABC);
                break;
            case "ADD_TICKET_TO_OPUS":
                navigateToQuantitySelection(event, "ABC");
                break;
            default:
                System.err.println("Unknown action: " + previousAction);
                break;
        }
    }

    @FXML
    public void handleZoneABCD(ActionEvent event) {
        if (!isValidState()) return;

        switch (previousAction){
            case "BUY_SINGLE_TICKET":
                System.out.println("Zone ABCD selected");
                double price = fareService.getTicketFare(ABCD, 1, false);
                System.out.println("Price: " + price);
                navigateToTicketSummary(event, "ABCD", 1, price);
                break;
            case "BUY_MULTIPLE_TICKETS":
                navigateToQuantitySelection(event, "ABCD");
                break;
            case "BUY_UNLIMITED_PASS":
                navigateToPassSummary(event, ABCD);
                break;
            case "RECHARGE_OPUS_CARD":
                switchScene(event, "/fxml/OpusCardScreen.fxml");
                break;
            case "ADD_TICKET_TO_OPUS":
                navigateToQuantitySelection(event, "ABCD");
                break;
            default:
                System.err.println("Unknown action: " + previousAction);
                break;
        }
    }

    private void navigateToPassSummary(ActionEvent event, Enums.ZoneType zone) {
        try {
            // Add null check for selectedPassDuration
            if (selectedPassDuration == null) {
                System.err.println("Error: selectedPassDuration is null!");
                System.err.println("Cannot calculate pass fare. Returning to main screen.");
                switchScene(event, "/fxml/MainScreen.fxml");
                return;
            }

            System.out.println("Calculating pass fare for zone: " + zone + ", duration: " + selectedPassDuration);
            double price = fareService.getPassFare(zone, selectedPassDuration, false);
            System.out.println("Calculated price: " + price);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TicketSummaryScreen.fxml"));
            Parent newScreen = loader.load();

            TicketSummaryController controller = loader.getController();
            controller.initializePassSummary(zone.name(), selectedPassDuration.name(), price);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Ticket Summary Screen");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        // No validation needed - just navigate back to main screen
        switchScene(event, "/fxml/MainScreen.fxml");
    }

    /**
     * Navigate to ticket summary with proper initialization
     */
    private void navigateToTicketSummary(ActionEvent event, String zone, int quantity, double ticketPrice) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TicketSummaryScreen.fxml"));
            Parent newScreen = loader.load();

            // Get controller and initialize with data
            TicketSummaryController controller = loader.getController();
            controller.initializeSummary(previousAction, zone, quantity, ticketPrice);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Ticket Summary Screen");
            e.printStackTrace();
        }
    }

    /**
     * Navigate to quantity selection screen for multiple tickets
     */
    private void navigateToQuantitySelection(ActionEvent event, String zone) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MultipleTicketsScreen.fxml"));
            Parent newScreen = loader.load();

            // Get controller and pass the selected zone
            BuyMultipleTicketsController controller = loader.getController();
            controller.setSelectedZone(zone);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Multiple Tickets Screen");
            e.printStackTrace();
        }
    }
}