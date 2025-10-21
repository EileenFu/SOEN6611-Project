package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.FareService;

import java.io.IOException;

import static application.controllers.NavigationUtil.switchScene;

public class TicketSummaryController {

    @FXML
    private Label ticketTypeLabel;

    @FXML
    private Label zoneLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private VBox quantityBox;

    @FXML
    private Label ticketPriceLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label validityLabel;

    private FareService fareService = new FareService();
    private String ticketType;
    private String zone;
    private int quantity = 1;
    private double ticketPrice;
    private double total;

    /**
     * Initialize the summary with ticket details (calculates price internally)
     */
    public void initializeSummary(String ticketType, String zone) {
        this.ticketType = ticketType;
        this.zone = zone;
        this.quantity = 1;

        updateDisplay();
    }

    /**
     * Initialize with quantity for multiple tickets (calculates price internally)
     */
    public void initializeSummary(String ticketType, String zone, int quantity) {
        this.ticketType = ticketType;
        this.zone = zone;
        this.quantity = quantity;

        // Show quantity box for multiple tickets
        if (quantity > 1) {
            quantityBox.setVisible(true);
        }

        updateDisplay();
    }


    /**
     * Initialize with pre-calculated ticket price from FareService
     */
    public void initializeSummary(String ticketType, String zone, int quantity, double ticketPrice) {
        this.ticketType = ticketType;
        this.zone = zone;
        this.quantity = quantity;
        this.ticketPrice = ticketPrice;

        // Show quantity box for multiple tickets
        if (quantity > 1) {
            quantityBox.setVisible(true);
        }

        updateDisplayWithPrice();
    }

    /**
     * Initialize pass summary with duration and price
     */
    public void initializePassSummary(String zone, String passDuration, double passPrice) {
        this.ticketType = "BUY_UNLIMITED_PASS";
        this.zone = zone;
        this.quantity = 1;
        this.ticketPrice = passPrice;

        // Hide quantity box for passes
        quantityBox.setVisible(false);

        // Set ticket type to show pass duration
        ticketTypeLabel.setText(getPassDurationDisplayName(passDuration));

        // Set zone
        zoneLabel.setText(zone);

        // Calculate total (taxes already included in fare)
        total = passPrice;

        // Update price labels
        ticketPriceLabel.setText(String.format("$%.2f", passPrice));
        totalLabel.setText(String.format("$%.2f (taxes included)", total));

        // Update validity message for passes
        updatePassValidityMessage(passDuration);
    }

    /**
     * Get display name for pass duration
     */
    private String getPassDurationDisplayName(String duration) {
        switch (duration) {
            case "DAY":
                return "1 Day Pass";
            case "THREEDAY":
                return "3 Day Pass";
            case "WEEK":
                return "Week Pass";
            case "MONTH":
                return "Monthly Pass";
            case "NIGHT":
                return "Night Pass";
            case "WEEKEND":
                return "Weekend Pass";
            case "FOURMONTH":
                return "4-Month Pass";
            default:
                return "Unlimited Pass";
        }
    }

    /**
     * Update validity message for passes
     */
    private void updatePassValidityMessage(String duration) {
        switch (duration) {
            case "DAY":
                validityLabel.setText("Valid for 24 hours from first use");
                break;
            case "THREEDAY":
                validityLabel.setText("Valid for 3 consecutive days");
                break;
            case "WEEK":
                validityLabel.setText("Valid for 7 consecutive days");
                break;
            case "MONTH":
                validityLabel.setText("Valid for 30 days from first use");
                break;
            case "NIGHT":
                validityLabel.setText("Valid from 6pm to 5am");
                break;
            case "WEEKEND":
                validityLabel.setText("Valid Saturday and Sunday unlimited");
                break;
            case "FOURMONTH":
                validityLabel.setText("Valid for 120 consecutive days");
                break;
            default:
                validityLabel.setText("Valid for selected duration");
        }
    }

    /**
     * Update all display labels with calculated values (when price needs to be fetched)
     */
    private void updateDisplay() {
        // Set ticket type
        ticketTypeLabel.setText(getTicketTypeDisplayName(ticketType));

        // Set zone
        zoneLabel.setText(zone);

        // Set quantity
        quantityLabel.setText(String.valueOf(quantity));

        // Calculate total (taxes already included in fare)
        total = ticketPrice * quantity;

        // Update price labels
        ticketPriceLabel.setText(String.format("$%.2f", ticketPrice));
        totalLabel.setText(String.format("$%.2f (taxes included)", total));

        // Update validity message
        updateValidityMessage();
    }

    /**
     * Update display with pre-calculated ticket price
     */
    private void updateDisplayWithPrice() {
        // Set ticket type
        ticketTypeLabel.setText(getTicketTypeDisplayName(ticketType));

        // Set zone
        zoneLabel.setText(zone);

        // Set quantity
        quantityLabel.setText(String.valueOf(quantity));

        // Calculate total (taxes already included in fare)
        total = ticketPrice * quantity;

        // Update price labels
        ticketPriceLabel.setText(String.format("$%.2f", ticketPrice));
        totalLabel.setText(String.format("$%.2f (taxes included)", total));

        // Update validity message
        updateValidityMessage();
    }

    /**
     * Get display name for ticket type
     */
    private String getTicketTypeDisplayName(String ticketType) {
        switch (ticketType) {
            case "BUY_SINGLE_TICKET":
                return "Single Ticket";
            case "BUY_MULTIPLE_TICKETS":
                return "Multiple Tickets";
            case "RECHARGE_OPUS_CARD":
                return "OPUS Card Recharge";
            case "BUY_UNLIMITED_PASS":
                return "Unlimited Pass";
            default:
                return "Ticket";
        }
    }

    /**
     * Update validity message based on ticket type
     */
    private void updateValidityMessage() {
        if (ticketType.equals("BUY_UNLIMITED_PASS")) {
            validityLabel.setText("Valid for 30 days from first use");
        } else {
            validityLabel.setText("Covers all transit modes in selected zone");
        }
    }

    @FXML
    public void handleConfirm(ActionEvent event) {
        System.out.println("Purchase confirmed!");
        System.out.println("Type: " + ticketType);
        System.out.println("Zone: " + zone);
        System.out.println("Quantity: " + quantity);

        // Navigate to payment screen
        navigateToPayment(event);
    }

    /**
     * Navigate to payment screen
     */
    private void navigateToPayment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentScreen.fxml"));
            Parent newScreen = loader.load();

            PaymentController controller = loader.getController();
            controller.initializePayment(total, ticketType, zone, quantity);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Payment Screen");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        // Go back to main screen
        switchScene("Main Screen", event, "/fxml/MainScreen.fxml");
    }

    @FXML
    public void handleBack(ActionEvent event) {
        // Go back to zone selection
        switchScene("Zone Type", event, "/fxml/ZoneTypeScreen.fxml");
    }

    /**
     * Get stored price values
     */
    public double getTotal() {
        return total;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

}