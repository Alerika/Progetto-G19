/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.user;

import java.net.URL;
import java.util.ResourceBundle;

import it.unipv.main.UserHome;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author Fede
 */
public class AreaRiservataController extends UserHome implements Initializable{

    public AnchorPane redRectangle;

    @FXML
    private Rectangle rectangleMenu;
    
    @FXML
    private AnchorPane homePane, prenotazioniPane, filmVistiPane, suggerimentiPane;
    
    @FXML
    private Label home, prenotazioni, filmVisti, suggerimenti, logout;
    
    public void rectangleAnimation(MouseEvent event){
        TranslateTransition transition = new TranslateTransition(javafx.util.Duration.seconds(0.2), rectangleMenu);
        Label label = (Label) event.getSource();
        transition.setToY(label.getLayoutY()-69-rectangleMenu.getHeight()/4);
        transition.play();
        navigationMenu(event);
    }
    
    public void setInvisblePane() {
        homePane.setVisible(false);
        prenotazioniPane.setVisible(false);
        filmVistiPane.setVisible(false);
        suggerimentiPane.setVisible(false);
    }
    
    public void setBlackLabel(){
        home.setTextFill(Color.BLACK);
        prenotazioni.setTextFill(Color.BLACK);
        filmVisti.setTextFill(Color.BLACK);
        suggerimenti.setTextFill(Color.BLACK);
    }
    
    public void navigationMenu(MouseEvent event){
        setBlackLabel();
        Label label = (Label) event.getSource();
        //si può implementare un delay per quando cambia colore l'etichetta
        label.setTextFill(Color.WHITE);
        String tmp = label.getText();
        setInvisblePane();
        switch(tmp){
            case "HOME":
                homePane.setVisible(true);
                break;
    
            case "PRENOTAZIONI":
                prenotazioniPane.setVisible(true);
                break;

            case "FILM VISTI":
                filmVistiPane.setVisible(true);
                break;

            case "SUGGERIMENTI":
                suggerimentiPane.setVisible(true);
                break;

        }
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //topLine.setEndX(super.getStageWidth());
        
        setInvisblePane();
        setBlackLabel();
        
        homePane.setVisible(true);
        logout.setTextFill(Color.BLACK);
        home.setTextFill(Color.WHITE);
    }    
    
}
