
package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.PaymentGateway;
import services.TokenizationService;
import utils.Enums.PaymentMethod;
import utils.Enums.TransactionStatus;

import java.io.IOException;
import java.util.UUID;

import static application.controllers.NavigationUtil.switchScene;

public class PaymentController {



    @FXML private Label totalAmountLabel;
    @FXML private RadioButton cashRadio;
    @FXML private RadioButton creditCardRadio;
    @FXML private RadioButton debitCardRadio;
    @FXML private RadioButton nfcRadio;
    @FXML private Button nfcButton;
    @FXML private ToggleGroup paymentMethodGroup;
    @FXML private VBox cardDetailsBox;
    @FXML private VBox cashPaymentBox;
    @FXML private Label cashInsertedLabel;
    @FXML private HBox changeBox;
    @FXML private Label changeLabel;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private Label statusLabel;

    private double totalAmount;
    private double cashInserted = 0.0;
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
            if (newValue == cashRadio) {
                cardDetailsBox.setVisible(false);
                cardDetailsBox.setManaged(false);
                cashPaymentBox.setVisible(true);
                cashPaymentBox.setManaged(true);
                changeBox.setVisible(false);
                changeBox.setManaged(false);
                nfcButton.setVisible(false);
                nfcButton.setManaged(false);
            }

            else if (newValue == nfcRadio) {
                cardDetailsBox.setVisible(false);
                cardDetailsBox.setManaged(false);
                cashPaymentBox.setVisible(false);
                cashPaymentBox.setManaged(false);
                changeBox.setVisible(false);
                changeBox.setManaged(false);
                nfcButton.setVisible(true);
                nfcButton.setManaged(true);

            }
            else {
                cardDetailsBox.setVisible(true);
                cardDetailsBox.setManaged(true);
                cashPaymentBox.setVisible(false);
                cashPaymentBox.setManaged(false);
                changeBox.setVisible(false);
                changeBox.setManaged(false);
                nfcButton.setVisible(false);
                nfcButton.setManaged(false);
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
        this.cashInserted = 0.0;

        if (totalAmountLabel != null) {
            totalAmountLabel.setText(String.format("$%.2f", amount));
        }
        updateCashDisplay();
    }

    /**
     * Handle cash insertion
     */
    @FXML
    public void handleCashInsert(ActionEvent event) {
        Button source = (Button) event.getSource();
        String id = source.getId();

        double amount = 0.0;
        switch (id) {
            case "bill100": amount = 100.0; break;
            case "bill50": amount = 50.0; break;
            case "bill20": amount = 20.0; break;
            case "bill10": amount = 10.0; break;
            case "bill5": amount = 5.0; break;
            case "coin2": amount = 2.0; break;
            case "coin1": amount = 1.0; break;
            case "coin025": amount = 0.25; break;
            case "coin010": amount = 0.10; break;
            case "coin005": amount = 0.05; break;
        }

        cashInserted += amount;
        updateCashDisplay();

        System.out.println("Cash inserted: $" + amount);
        System.out.println("Total cash: $" + cashInserted);
    }

    /**
     * Update cash display
     */
    private void updateCashDisplay() {
        // Add null check to prevent NullPointerException
        if (cashInsertedLabel == null) {
            return;
        }

        cashInsertedLabel.setText(String.format("$%.2f", cashInserted));

        // Check if enough cash is inserted
        if (cashInserted >= totalAmount) {
            double change = cashInserted - totalAmount;
            if (changeLabel != null) {
                changeLabel.setText(String.format("$%.2f", change));
            }
            if (changeBox != null) {
                changeBox.setVisible(true);
                changeBox.setManaged(true);
            }
        } else {
            if (changeBox != null) {
                changeBox.setVisible(false);
                changeBox.setManaged(false);
            }
        }
    }

    /**
     * Validate card input
     */
    private boolean validateCardDetails() {
        String cardNumber = cardNumberField.getText().replaceAll("\\s+", "");
        String expiry = expiryField.getText();
        String cvv = cvvField.getText();

        if (cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            showError("Please fill in all card details");
            return false;
        }

        if (cardNumber.length() < 13 || cardNumber.length() > 19) {
            showError("Invalid card number");
            return false;
        }

        if (!expiry.matches("\\d{2}/\\d{2}")) {
            showError("Invalid expiry date format (MM/YY)");
            return false;
        }

        if (cvv.length() < 3 || cvv.length() > 4) {
            showError("Invalid CVV");
            return false;
        }

        return true;
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        }
    }

    /**
     * Get selected payment method
     */
    private PaymentMethod getSelectedPaymentMethod() {
        if (cashRadio.isSelected()) return PaymentMethod.CASH;
        if (creditCardRadio.isSelected()) return PaymentMethod.CREDIT_CARD;
        if (debitCardRadio.isSelected()) return PaymentMethod.DEBIT_CARD;
        if (nfcRadio.isSelected()) return PaymentMethod.CONTACTLESS;
        return PaymentMethod.CREDIT_CARD;
    }

    @FXML
    public void handleNFCPayment(ActionEvent event) throws IOException {
        PaymentMethod method = getSelectedPaymentMethod();
        String transactionId;
        TransactionStatus status;
        try {
            if (method == PaymentMethod.CONTACTLESS) {
                transactionId = UUID.randomUUID().toString();
                status = TransactionStatus.SUCCESS;
                try {

                } catch (Exception e) {
                    showError("An unexpected error occurred during contactless payment.");
                    status = TransactionStatus.FAILED;
                    e.printStackTrace();
                }
                navigateToConfirmation(event,status, transactionId);
            }
        } catch (Exception e) {
            showError("An unexpected error occurred during contactless payment.");
            e.printStackTrace();
        }
    }

    /**
     * Handle payment processing
     */
    @FXML
    public void handleProcessPayment(ActionEvent event) throws IOException {
        PaymentMethod method = getSelectedPaymentMethod();

        // Validate based on payment method
        if (method == PaymentMethod.CASH) {
            if (cashInserted < totalAmount) {
                showError(String.format("Insufficient cash. Please insert $%.2f more", totalAmount - cashInserted));
                return;
            }
        } else {
            if (!validateCardDetails()) {
                return;
            }
        }

        // Hide error if showing
        if (statusLabel != null) {
            statusLabel.setVisible(false);
        }

        // Process payment
        System.out.println("Processing payment...");
        System.out.println("Method: " + method);
        System.out.println("Amount: $" + totalAmount);

        String transactionId;
        TransactionStatus status;

        if (method == PaymentMethod.CASH) {
            // Cash payment always succeeds
            transactionId = UUID.randomUUID().toString();
            status = TransactionStatus.SUCCESS;

            double change = cashInserted - totalAmount;
            System.out.println("Payment successful with cash!");
            System.out.println("Change returned: $" + String.format("%.2f", change));
        } else {
            // Card payment processing
            String cardNumber = cardNumberField.getText().replaceAll("\\s+", "");
            String token = tokenizationService.tokenize(cardNumber);

            status = paymentGateway.processPayment(totalAmount, method, token);
            transactionId = UUID.randomUUID().toString();
        }


        // Navigate to confirmation
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
            stage.setFullScreenExitHint("");
            Scene scene = new Scene(newScreen);
            stage.setScene(scene);

            stage.setFullScreen(true);

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
        switchScene("Main Screen" ,event, "/fxml/MainScreen.fxml");
    }


}