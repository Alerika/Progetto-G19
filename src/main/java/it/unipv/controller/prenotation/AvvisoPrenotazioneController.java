package it.unipv.controller.prenotation;

import it.unipv.controller.common.GUIUtils;
import it.unipv.controller.common.IHomeTrigger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AvvisoPrenotazioneController {

    @FXML private Label areaRiservataButton;
    private IHomeTrigger homeController;

    public void init(IHomeTrigger homeController) {
        this.homeController = homeController;
        GUIUtils.setScaleTransitionOnControl(areaRiservataButton);
    }

    private void doClose() {
        Stage stage = (Stage) areaRiservataButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openReservedAreaListener() {
        homeController.triggerOpenReservedArea();
        doClose();
    }
    
}
