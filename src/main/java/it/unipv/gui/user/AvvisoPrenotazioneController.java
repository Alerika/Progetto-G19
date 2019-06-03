package it.unipv.gui.user;

import java.net.URL;
import java.util.ResourceBundle;

import it.unipv.gui.common.GUIUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AvvisoPrenotazioneController implements Initializable {

    @FXML private Label areaRiservataButton;
    private HomeController homeController;

    @Override public void initialize(URL url, ResourceBundle rb) { }

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
