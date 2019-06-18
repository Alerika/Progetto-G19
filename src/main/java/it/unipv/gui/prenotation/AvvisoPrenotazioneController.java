package it.unipv.gui.prenotation;

import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.home.HomeController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AvvisoPrenotazioneController {

    @FXML private Label areaRiservataButton;
    private HomeController homeController;

    public void init(it.unipv.gui.home.HomeController homeController) {
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
