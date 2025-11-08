package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import services.FareService;
import java.io.IOException;

public class TicketSummaryController {

    @FXML private Label ticketTypeLabel;
    @FXML private Label zoneLabel;
    @FXML private Label quantityLabel;
    @FXML private VBox quantityBox;
    @FXML private Label ticketPriceLabel;
    @FXML private Label totalLabel;
    @FXML private Label validityLabel;

    private FareService fareService = new FareService();
    private String ticketType;
    private String zone;
    private int quantity = 1;
    private double ticketPrice;
    private double total;

    // Reference to main container in MainScreenController
    private StackPane mainContainer;

    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    // ------------------ Initialization Methods ------------------

    public void initializeSummary(String ticketType, String zone) {
        this.ticketType = ticketType;
        this.zone = zone;
        this.quantity = 1;
        updateDisplay();
    }

    public void initializeSummary(String ticketType, String zone, int quantity) {
        this.ticketType = ticketType;
        this.zone = zone;
        this.quantity = quantity;
        if (quantity > 1) quantityBox.setVisible(true);
        updateDisplay();
    }

    public void initializeSummary(String ticketType, String zone, int quantity, double ticketPrice) {
        this.ticketType = ticketType;
        this.zone = zone;
        this.quantity = quantity;
        this.ticketPrice = ticketPrice;
        if (quantity > 1) quantityBox.setVisible(true);
        updateDisplayWithPrice();
    }

    public void initializePassSummary(String zone, String passDuration, double passPrice) {
        this.ticketType = "BUY_UNLIMITED_PASS";
        this.zone = zone;
        this.quantity = 1;
        this.ticketPrice = passPrice;
        quantityBox.setVisible(false);

        ticketTypeLabel.setText(getPassDurationDisplayName(passDuration));
        zoneLabel.setText(zone);
        total = passPrice;
        ticketPriceLabel.setText(String.format("$%.2f", passPrice));
        totalLabel.setText(String.format("$%.2f (tax included)", total));
        updatePassValidityMessage(passDuration);
    }

    private String getPassDurationDisplayName(String duration) {
        switch (duration) {
            case "DAY": return "1 Day Pass";
            case "THREEDAY": return "3 Day Pass";
            case "WEEK": return "Week Pass";
            case "MONTH": return "Monthly Pass";
            case "NIGHT": return "Night Pass";
            case "WEEKEND": return "Weekend Pass";
            case "FOURMONTH": return "4-Month Pass";
            default: return "Unlimited Pass";
        }
    }

    private void updatePassValidityMessage(String duration) {
        switch (duration) {
            case "DAY": validityLabel.setText("Valid for 24 hours from first use"); break;
            case "THREEDAY": validityLabel.setText("Valid for 3 consecutive days"); break;
            case "WEEK": validityLabel.setText("Valid for 7 consecutive days"); break;
            case "MONTH": validityLabel.setText("Valid for 30 days from first use"); break;
            case "NIGHT": validityLabel.setText("Valid from 6pm to 5am"); break;
            case "WEEKEND": validityLabel.setText("Valid Saturday and Sunday unlimited"); break;
            case "FOURMONTH": validityLabel.setText("Valid for 120 consecutive days"); break;
            default: validityLabel.setText("Valid for selected duration");
        }
    }

    private void updateDisplay() {
        ticketTypeLabel.setText(getTicketTypeDisplayName(ticketType));
        zoneLabel.setText(zone);
        quantityLabel.setText(String.valueOf(quantity));
        total = ticketPrice * quantity;
        ticketPriceLabel.setText(String.format("$%.2f", ticketPrice));
        totalLabel.setText(String.format("$%.2f (tax included)", total));
        updateValidityMessage();
    }

    private void updateDisplayWithPrice() {
        updateDisplay();
    }

    private String getTicketTypeDisplayName(String ticketType) {
        switch (ticketType) {
            case "BUY_SINGLE_TICKET": return "Single Ticket";
            case "BUY_MULTIPLE_TICKETS": return "Multiple Tickets";
            case "RECHARGE_OPUS_CARD": return "OPUS Card Recharge";
            case "BUY_UNLIMITED_PASS": return "Unlimited Pass";
            default: return "Ticket";
        }
    }

    private void updateValidityMessage() {
        if (ticketType.equals("BUY_UNLIMITED_PASS")) {
            validityLabel.setText("Valid for 30 days from first use");
        } else {
            validityLabel.setText("Covers all transit modes in selected zone");
        }
    }

    // ------------------ Button Handlers ------------------

    @FXML
    public void handleConfirm(ActionEvent event) {
        // Load PaymentScreen dynamically
        if (mainContainer == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentScreen.fxml"));
            Parent paymentScreen = loader.load();

            PaymentController controller = loader.getController();
            controller.initializePayment(total, ticketType, zone, quantity);
            controller.setMainContainer(mainContainer);

            mainContainer.getChildren().setAll(paymentScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        // Return to Main Screen dynamically
        if (mainContainer == null) return;
        NavigationUtil.setContent(mainContainer, "/fxml/MainScreen.fxml");
    }

    @FXML
    public void handleBack(ActionEvent event) {
        // Return to ZoneTypeScreen dynamically
        if (mainContainer == null) return;
        NavigationUtil.setContent(mainContainer, "/fxml/ZoneTypeScreen.fxml");
    }

    // ------------------ Getters ------------------
    public double getTotal() { return total; }
    public double getTicketPrice() { return ticketPrice; }
}
