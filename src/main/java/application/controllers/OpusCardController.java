package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import model.OPUSCard;
import services.FareService;

import java.io.IOException;

public class OpusCardController {

    @FXML
    private TextField cardIdField;

    @FXML
    private RadioButton regularCardRadio;

    @FXML
    private RadioButton reducedFareCardRadio;

    @FXML
    private Label errorLabel;

    private OPUSCard opusCard;
    private FareService fareService = new FareService();

    // Reference to MainScreen's dynamic content container
    private StackPane mainContainer;

    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    private boolean validateCardInput() {
        String cardId = cardIdField.getText().trim();
        errorLabel.setVisible(false);

        if (cardId.isEmpty()) {
            showError("Please enter a card number");
            return false;
        }

        if (!cardId.matches("\\d{16}")) {
            showError("Card number must be exactly 16 digits");
            return false;
        }

        OPUSCard.CardType cardType = regularCardRadio.isSelected()
                ? OPUSCard.CardType.REGULAR
                : OPUSCard.CardType.REDUCED_FARE;

        opusCard = new OPUSCard(cardType, fareService);
        return true;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    public void handleAddTicket() {
        if (!validateCardInput() || mainContainer == null) return;

        NavigationUtil.setContent(mainContainer, "/fxml/ZoneTypeScreen.fxml");
        ZoneTypeController controller = (ZoneTypeController) NavigationUtil.loadFXMLController("/fxml/ZoneTypeScreen.fxml");
        if (controller != null) {
            controller.setPreviousAction("ADD_TICKET_TO_OPUS");
            controller.setMainContainer(mainContainer);
        }
    }

    @FXML
    public void handleAddPass() {
        if (!validateCardInput() || mainContainer == null) return;

        NavigationUtil.setContent(mainContainer, "/fxml/UnlimitedPassScreen.fxml");
        // Optionally, get controller and pass mainContainer if needed
    }

    @FXML
    public void handleBack() {
        if (mainContainer != null) {
            NavigationUtil.setContent(mainContainer, "/fxml/MainScreen.fxml");
        }
    }
}
