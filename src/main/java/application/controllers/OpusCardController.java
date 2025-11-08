package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.OPUSCard;
import services.FareService;

import java.io.IOException;

import static application.controllers.NavigationUtil.switchScene;

public class OpusCardController {

    @FXML
    private TextField cardIdField;

    @FXML
    private RadioButton regularCardRadio;

    @FXML
    private Label errorLabel;

    private OPUSCard opusCard;
    private FareService fareService = new FareService();

    /**
     * Validates the card input and creates an OPUS card instance
     */
    private boolean validateCardInput() {
        String cardId = cardIdField.getText().trim();

        // Clear previous error
        errorLabel.setVisible(false);

        // Validate card number
        if (cardId.isEmpty()) {
            showError("Please enter a card number");
            return false;
        }

        if (!cardId.matches("\\d{16}")) {
            showError("Card number must be exactly 16 digits");
            return false;
        }

        // Create OPUS card based on selection
        OPUSCard.CardType cardType = regularCardRadio.isSelected()
                ? OPUSCard.CardType.REGULAR
                : OPUSCard.CardType.REDUCED_FARE;

        opusCard = new OPUSCard(cardType, fareService);

        return true;
    }

    /**
     * Display error message
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Handle Add Ticket button click
     */
    @FXML
    public void handleAddTicket(ActionEvent event) {
        if (!validateCardInput()) {
            return;
        }

        navigateToZoneSelection(event, "ADD_TICKET_TO_OPUS");
    }

    /**
     * Handle Add Pass button click
     */
    @FXML
    public void handleAddPass(ActionEvent event) {
        if (!validateCardInput()) {
            return;
        }

        navigateToPassSelection(event);
    }

    /**
     * Navigate to zone selection for adding tickets
     */
    private void navigateToZoneSelection(ActionEvent event, String action) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ZoneTypeScreen.fxml"));
            Parent newScreen = loader.load();

            ZoneTypeController controller = loader.getController();
            controller.setPreviousAction(action);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setFullScreenExitHint("");
            stage.setResizable(true);
            stage.setFullScreen(true);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Zone Type Screen");
            e.printStackTrace();
            showError("Failed to load zone selection screen");
        }
    }

    /**
     * Navigate to pass selection screen (you'll need to create this screen)
     */
    private void navigateToPassSelection(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UnlimitedPassScreen.fxml"));
            Parent newScreen = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Pass Selection Screen");
            e.printStackTrace();
            showError("Pass selection screen not yet implemented");
        }
    }

    /**
     * Handle Back button click
     */
    @FXML
    public void handleBack(ActionEvent event) {
        switchScene("", event, "/fxml/MainScreen.fxml");
    }

}