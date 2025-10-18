package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.PaymentGateway;
import services.TokenizationService;
import utils.Enums.PaymentMethod;
import utils.Enums.TransactionStatus;

import java.io.IOException;
import java.util.UUID;

public class PaymentController {

    @FXML private Label totalAmountLabel;
    @FXML private RadioButton creditCardRadio;
    @FXML private RadioButton debitCardRadio;
    @FXML private RadioButton mobilePayRadio;
    @FXML private ToggleGroup paymentMethodGroup;
    @FXML private VBox cardDetailsBox;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private Label statusLabel;

    private double totalAmount;
    private String ticketType;
    private String zone;
    private int quantity;
    private PaymentGateway paymentGateway;
    private TokenizationService tokenizationService;

    public PaymentController() {
        this.paymentGateway = new PaymentGateway();
        this.tokenizationService = new TokenizationService();
    }

    /**
     * Initialize UI components and listeners
     */
    @FXML
    public void initialize() {
        // Listen for payment method changes
        paymentMethodGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == mobilePayRadio) {
                // Show mobile payment fields
                cardNumberField.setPromptText("Phone Number");
                expiryField.setVisible(false);
                cvvField.setVisible(false);
            } else {
                // Show card payment fields
                cardNumberField.setPromptText("Card Number");
                expiryField.setVisible(true);
                cvvField.setVisible(true);
            }
        });
    }

    /**
     * Initialize payment screen with transaction details
     */
    public void initializePayment(double amount, String ticketType, String zone, int quantity) {
        this.totalAmount = amount;
        this.ticketType = ticketType;
        this.zone = zone;
        this.quantity = quantity;

        totalAmountLabel.setText(String.format("$%.2f", amount));

        System.out.println("Payment screen initialized:");
        System.out.println("Amount: $" + amount);
        System.out.println("Type: " + ticketType);
        System.out.println("Zone: " + zone);
    }

    /**
     * Validate card input
     */
    private boolean validateCardDetails() {
        String cardNumber = cardNumberField.getText().trim();
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();

        // Clear previous errors
        statusLabel.setVisible(false);

        // Validate card number (16 digits)
        if (!cardNumber.matches("\\d{16}")) {
            showError("Card number must be 16 digits");
            return false;
        }

        // Validate expiry (MM/YY format)
        if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            showError("Expiry must be in MM/YY format");
            return false;
        }

        // Validate CVV (3-4 digits)
        if (!cvv.matches("\\d{3,4}")) {
            showError("CVV must be 3 or 4 digits");
            return false;
        }

        return true;
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
    }

    /**
     * Get selected payment method
     */
    private PaymentMethod getSelectedPaymentMethod() {
        if (creditCardRadio.isSelected()) return PaymentMethod.CREDIT_CARD;
        if (debitCardRadio.isSelected()) return PaymentMethod.DEBIT_CARD;
        if (mobilePayRadio.isSelected()) return PaymentMethod.MOBILE_PAY;
        return PaymentMethod.CREDIT_CARD;
    }

    /**
     * Handle payment processing
     */
    @FXML
    public void handleProcessPayment(ActionEvent event) {
        // Get payment method
        PaymentMethod method = getSelectedPaymentMethod();

        String token;

        if (method == PaymentMethod.MOBILE_PAY) {
            // For mobile payment, use phone number or wallet ID instead of card
            String mobileIdentifier = cardNumberField.getText().trim();

            // Validate mobile identifier (e.g., phone number)
            if (!mobileIdentifier.matches("\\d{10,15}")) {
                showError("Please enter a valid phone number (10-15 digits)");
                return;
            }

            token = tokenizationService.tokenize(mobileIdentifier);
        } else {
            // Validate card details for card payments
            if (!validateCardDetails()) {
                return;
            }

            // Tokenize card information
            String cardNumber = cardNumberField.getText().trim();
            token = tokenizationService.tokenize(cardNumber);
        }

        // Generate transaction ID
        String transactionId = UUID.randomUUID().toString();

        System.out.println("Processing payment...");
        System.out.println("Method: " + method);
        System.out.println("Amount: $" + totalAmount);
        System.out.println("Token: " + token);
        System.out.println("Transaction ID: " + transactionId);

        // Process payment through gateway
        TransactionStatus status = paymentGateway.processPayment(totalAmount, method, token);

        // Navigate to confirmation screen
        navigateToConfirmation(event, status, transactionId);
    }

    /**
     * Navigate to confirmation screen
     */
    private void navigateToConfirmation(ActionEvent event, TransactionStatus status, String transactionId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentConfirmationScreen.fxml"));
            Parent newScreen = loader.load();

            PaymentConfirmationController controller = loader.getController();
            controller.initializeConfirmation(status, transactionId, totalAmount, ticketType, zone, quantity);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Payment Confirmation Screen");
            e.printStackTrace();
        }
    }

    /**
     * Handle cancel button
     */
    @FXML
    public void handleCancel(ActionEvent event) {
        NavigationUtil.switchScene(event, "/fxml/TicketSummaryScreen.fxml");
    }
}