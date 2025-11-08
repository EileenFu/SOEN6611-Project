package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import services.PaymentGateway;
import utils.Enums.TransactionStatus;


import java.io.IOException;

public class PaymentConfirmationController {

    @FXML private VBox successBox;
    @FXML private VBox failureBox;
    @FXML private Label transactionIdLabel;
    @FXML private Label amountLabel;
    @FXML private Label ticketInfoLabel;
    @FXML private Label errorMessageLabel;
    @FXML private Label failedTransactionIdLabel;

    private String transactionId;
    private double amount;
    private String ticketType;
    private String zone;
    private int quantity;
    private TransactionStatus status;
    private PaymentGateway paymentGateway;

    // Reference to main container for dynamic navigation
    private StackPane mainContainer;

    public PaymentConfirmationController() {
        this.paymentGateway = new PaymentGateway();
    }

    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void initializeConfirmation(TransactionStatus status, String transactionId,
                                       double amount, String ticketType, String zone, int quantity) {
        this.status = status;
        this.transactionId = transactionId;
        this.amount = amount;
        this.ticketType = ticketType;
        this.zone = zone;
        this.quantity = quantity;

        if (status == TransactionStatus.SUCCESS) {
            showSuccess();
        } else {
            showFailure();
        }
    }

    private void showSuccess() {
        successBox.setVisible(true);
        successBox.setManaged(true);
        failureBox.setVisible(false);
        failureBox.setManaged(false);

        transactionIdLabel.setText("Transaction ID: " + transactionId);
        amountLabel.setText(String.format("Amount: $%.2f", amount));
        ticketInfoLabel.setText(String.format("%s - Zone %s (Qty: %d)",
                getTicketTypeDisplayName(ticketType), zone, quantity));
    }

    private void showFailure() {
        successBox.setVisible(false);
        successBox.setManaged(false);
        failureBox.setVisible(true);
        failureBox.setManaged(true);

        errorMessageLabel.setText("Payment could not be processed. Please check your card details and try again.");
        failedTransactionIdLabel.setText("Transaction ID: " + transactionId);
    }

    private String getTicketTypeDisplayName(String ticketType) {
        switch (ticketType) {
            case "BUY_SINGLE_TICKET": return "Single Ticket";
            case "BUY_MULTIPLE_TICKETS": return "Multiple Tickets";
            case "BUY_UNLIMITED_PASS": return "Unlimited Pass";
            default: return "Ticket";
        }
    }

    @FXML
    public void handlePrintTicket(ActionEvent event) {
        System.out.println("Printing ticket...");
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Zone: " + zone);
        System.out.println("Quantity: " + quantity);
        // TODO: Implement actual printing functionality
    }

    @FXML
    public void handleDone(ActionEvent event) {
        if (mainContainer != null) {
            // Navigate back to MainScreen
            NavigationUtil.setContent(mainContainer, "/fxml/MainScreen.fxml");
        }
    }

    @FXML
    public void handleRefund(ActionEvent event) {
        if (paymentGateway == null) return;

        System.out.println("Processing refund for transaction: " + transactionId);

        TransactionStatus refundStatus = paymentGateway.refundPayment(transactionId);

        if (refundStatus == TransactionStatus.REFUNDED) {
            errorMessageLabel.setText("Refund processed successfully. Amount will be credited back to your account.");
            errorMessageLabel.setStyle("-fx-text-fill: green;");
        } else {
            errorMessageLabel.setText("Refund failed. Please contact customer support.");
            System.err.println("Refund failed");
        }
    }

    @FXML
    public void handleRetry(ActionEvent event) {
        if (mainContainer != null) {
            NavigationUtil.setContent(mainContainer, "/fxml/TicketSummaryScreen.fxml");
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        if (mainContainer != null) {
            NavigationUtil.setContent(mainContainer, "/fxml/MainScreen.fxml");
        }
    }
}
