/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.user;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import it.unipv.conversion.CSVToMovieList;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.common.Movie;
import it.unipv.gui.common.MovieStatusTYPE;
import it.unipv.gui.common.MovieTYPE;
import it.unipv.gui.login.User;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;
import it.unipv.utils.DataReferences;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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

/**
 *
 * @author Fede
 */
public class HomeController implements Initializable {
    
    @FXML
    private Rectangle rectangleMenu, rectangleGenere, rectangle2D3D;
    
    @FXML
    private AnchorPane menuWindow, genereWindow, usernamePane, anchorInfo, homePane, singleFilmPane;

    @FXML
    private GridPane filmGrid = new GridPane();

    @FXML
    private GridPane filmGridFiltered = new GridPane();

    @FXML
    private ScrollPane filmScroll, filmScrollFiltered;

    @FXML
    private Label labelIVA, labelCellulari, labelCosti, infoUtili;
    
    @FXML
    private Line lineGenere;

    private static int x = 0, y = 60;
    private double castY = 0;
    private HashMap<Label, Rectangle> orariMap = new HashMap<Label, Rectangle>();
    private final Stage stageRegistrazione = new Stage();
    private final Stage stageLogin = new Stage();
    private final Stage stagePrenotazione = new Stage();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static int rowCount = 0;
    private static int columnCount = 0;
    private static int columnCountMax = 0;
    private List<Movie> film = new ArrayList<>();
    
    public void initGrid(){
        film = CSVToMovieList.getMovieListFromCSV(DataReferences.MOVIEFILEPATH);
        Collections.sort(film);

        filmGrid.setHgap(80);
        filmGrid.setVgap(80);

        filmGridFiltered.setHgap(80);
        filmGridFiltered.setVgap(80);

        if (lineGenere.getScene().getWindow().getWidth() > 1360) {
            columnCountMax = 5;
            filmGrid.setHgap(150);
            filmGridFiltered.setHgap(150);
        } else {
            columnCountMax = 3;
        }

        for (Movie movie : film) {
            if(movie.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                addMovie(movie, filmGrid, filmScroll);
            }
        }

        initRowAndColumnCount();
    }

    private void initRowAndColumnCount() {
        rowCount = 0;
        columnCount = 0;
    }

    public void addMovie(Movie movie, GridPane grid, ScrollPane scroll){
        try {
            FileInputStream fis = new FileInputStream(movie.getLocandinaPath());
            ImageView posterPreview = new ImageView(new Image(fis, 1000, 0, true, true));
            posterPreview.setPreserveRatio(true);
            posterPreview.setFitWidth(200);
            CloseableUtils.close(fis);

            AnchorPane anchor = new AnchorPane();

            if(columnCount == columnCountMax){
                columnCount = 0;
                rowCount++;
            }

            grid.add(anchor, columnCount, rowCount);
            columnCount++;

            scroll.setContent(grid);
            GridPane.setMargin(anchor, new Insets(15,0,5,15));

            anchor.getChildren().addAll(posterPreview);
            posterPreview.setLayoutX(30);
            if(rowCount==0) {
                posterPreview.setLayoutY(20);
            }

            posterPreview.setOnMouseClicked(e ->
            {
                homePane.setVisible(false);

                singleFilmPane.getChildren().clear();

                try {
                    FileInputStream fis2 = new FileInputStream(movie.getLocandinaPath());
                    ImageView poster = new ImageView(new Image(fis2, 1000, 0, true, true));
                    poster.setPreserveRatio(true);
                    CloseableUtils.close(fis2);

                    poster.setFitWidth(350);

                    singleFilmPane.getChildren().add(poster);

                    singleFilmPane.setVisible(true);
                } catch (FileNotFoundException exce){
                    throw new ApplicationException(exce);
                }
            });

            GUIUtils.setScaleTransitionOnControl(posterPreview);

        } catch(FileNotFoundException ex) {
            throw new ApplicationException(ex);
        }
    }
    
    public void animationMenu(){
        KeyValue widthValueForward = new KeyValue(rectangleMenu.widthProperty(), rectangleMenu.getWidth() +81);
        KeyValue widthValueBackwards = new KeyValue(rectangleMenu.widthProperty(), rectangleMenu.getWidth() -81);
        KeyValue heightValueForward = new KeyValue(rectangleMenu.heightProperty(), rectangleMenu.getHeight()+244);
        KeyValue heightValueBackwards = new KeyValue(rectangleMenu.heightProperty(), rectangleMenu.getHeight()-244);
        KeyFrame forwardW = new KeyFrame(javafx.util.Duration.seconds(0.3), widthValueForward);
        KeyFrame backwardW = new KeyFrame(javafx.util.Duration.seconds(0.15), widthValueBackwards);
        KeyFrame forwardH = new KeyFrame(javafx.util.Duration.seconds(0.3), heightValueForward);
        KeyFrame backwardH = new KeyFrame(javafx.util.Duration.seconds(0.15), heightValueBackwards);
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
        rectangle2D3D.setVisible(true);
        TranslateTransition transition = new TranslateTransition(javafx.util.Duration.seconds(0.2), rectangle2D3D);
        Label label = (Label) event.getSource();
        transition.setToX(label.getLayoutX()-rectangle2D3D.getLayoutX()-rectangle2D3D.getWidth()/4);
        transition.play();

        switch(label.getText()) {
            case "2D":
                filterMoviesByMovieTYPE(MovieTYPE.TWOD);
                break;
            case "3D":
                filterMoviesByMovieTYPE(MovieTYPE.THREED);
                break;
        }
    }

    private void filterMoviesByMovieTYPE(MovieTYPE type) {
        if(type!=null) {
            List<Movie> filteredMovies = new ArrayList<>();
            for(Movie m : film) {
                if(m.getTipo().equals(type)){
                    if(m.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                        filteredMovies.add(m);
                    }
                }
            }
            filmGridFiltered.getChildren().clear();
            for(Movie m : filteredMovies) {
                addMovie(m, filmGridFiltered, filmScrollFiltered);
            }
            initRowAndColumnCount();
        } else {
            refreshUI();
        }
        filmScroll.setVisible(false);
        filmScrollFiltered.setVisible(true);
    }

    private void refreshUI() {
        filmGrid.getChildren().clear();
        initGrid();
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
        } catch (IOException e) {
            throw new ApplicationException(e);
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
            
        } catch (IOException e) {
            throw new ApplicationException(e);
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
            } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    public void infoPaneClick() {
        homePane.setVisible(false);
        animationMenu();

        Stage stage = (Stage) homePane.getScene().getWindow();

        infoUtili.setLayoutX(stage.getWidth()/2-infoUtili.getWidth()/2);

        labelCellulari.setLayoutX(stage.getWidth()/2-labelCellulari.getWidth()/2);
        labelIVA.setLayoutX(stage.getWidth()/2-labelIVA.getWidth()/2);
        labelCosti.setLayoutX(stage.getWidth()/2-labelCosti.getWidth()/2);

        anchorInfo.setVisible(true);

    }

    public void homeClick(){
        anchorInfo.setVisible(false);
        singleFilmPane.setVisible(false);
        rectangle2D3D.setVisible(false);
        animationMenu();
        if(film.isEmpty()){
            initGrid();
        } else {
            homePane.setVisible(true);
            filmScroll.setVisible(true);
            filmScrollFiltered.setVisible(false);
        }
    }

    public void listaSaleClick(){
        anchorInfo.setVisible(false);
        homePane.setVisible(false);
        animationMenu();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        dtf.format(LocalDateTime.now());

        rectangle2D3D.setVisible(false);
        anchorInfo.setVisible(false);
        menuWindow.setVisible(false);
        lineGenere.setVisible(false);
        genereWindow.setVisible(false);
        usernamePane.setVisible(false);
        singleFilmPane.setVisible(false);

    }
    
}
