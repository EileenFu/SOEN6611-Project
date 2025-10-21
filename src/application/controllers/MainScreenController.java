package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import static application.controllers.NavigationUtil.switchScene;
import java.io.IOException;

public class MainScreenController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void handleBuySingleTicket(ActionEvent event) {
        navigateToZoneType(event, "BUY_SINGLE_TICKET");
    }

    @FXML
    public void handleBuyMultipleTickets(ActionEvent event) {
        navigateToZoneType(event, "BUY_MULTIPLE_TICKETS");
    }

    @FXML
    public void handleRechargeOpusCard(ActionEvent event) {switchScene("Recharge Opus Card",event, "/fxml/OpusCardScreen.fxml");}

    @FXML
    public void handleBuyUnlimitedPass(ActionEvent event) {
        switchScene("Buy Unlimited Pass", event, "/fxml/UnlimitedPassScreen.fxml");;
    }

    /**
     * Navigate to Zone Type selection screen with action context
     */
    private void navigateToZoneType(ActionEvent event, String action) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ZoneTypeScreen.fxml"));
            Parent zoneTypeScreen = loader.load();

            // Get the controller and pass the action
            ZoneTypeController controller = loader.getController();
            controller.setPreviousAction(action);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(zoneTypeScreen);
            stage.centerOnScreen();
            stage.setResizable(true);
            stage.setTitle("Select Zone Type");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}