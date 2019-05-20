/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.user;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Fede
 */
public class HomeController implements Initializable {
    
    @FXML
    private Rectangle rectangleMenu, rectangleGenere, rectangle2D3D, rectangleDays;
    
    @FXML
    private AnchorPane menuWindow, genereWindow, anchorLunedi, anchorMartedi, anchorMercoledi, anchorGiovedi, anchorVenerdi, anchorSabato, anchorDomenica, usernamePane, anchorInfo;
    
    @FXML
    private ScrollPane filmScrollLun;
    
    @FXML
    private Line lineGenere;
    
    private static int count = 0;
    private static int x = 0, y = 60;
    private double castY = 0;
    private HashMap<Label, Rectangle> orariMap = new HashMap<Label, Rectangle>();
    private boolean logStatus = false;
    private final Stage stageRegistrazione = new Stage();
    private final Stage stageLogin = new Stage();
    private final Stage stagePrenotazione = new Stage();
    
    public void setLogStatus(boolean bool){
        logStatus = bool;
    }
    
    public void animationMenu(){
        KeyValue widthValueForward = new KeyValue(rectangleMenu.widthProperty(), rectangleMenu.getWidth() +81);
        KeyValue widthValueBackwards = new KeyValue(rectangleMenu.widthProperty(), rectangleMenu.getWidth() -81);
        KeyValue heightValueForward = new KeyValue(rectangleMenu.heightProperty(), rectangleMenu.getHeight()+244);
        KeyValue heightValueBackwards = new KeyValue(rectangleMenu.heightProperty(), rectangleMenu.getHeight()-244);
        KeyFrame forwardW = new KeyFrame(javafx.util.Duration.seconds(0.3), widthValueForward);
        KeyFrame backwardW = new KeyFrame(javafx.util.Duration.seconds(0.3), widthValueBackwards);
        KeyFrame forwardH = new KeyFrame(javafx.util.Duration.seconds(0.3), heightValueForward);
        KeyFrame backwardH = new KeyFrame(javafx.util.Duration.seconds(0.3), heightValueBackwards);
        Timeline timelineForwardH = new Timeline(forwardH);
        Timeline timelineBackwardH = new Timeline(backwardH);
        Timeline timelineForwardW = new Timeline(forwardW);
        Timeline timelineBackwardW = new Timeline(backwardW);
        FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.seconds(0.4), menuWindow);
        FadeTransition fadeOut = new FadeTransition(javafx.util.Duration.seconds(0.1), menuWindow);
              
        if(!menuWindow.isVisible()){
        menuWindow.setOpacity(0);
        menuWindow.setVisible(true);
        timelineForwardW.play();
        timelineForwardH.play();

        fadeIn.setDelay(javafx.util.Duration.seconds(0.2));
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        } else {
            if(menuWindow.isVisible()){
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0);
                fadeOut.play();
                menuWindow.setVisible(false);
                
                timelineBackwardH.play();
                timelineBackwardW.play();
            }
        }
    }
    
    public void animationGenere(){
        KeyValue heightValueForward = new KeyValue(rectangleGenere.heightProperty(), rectangleGenere.getHeight()+248);
        KeyValue heightValueBackwards = new KeyValue(rectangleGenere.heightProperty(), rectangleGenere.getHeight()-248);
        KeyFrame forwardH = new KeyFrame(javafx.util.Duration.seconds(0.3), heightValueForward);
        KeyFrame backwardH = new KeyFrame(javafx.util.Duration.seconds(0.3), heightValueBackwards);
        Timeline timelineForwardH = new Timeline(forwardH);
        Timeline timelineBackwardH = new Timeline(backwardH);
        FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.seconds(0.4), genereWindow);
        FadeTransition fadeOut = new FadeTransition(javafx.util.Duration.seconds(0.1), genereWindow);
        
        if(!genereWindow.isVisible()){
        genereWindow.setOpacity(0);
        genereWindow.setVisible(true);
        timelineForwardH.play();

        fadeIn.setDelay(javafx.util.Duration.seconds(0.2));
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        } else {
            if(genereWindow.isVisible()){
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0);
                fadeOut.play();
                genereWindow.setVisible(false);
                
                timelineBackwardH.play();
            }
        }
    }
    
    public void loginWindow(){
        if(!stageLogin.isShowing()){
            openLogin();
        }
    }
    
    public void registrationWindow(MouseEvent event){
        Label label = (Label) event.getSource();
        
        KeyValue XValue = new KeyValue(label.scaleXProperty(), 0.85);
        KeyFrame forwardX = new KeyFrame(javafx.util.Duration.seconds(0.125), XValue);
        Timeline timelineX = new Timeline(forwardX);
        timelineX.setAutoReverse(true);
        timelineX.setCycleCount(2);
        timelineX.play();
        KeyValue YValue = new KeyValue(label.scaleYProperty(), 0.85);
        KeyFrame forwardY = new KeyFrame(javafx.util.Duration.seconds(0.125), YValue);
        Timeline timelineY = new Timeline(forwardY);
        timelineY.setAutoReverse(true);
        timelineY.setCycleCount(2);
        timelineY.play();
        
        if(!stageRegistrazione.isShowing()){
            openRegistrazione();
        }
    }
    
    public void hoverOrariEnter(MouseEvent event){
        Rectangle rect = (Rectangle) event.getSource();
        rect.setStroke(Color.YELLOW);
    }
    
    public void hoverOrariExit(MouseEvent event){
        Rectangle rect = (Rectangle) event.getSource();
        rect.setStroke(Color.WHITE);
    }
    
    public void hoverGenereEnter(MouseEvent event){
        Label label = (Label) event.getSource();
        if(genereWindow.isVisible()){
            lineGenere.setVisible(true);
        }
        
        lineGenere.setLayoutY(label.getLayoutY()+32);
        lineGenere.setStartX(-label.getWidth()/2);
        lineGenere.setEndX(label.getWidth()/2);
    }
    
    public void animation2D3D(MouseEvent event){
        TranslateTransition transition = new TranslateTransition(javafx.util.Duration.seconds(0.2), rectangle2D3D);
        Label label = (Label) event.getSource();
        transition.setToX(label.getLayoutX()-rectangle2D3D.getLayoutX()-rectangle2D3D.getWidth()/4);
        transition.play();
    }
    
    
    
    public void setInvisblePane() {
        anchorLunedi.setVisible(false);
        anchorMartedi.setVisible(false);
        anchorMercoledi.setVisible(false);
        anchorGiovedi.setVisible(false);
        anchorVenerdi.setVisible(false);
        anchorSabato.setVisible(false);
        anchorDomenica.setVisible(false);
        anchorInfo.setVisible(false);
    }

    public void infoPaneClick() {
        anchorInfo.setVisible(true);
    }
    
    public void filterByDay(MouseEvent event){
        Label label = (Label) event.getSource();
        String tmp = label.getText();
        setInvisblePane();
        
        TranslateTransition transition = new TranslateTransition(javafx.util.Duration.seconds(0.2), rectangleDays);
        KeyValue width = new KeyValue(rectangleDays.widthProperty(), label.getWidth());
        KeyFrame widthFrame = new KeyFrame(javafx.util.Duration.seconds(0.3), width);
        Timeline timelineWidth = new Timeline(widthFrame);
        transition.setToX(label.getLayoutX());
        if(!rectangleDays.isVisible()){
            rectangleDays.setVisible(true);
            rectangleDays.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.seconds(0.4), rectangleDays);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
        timelineWidth.play();
        transition.play();
        
        switch(tmp){
            case "Lunedi":
                anchorLunedi.setVisible(true);
                break;
    
            case "Martedi":
                anchorMartedi.setVisible(true);
                break;

            case "Mercoledi":
                anchorMercoledi.setVisible(true);
                break;

            case "Giovedi":
                anchorGiovedi.setVisible(true);
                break;

            case "Venerdi":
                anchorVenerdi.setVisible(true);
                break;
   
            case "Sabato":
                anchorSabato.setVisible(true);
                break;

            case "Domenica":
                anchorDomenica.setVisible(true);
                break;

        }
    }
    
    GridPane grigliaFilm = new GridPane();
    
    public void aggiungiFilm(){
        
        grigliaFilm.setPrefSize(filmScrollLun.getWidth(), filmScrollLun.getHeight());
        
        Label titolo = new Label("Avengers");
        titolo.setFont(new Font("New Amsterdam Regular", 34));
        
        Label genere = new Label("Genere: ");
        genere.setFont(new Font("New Amsterdam Regular", 20));
        
        Label regia = new Label("Regia: ");
        regia.setFont(new Font("New Amsterdam Regular", 20));
        
        Label cast = new Label("Cast: ");
        cast.setFont(new Font("New Amsterdam Regular", 20));
        
        File file = new File("resources/images/Avengers.jpg");
        Image image = new Image(file.toURI().toString());
        ImageView view = new ImageView(image);
        view.setFitHeight(image.getHeight()/6.5);
        view.setFitWidth(image.getWidth()/6.5);

        grigliaFilm.setVgap(80);
        
        AnchorPane pane = new AnchorPane();
        
        grigliaFilm.addRow(count++, pane);
        
        filmScrollLun.setContent(grigliaFilm);
        
        pane.getChildren().add(view);
        pane.getChildren().add(titolo);
        pane.getChildren().add(genere);
        pane.getChildren().add(regia);
        pane.getChildren().add(cast);
        
        titolo.setLayoutX(200);
        
        genere.setLayoutX(200);
        genere.setLayoutY(titolo.getLayoutY()+60);
        
        regia.setLayoutX(200);
        regia.setLayoutY(genere.getLayoutY()+40);
        
        cast.setLayoutX(200);
        cast.setLayoutY(regia.getLayoutY()+40);
        
        castY = cast.getLayoutY()+40;
        
        addRectangleOrario(pane, "20:00");
        
        x = 0;
        y = 60;
    }
    
    public void addRectangleOrario(AnchorPane pane, String orario){
        
        Rectangle rect = createRectangle(pane);
        
        rect.setCursor(Cursor.HAND);
        
	rect.setOnMouseEntered(e ->
	{ 
		rect.setStroke(Color.YELLOW);
	});
        
        rect.setOnMouseExited(e ->
	{ 
		rect.setStroke(Color.WHITE);
	});
        
        rect.setOnMouseClicked(e ->
        {
            if(!stagePrenotazione.isShowing()){
                openPrenotazioneFilm();
            }
        });
        
        Label orarioLabel = new Label(orario);
        
        orarioLabel.setCursor(Cursor.HAND);
        
        orarioLabel.setOnMouseClicked(e ->
        {
            if(!stagePrenotazione.isShowing()){
                openPrenotazioneFilm();
            }
        });
        
        orarioLabel.setOnMouseEntered(e ->
	{ 
		rect.setStroke(Color.YELLOW);
	});
        
        orarioLabel.setOnMouseExited(e ->
	{ 
		rect.setStroke(Color.WHITE);
	});
            
            if(rect.getLayoutX() < 800){
                
                pane.getChildren().add(rect);
            
            } else {
                
                x = 0;
                
                int tmpX = 200;
                rect.setLayoutX(tmpX);
                
                castY = castY + y;
                rect.setLayoutY(castY);
                
                pane.getChildren().add(rect);
                
                x = tmpX - 100;
            }
            
            orarioLabel.setLayoutX(rect.getLayoutX()+rect.getWidth()/4);
            orarioLabel.setLayoutY(rect.getLayoutY()+rect.getHeight()/4);
            
            orarioLabel.setFont(new Font("New Amsterdam Regular", 22));
            
            pane.getChildren().add(orarioLabel);
            
            orariMap.put(orarioLabel, rect);
            
            //removeOrario(pane, "20:00");
    }
    
    public void removeOrario(AnchorPane pane, String orario){
        Label labelTmp = new Label();
        
        Rectangle rectangleTmp = new Rectangle();
        
        for (HashMap.Entry<Label, Rectangle> entry : orariMap.entrySet()) {
            if(entry.getKey().getText().equals(orario)){
                labelTmp = entry.getKey();
                rectangleTmp = entry.getValue();
            }
        }
        
        pane.getChildren().remove(labelTmp);
        pane.getChildren().remove(rectangleTmp);
        
        orariMap.remove(labelTmp, rectangleTmp);
    }
    
    public Rectangle createRectangle(AnchorPane pane){
        x += 200;
        
        Rectangle rect = new Rectangle();
        
        rect.setWidth(80);
        rect.setHeight(40);
        rect.setLayoutX(x);
        rect.setLayoutY(castY);
        rect.setStroke(Color.WHITE);
        rect.setStrokeWidth(1);
        
        x -= 100;
        
        return rect;
    }
    
    public void openRegistrazione(){
        try {
            FXMLLoader fxmlLoaderRegistrazione = new FXMLLoader(getClass().getResource("/fxml/user/Registrazione.fxml"));
            Parent root = (Parent) fxmlLoaderRegistrazione.load();
            stageRegistrazione.setScene(new Scene(root));
            stageRegistrazione.setResizable(false);
            stageRegistrazione.setAlwaysOnTop(true);
            stageRegistrazione.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void openLogin(){
        try {
            FXMLLoader fxmlLoaderLogin = new FXMLLoader(getClass().getResource("/fxml/user/Login.fxml"));
            Parent root = (Parent) fxmlLoaderLogin.load();
            stageLogin.setScene(new Scene(root));
            stageLogin.setResizable(false);
            stageLogin.setAlwaysOnTop(true);
            stageLogin.show();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void openPrenotazioneFilm(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/user/PrenotazioneFilm.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            stagePrenotazione.setScene(new Scene(root));
            stagePrenotazione.setResizable(false);
            stagePrenotazione.setMaximized(true);
            stagePrenotazione.show();
            } catch (Exception ex) {
                System.out.println(ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInvisblePane();
        rectangleDays.setVisible(false);
        anchorLunedi.setVisible(true);
        menuWindow.setVisible(false);
        lineGenere.setVisible(false);
        genereWindow.setVisible(false);
        usernamePane.setVisible(false);
    }
    
}
