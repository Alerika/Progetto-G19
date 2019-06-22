package it.unipv.controller.prenotation;

import it.unipv.controller.common.GUIUtils;
import it.unipv.controller.home.HomeController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AvvisoPrenotazioneController {

    @FXML private Label areaRiservataButton;
    private HomeController homeController;

    public void init(it.unipv.controller.home.HomeController homeController) {
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
