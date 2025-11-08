package application.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static application.controllers.NavigationUtil.navigateToZoneType;
import static application.controllers.NavigationUtil.switchScene;
import java.io.IOException;

public class MainScreenController {


    @FXML
    public void handleBuySingleTicket(ActionEvent event) {
        navigateToZoneType(event, "BUY_SINGLE_TICKET");
    }

    @FXML
    public void handleBuyMultipleTickets(ActionEvent event) {
        navigateToZoneType(event, "BUY_MULTIPLE_TICKETS");
    }

    @FXML
    public void handleRechargeOpusCard(ActionEvent event) {
        switchScene("Recharge Opus Card", event, "/fxml/OpusCardScreen.fxml");
    }

    @FXML
    public void handleBuyUnlimitedPass(ActionEvent event) {
        switchScene("Buy Unlimited Pass", event, "/fxml/UnlimitedPassScreen.fxml");
    }

}