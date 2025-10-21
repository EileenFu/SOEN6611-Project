
package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.FareService;
import utils.Enums.PassDuration;

import java.io.IOException;

public class UnlimitedPassController {

    @FXML private ToggleGroup passDurationGroup;

    @FXML private RadioButton dayPassRadio;
    @FXML private RadioButton threeDayPassRadio;
    @FXML private RadioButton weekPassRadio;
    @FXML private RadioButton monthPassRadio;
    @FXML private RadioButton nightPassRadio;
    @FXML private RadioButton weekendPassRadio;
    @FXML private RadioButton fourMonthPassRadio;

    @FXML private VBox weekPassBox;
    @FXML private VBox nightPassBox;
    @FXML private VBox weekendPassBox;
    @FXML private VBox fourMonthPassBox;

    private FareService fareService = new FareService();

    @FXML
    public void initialize() {
        // Show all pass options - all zones are now available for all passes
        weekPassBox.setVisible(true);
        weekPassBox.setManaged(true);
        nightPassBox.setVisible(true);
        nightPassBox.setManaged(true);
        weekendPassBox.setVisible(true);
        weekendPassBox.setManaged(true);
        fourMonthPassBox.setVisible(true);
        fourMonthPassBox.setManaged(true);
    }

    /**
     * Get the selected pass duration
     */
    private PassDuration getSelectedDuration() {
        if (dayPassRadio.isSelected()) return PassDuration.DAY;
        if (threeDayPassRadio.isSelected()) return PassDuration.THREEDAY;
        if (weekPassRadio.isSelected()) return PassDuration.WEEK;
        if (monthPassRadio.isSelected()) return PassDuration.MONTH;
        if (nightPassRadio.isSelected()) return PassDuration.NIGHT;
        if (weekendPassRadio.isSelected()) return PassDuration.WEEKEND;
        if (fourMonthPassRadio.isSelected()) return PassDuration.FOURMONTH;
        return PassDuration.DAY;
    }

    /**
     * Handle Continue button - navigate to zone selection
     */
    @FXML
    public void handleContinue(ActionEvent event) {
        PassDuration selectedDuration = getSelectedDuration();
        navigateToZoneSelection(event, selectedDuration);
    }

    /**
     * Navigate to zone selection screen
     */
    private void navigateToZoneSelection(ActionEvent event, PassDuration duration) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ZoneTypeScreen.fxml"));
            Parent newScreen = loader.load();

            ZoneTypeController controller = loader.getController();
            controller.setPreviousAction("BUY_UNLIMITED_PASS");
            controller.setPassDuration(duration);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newScreen);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Zone Type Screen");
            e.printStackTrace();
        }
    }

    /**
     * Handle Cancel/Back button
     */
    @FXML
    public void handleCancel(ActionEvent event) {
        NavigationUtil.switchScene("Main Screen",event, "/fxml/MainScreen.fxml");
    }
}