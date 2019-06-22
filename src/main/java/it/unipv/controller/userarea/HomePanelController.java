package it.unipv.controller.userarea;

import it.unipv.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomePanelController {

    @FXML private Label usernameInjectedLabel;
    @FXML private Label emailInjectedLabel;
    @FXML private Label codeInjectedLabel;

    public void init(User loggedUser) {
        usernameInjectedLabel.setText(loggedUser.getNome());
        emailInjectedLabel.setText(loggedUser.getEmail());
        codeInjectedLabel.setText(loggedUser.getCodice());
    }
    
}
