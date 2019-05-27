package it.unipv.gui.user;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.login.User;
import it.unipv.gui.user.areariservata.AreaRiservataHomeController;
import it.unipv.utils.ApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AvvisoPrenotazioneController implements Initializable {

    @FXML private Label areaRiservataButton;
    private User loggedUser;

    @Override public void initialize(URL url, ResourceBundle rb) { }

    public void init(User loggedUser) {
        this.loggedUser = loggedUser;
        GUIUtils.setScaleTransitionOnControl(areaRiservataButton);
    }

    private void doClose() {
        Stage stage = (Stage) areaRiservataButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openReservedAreaListener() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/areariservata/AreaRiservataHome.fxml"));
            Parent p = loader.load();
            AreaRiservataHomeController arhc = loader.getController();
            arhc.init(loggedUser);
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.setMinHeight(850);
            stage.setMinWidth(1200);
            stage.setTitle("Area riservata di " + loggedUser.getName());
            stage.show();
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        }
        doClose();
    }
    
}
