package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import services.PaymentGateway;
import services.TokenizationService;
import utils.Enums.PaymentMethod;
import utils.Enums.TransactionStatus;

import java.io.IOException;
import java.util.UUID;

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

    // Reference to main container in MainScreenController
    private StackPane mainContainer;

    public PaymentController() {
        this.paymentGateway = new PaymentGateway();
        this.tokenizationService = new TokenizationService();
    }

    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    @FXML
    public void initialize() {
        paymentMethodGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == cashRadio) {
                showCashBox();
            } else if (newVal == nfcRadio) {
                showNFCBox();
            } else {
                showCardBox();
            }
        });
    }

    private void showCashBox() {
        cardDetailsBox.setVisible(false);
        cardDetailsBox.setManaged(false);
        cashPaymentBox.setVisible(true);
        cashPaymentBox.setManaged(true);
        changeBox.setVisible(false);
        changeBox.setManaged(false);
        nfcButton.setVisible(false);
        nfcButton.setManaged(false);
    }

    private void showNFCBox() {
        cardDetailsBox.setVisible(false);
        cardDetailsBox.setManaged(false);
        cashPaymentBox.setVisible(false);
        cashPaymentBox.setManaged(false);
        changeBox.setVisible(false);
        changeBox.setManaged(false);
        nfcButton.setVisible(true);
        nfcButton.setManaged(true);
    }

    private void showCardBox() {
        cardDetailsBox.setVisible(true);
        cardDetailsBox.setManaged(true);
        cashPaymentBox.setVisible(false);
        cashPaymentBox.setManaged(false);
        changeBox.setVisible(false);
        changeBox.setManaged(false);
        nfcButton.setVisible(false);
        nfcButton.setManaged(false);
    }

    public void initializePayment(double amount, String ticketType, String zone, int quantity) {
        this.totalAmount = amount;
        this.ticketType = ticketType;
        this.zone = zone;
        this.quantity = quantity;
        this.cashInserted = 0.0;

        if (totalAmountLabel != null) totalAmountLabel.setText(String.format("$%.2f", amount));
        updateCashDisplay();
    }

    @FXML
    public void handleCashInsert(ActionEvent event) {
        Button source = (Button) event.getSource();
        double amount = switch (source.getId()) {
            case "bill100" -> 100.0;
            case "bill50" -> 50.0;
            case "bill20" -> 20.0;
            case "bill10" -> 10.0;
            case "bill5" -> 5.0;
            case "coin2" -> 2.0;
            case "coin1" -> 1.0;
            case "coin025" -> 0.25;
            case "coin010" -> 0.10;
            case "coin005" -> 0.05;
            default -> 0.0;
        };
        cashInserted += amount;
        updateCashDisplay();
    }

    private void updateCashDisplay() {
        if (cashInsertedLabel == null) return;
        cashInsertedLabel.setText(String.format("$%.2f", cashInserted));

        if (cashInserted >= totalAmount) {
            double change = cashInserted - totalAmount;
            if (changeLabel != null) changeLabel.setText(String.format("$%.2f", change));
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

    private void showError(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        }
    }

    private PaymentMethod getSelectedPaymentMethod() {
        if (cashRadio.isSelected()) return PaymentMethod.CASH;
        if (creditCardRadio.isSelected()) return PaymentMethod.CREDIT_CARD;
        if (debitCardRadio.isSelected()) return PaymentMethod.DEBIT_CARD;
        if (nfcRadio.isSelected()) return PaymentMethod.CONTACTLESS;
        return PaymentMethod.CREDIT_CARD;
    }

    @FXML
    public void handleNFCPayment(ActionEvent event) {
        processPayment(PaymentMethod.CONTACTLESS);
    }

    @FXML
    public void handleProcessPayment(ActionEvent event) {
        PaymentMethod method = getSelectedPaymentMethod();
        processPayment(method);
    }

    private void processPayment(PaymentMethod method) {
        if (method == PaymentMethod.CASH && cashInserted < totalAmount) {
            showError(String.format("Insufficient cash. Please insert $%.2f more", totalAmount - cashInserted));
            return;
        } else if (method != PaymentMethod.CASH && !validateCardDetails()) {
            return;
        }

        if (statusLabel != null) statusLabel.setVisible(false);

        String transactionId = UUID.randomUUID().toString();
        TransactionStatus status;

        if (method == PaymentMethod.CASH) {
            status = TransactionStatus.SUCCESS;
        } else {
            String cardNumber = cardNumberField.getText().replaceAll("\\s+", "");
            String token = tokenizationService.tokenize(cardNumber);
            status = paymentGateway.processPayment(totalAmount, method, token);
        }

        navigateToConfirmation(status, transactionId);
    }

    private void navigateToConfirmation(TransactionStatus status, String transactionId) {
        if (mainContainer == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentConfirmationScreen.fxml"));
            Parent confirmationScreen = loader.load();

            PaymentConfirmationController controller = loader.getController();
            controller.initializeConfirmation(status, transactionId, totalAmount, ticketType, zone, quantity);
            controller.setMainContainer(mainContainer);

            mainContainer.getChildren().setAll(confirmationScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        if (mainContainer != null) {
            NavigationUtil.setContent(mainContainer, "/fxml/MainScreen.fxml");
        }
    }
}
