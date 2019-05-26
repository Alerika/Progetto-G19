package it.unipv.gui.user.areariservata;

import java.net.URL;
import java.util.ResourceBundle;

import it.unipv.gui.login.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class HomePanelController implements Initializable {

    @Override public void initialize(URL url, ResourceBundle rb) { }

    @FXML private Label usernameInjectedLabel;
    @FXML private Label emailInjectedLabel;

    public void init(User loggedUser) {
        usernameInjectedLabel.setText(loggedUser.getName());
        emailInjectedLabel.setText(loggedUser.getEmail());
    }
    
}
