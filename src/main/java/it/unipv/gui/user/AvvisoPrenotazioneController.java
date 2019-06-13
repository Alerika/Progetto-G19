package it.unipv.gui.user;

import it.unipv.gui.common.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AvvisoPrenotazioneController {

    @FXML private Label areaRiservataButton;
    private HomeController homeController;

    public void init(HomeController homeController) {
        this.homeController = homeController;
        GUIUtils.setScaleTransitionOnControl(areaRiservataButton);
    }

    private void doClose() {
        Stage stage = (Stage) areaRiservataButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openReservedAreaListener() {
        homeController.doOpenReservedArea();
        doClose();
    }
    
}
