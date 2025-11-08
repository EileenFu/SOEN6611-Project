package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class StartupController {

    @FXML
    private ImageView loadingImage;

    public void startScreen(URL location, ResourceBundle resources) {
        Image image = new Image(getClass().getResourceAsStream("/Images/iGo_Loading_img.png"));
        loadingImage.setImage(image);
    }
}
