package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import services.FareService;
import utils.Enums;

import java.io.IOException;

import static utils.Enums.ZoneType.*;

public class ZoneTypeController {

    private FareService fareService = new FareService();
    private String previousAction;
    private Enums.PassDuration selectedPassDuration;

    // Reference to the container in MainScreen.fxml where dynamic content will be loaded
    private StackPane mainContainer;

    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void setPreviousAction(String action) {
        this.previousAction = action;
        System.out.println("Previous action set to: " + action);
    }

    public void setPassDuration(Enums.PassDuration duration) {
        this.selectedPassDuration = duration;
        System.out.println("Pass duration set to: " + duration);
    }

    private boolean isValidState() {
        if (previousAction == null) {
            System.err.println("Error: previousAction is not set!");
            return false;
        }
        return true;
    }

    @FXML
    public void handleZoneA() {
        handleZoneSelection(A, 1);
    }

    @FXML
    public void handleZoneAB() {
        handleZoneSelection(AB, 1);
    }

    @FXML
    public void handleZoneABC() {
        handleZoneSelection(ABC, 1);
    }

    @FXML
    public void handleZoneABCD() {
        handleZoneSelection(ABCD, 1);
    }

    private void handleZoneSelection(Enums.ZoneType zone, int quantity) {
        if (!isValidState() || mainContainer == null) return;

        try {
            switch (previousAction) {
                case "BUY_SINGLE_TICKET" -> {
                    double price = fareService.getTicketFare(zone, quantity, false);
                    System.out.println("Price: " + price);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TicketSummaryScreen.fxml"));
                    mainContainer.getChildren().clear();
                    mainContainer.getChildren().add(loader.load());

                    TicketSummaryController controller = loader.getController();
                    controller.initializeSummary(previousAction, zone.name(), quantity, price);
                    controller.setMainContainer(mainContainer);
                }
                case "BUY_MULTIPLE_TICKETS" -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MultipleTicketsScreen.fxml"));
                    mainContainer.getChildren().clear();
                    mainContainer.getChildren().add(loader.load());

                    MultipleTicketsController controller = loader.getController();
                    controller.setSelectedZone(zone.name());
                    controller.setMainContainer(mainContainer);
                }
                case "BUY_UNLIMITED_PASS" -> {
                    if (selectedPassDuration == null) {
                        System.err.println("Pass duration not set, cannot continue");
                        return;
                    }
                    double price = fareService.getPassFare(zone, selectedPassDuration, false);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TicketSummaryScreen.fxml"));
                    mainContainer.getChildren().clear();
                    mainContainer.getChildren().add(loader.load());

                    TicketSummaryController controller = loader.getController();
                    controller.initializePassSummary(zone.name(), selectedPassDuration.name(), price);
                    controller.setMainContainer(mainContainer);
                }
                case "RECHARGE_OPUS_CARD" -> {
                    NavigationUtil.setContent(mainContainer, "/fxml/OpusCardScreen.fxml");
                }
                default -> System.err.println("Unknown action: " + previousAction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        if (mainContainer != null) {
            NavigationUtil.setContent(mainContainer, "/fxml/MainScreen.fxml");
        }
    }
}
