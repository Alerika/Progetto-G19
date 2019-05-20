/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.user;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Fede
 */
public class PrenotazioneFilmController implements Initializable {

    /**
     * Initializes the controller class.
     * 
     */
    
    @FXML
    private ImageView logo;
    
    @FXML
    private Label annulla, confermaPrenotazione;
    
    @FXML
    private Rectangle annullaRectangle, confermaPrenotazioneRectangle;
    
    public void prenotazionePopUp(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/user/AvvisoPrenotazione.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
        closeStage();
    }
    
    public void closeStage(){
        Stage stage = (Stage) annulla.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        logo.setLayoutX(primaryScreenBounds.getWidth()/2-logo.getFitWidth()/2);
        
        annullaRectangle.setLayoutX(primaryScreenBounds.getWidth()/2-annullaRectangle.getWidth()-100);
        annulla.setLayoutX(annullaRectangle.getLayoutX()+annullaRectangle.getWidth()/2-33);
        confermaPrenotazioneRectangle.setLayoutX(primaryScreenBounds.getWidth()/2+confermaPrenotazioneRectangle.getWidth()-100);
        confermaPrenotazione.setLayoutX(confermaPrenotazioneRectangle.getLayoutX()+confermaPrenotazioneRectangle.getWidth()/2-95);
    }    
    
}
