package it.unipv.controller.home;

import it.unipv.db.DBConnection;
import it.unipv.controller.login.RegistrazioneController;
import it.unipv.model.User;
import it.unipv.utils.ApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomePanelController {

    @FXML AnchorPane welcomeFooter;
    @FXML Label welcomeLabel, registerLabel;
    private Stage stageRegistrazione;

    public void init(User loggedUser, DBConnection dbConnection, Stage stageRegistrazione) {
        this.stageRegistrazione = stageRegistrazione;
        if(loggedUser==null) {
            registerLabel.setOnMouseExited(event -> registerLabel.setTextFill(Color.WHITE));
            registerLabel.setOnMouseEntered(event -> registerLabel.setTextFill(Color.valueOf("db8f00")));
            registerLabel.setOnMouseClicked(event -> openRegisterPage(dbConnection));
        } else {
            welcomeFooter.setVisible(false);
            welcomeLabel.setText(loggedUser.getNome() + ", bentornato in Golden Movie Studio!");
        }
    }

    private void openRegisterPage(DBConnection dbConnection) {
        if(!stageRegistrazione.isShowing()) {
            doOpenRegisterPage(dbConnection);
        }
    }

    private void doOpenRegisterPage(DBConnection dbConnection) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/Registrazione.fxml"));
            Parent p = loader.load();
            RegistrazioneController rc = loader.getController();
            rc.init(dbConnection);
            stageRegistrazione.setScene(new Scene(p));
            stageRegistrazione.setResizable(false);
            stageRegistrazione.setTitle("Registrazione");
            stageRegistrazione.show();
            stageRegistrazione.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }
}
