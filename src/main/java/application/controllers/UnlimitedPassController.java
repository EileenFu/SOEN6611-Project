package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import services.FareService;
import utils.Enums.PassDuration;

public class UnlimitedPassController {

    @FXML private ToggleGroup passDurationGroup;

    @FXML private RadioButton dayPassRadio;
    @FXML private RadioButton threeDayPassRadio;
    @FXML private RadioButton weekPassRadio;
    @FXML private RadioButton monthPassRadio;
    @FXML private RadioButton nightPassRadio;
    @FXML private RadioButton weekendPassRadio;
    @FXML private RadioButton fourMonthPassRadio;

    private FareService fareService = new FareService();
    private StackPane mainContainer;

    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    @FXML
    private void handleContinue(ActionEvent event) {
        if (mainContainer == null) return;

        PassDuration duration = getSelectedDuration();
        NavigationUtil.setContent(mainContainer, "/fxml/ZoneTypeScreen.fxml", controller -> {
            if (controller instanceof ZoneTypeController ztc) {
                ztc.setPreviousAction("BUY_UNLIMITED_PASS");
                ztc.setPassDuration(duration);
                ztc.setMainContainer(mainContainer);
            }
        });
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        if (mainContainer == null) return;
        NavigationUtil.setContent(mainContainer, "/fxml/MainScreen.fxml");
    }

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
}
