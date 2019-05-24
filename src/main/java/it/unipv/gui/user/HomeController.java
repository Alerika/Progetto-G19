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
import it.unipv.gui.login.LoginController;
import it.unipv.gui.login.User;
import it.unipv.gui.login.UserInfo;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class HomeController implements Initializable {
    
    @FXML private Rectangle rectangleMenu, rectangleGenere, rectangle2D3D;
    @FXML private AnchorPane menuWindow, genereWindow, usernamePane, anchorInfo, homePane, singleFilmPane;
    @FXML private GridPane filmGrid = new GridPane();
    @FXML private GridPane filmGridFiltered = new GridPane();
    @FXML private ScrollPane filmScroll, filmScrollFiltered;
    @FXML private Label labelIVA, labelCellulari, labelCosti, infoUtili, genreLabel, logLabel, nonRegistratoQuestionLabel, registerButton, areaRiservataButton;
    @FXML private Line lineGenere;

    private final Stage stageRegistrazione = new Stage();
    private final Stage stageLogin = new Stage();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static int rowCount = 0;
    private static int columnCount = 0;
    private static int columnCountMax = 0;
    private List<Movie> film = new ArrayList<>();
    private User loggedUser;

    private void initGrid(){
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

    private void addMovie(Movie movie, GridPane grid, ScrollPane scroll){
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

            posterPreview.setOnMouseClicked(e -> {
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
        KeyValue heightValueForward = new KeyValue(rectangleGenere.heightProperty(), rectangleGenere.getHeight()+305);
        KeyValue heightValueBackwards = new KeyValue(rectangleGenere.heightProperty(), rectangleGenere.getHeight()-305);
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
            if(loggedUser==null) {
                openLogin();
            } else {
                //TODO MOSTRARE AREA RISERVATA
            }
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
            if(loggedUser==null) {
                openRegistrazione();
            } else {
                doLogout();
            }
        }
    }

    private void doLogout() {
        logLabel.setText("effettua il login");
        nonRegistratoQuestionLabel.setText("non sei registrato?");
        registerButton.setText("Registrati");
        areaRiservataButton.setVisible(false);
        loggedUser = null;

        if(checkIfThereIsAlreadyUserSaved()) {
            UserInfo.deleteUserInfoFileInUserDir();
        }

        animationMenu();
    }

    private MovieTYPE type;

    public void hoverGenereEnter(MouseEvent event){
        Label label = (Label) event.getSource();
        if(genereWindow.isVisible()){
            lineGenere.setVisible(true);
        }
        
        lineGenere.setLayoutY(label.getLayoutY()+32);
        lineGenere.setStartX(-label.getWidth()/2);
        lineGenere.setEndX(label.getWidth()/2);
    }

    public void genereClicked(MouseEvent event) {
        Label l = (Label)event.getSource();
        genreLabel.setText(l.getText());

        if(type==null) {
            filterMoviesByMovieGenre(genreLabel.getText());
        } else {
            filterMoviesByMovieTYPEAndMovieGenre(type, genreLabel.getText());
        }

        animationGenere();
    }

    public void animation2D3D(MouseEvent event){
        rectangle2D3D.setVisible(true);
        TranslateTransition transition = new TranslateTransition(javafx.util.Duration.seconds(0.2), rectangle2D3D);
        Label label = (Label) event.getSource();
        transition.setToX(label.getLayoutX()-rectangle2D3D.getLayoutX()-rectangle2D3D.getWidth()/4);
        transition.play();

        switch(label.getText()) {
            case "2D":
                if(genreLabel.getText().toLowerCase().equalsIgnoreCase("genere")) {
                    filterMoviesByMovieTYPE(MovieTYPE.TWOD);
                } else {
                    filterMoviesByMovieTYPEAndMovieGenre(MovieTYPE.TWOD, genreLabel.getText());
                }
                type = MovieTYPE.TWOD;
                break;
            case "3D":
                if(genreLabel.getText().toLowerCase().equalsIgnoreCase("genere")) {
                    filterMoviesByMovieTYPE(MovieTYPE.THREED);
                } else {
                    filterMoviesByMovieTYPEAndMovieGenre(MovieTYPE.THREED, genreLabel.getText());
                }
                type = MovieTYPE.THREED;
                break;
        }
    }

    private void filterMoviesByMovieTYPE(MovieTYPE type) {
        filmGridFiltered.getChildren().clear();
        for(Movie m : film) {
            if(m.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                if(m.getTipo().equals(type)) {
                    addMovie(m, filmGridFiltered, filmScrollFiltered);
                }
            }
        }
        initRowAndColumnCount();
        filmScroll.setVisible(false);
        filmScrollFiltered.setVisible(true);
    }

    private void filterMoviesByMovieGenre(String genere) {
        filmGridFiltered.getChildren().clear();
        for(Movie m : film) {
            if(m.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                if(m.getGenere().toLowerCase().contains(genere.toLowerCase())) {
                    addMovie(m, filmGridFiltered, filmScrollFiltered);
                }
            }
        }
        initRowAndColumnCount();
        filmScroll.setVisible(false);
        filmScrollFiltered.setVisible(true);
    }

    private void filterMoviesByMovieTYPEAndMovieGenre(MovieTYPE type, String genere) {
        filmGridFiltered.getChildren().clear();
        for(Movie m : film) {
            if(m.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                if(m.getGenere().toLowerCase().contains(genere.toLowerCase()) && m.getTipo().equals(type)) {
                    addMovie(m, filmGridFiltered, filmScrollFiltered);
                }
            }
        }
        initRowAndColumnCount();
        filmScroll.setVisible(false);
        filmScrollFiltered.setVisible(true);
    }

    private void openRegistrazione(){
        try {
            stageRegistrazione.setScene(new Scene(new FXMLLoader(getClass().getResource("/fxml/user/Registrazione.fxml")).load()));
            stageRegistrazione.setResizable(false);
            stageRegistrazione.setTitle("Registrazione");
            stageRegistrazione.show();
            animationMenu();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }
    
    private void openLogin(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/Login.fxml"));
            Parent p = loader.load();
            LoginController lc = loader.getController();
            lc.init(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            throw new ApplicationException(ex);
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
        homePane.setVisible(true);
        filmScroll.setVisible(true);
        filmScrollFiltered.setVisible(false);
        if(film.isEmpty()){
            initGrid();
        }
        genreLabel.setText("genere");
        type = null;
    }

    public void listaSaleClick(){
        anchorInfo.setVisible(false);
        homePane.setVisible(false);
        animationMenu();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(checkIfThereIsAlreadyUserSaved()) {
            loggedUser = UserInfo.getUserInfo();
            setupLoggedUser();
        } else {
            areaRiservataButton.setVisible(false);
        }
        dtf.format(LocalDateTime.now());
        rectangle2D3D.setVisible(false);
        anchorInfo.setVisible(false);
        menuWindow.setVisible(false);
        lineGenere.setVisible(false);
        genereWindow.setVisible(false);
        usernamePane.setVisible(false);
        singleFilmPane.setVisible(false);
        homePane.setVisible(false);
    }

    private void setupLoggedUser() {
        logLabel.setText(loggedUser.getName());
        nonRegistratoQuestionLabel.setText("Vuoi uscire?");
        registerButton.setText("logout");
        areaRiservataButton.setVisible(true);
    }

    private boolean checkIfThereIsAlreadyUserSaved() {
        return UserInfo.checkIfUserInfoFileExists();
    }

    public void triggerNewLogin(User user) {
        loggedUser = user;
        setupLoggedUser();
    }
}