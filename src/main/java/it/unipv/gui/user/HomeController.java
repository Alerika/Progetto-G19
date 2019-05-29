package it.unipv.gui.user;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import it.unipv.conversion.CSVToMovieList;
import it.unipv.conversion.CSVToMovieScheduleList;
import it.unipv.gui.common.*;
import it.unipv.gui.login.LoginController;
import it.unipv.gui.login.User;
import it.unipv.gui.login.UserInfo;
import it.unipv.gui.user.areariservata.AreaRiservataHomeController;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.ApplicationUtils;
import it.unipv.utils.CloseableUtils;
import it.unipv.utils.DataReferences;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class HomeController implements Initializable {
    
    @FXML private Rectangle rectangleMenu, rectangleGenere, rectangle2D3D;
    @FXML private AnchorPane menuWindow, genereWindow, usernamePane, anchorInfo, homePane, singleFilmPane;
    @FXML private GridPane filmGrid = new GridPane();
    @FXML private GridPane filmGridFiltered = new GridPane();
    @FXML private ScrollPane filmScroll, filmScrollFiltered;
    @FXML private Label labelIVA, labelCellulari, labelCosti, infoUtili, genreLabel, logLabel, nonRegistratoQuestionLabel, registerButton, areaRiservataButton;
    @FXML private Line lineGenere;
    @FXML private Label goBackToHomeButton;

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

            posterPreview.setOnMouseClicked(e -> populateSingleFilmPane(movie));

            GUIUtils.setScaleTransitionOnControl(posterPreview);

        } catch(FileNotFoundException ex) {
            throw new ApplicationException(ex);
        }
    }

    private void populateSingleFilmPane(Movie movie) {
        homePane.setVisible(false);
        singleFilmPane.getChildren().clear();

        Label title = new Label();
        Label movieTitle = new Label();

        Label genre = new Label();
        Label movieGenre = new Label();

        Label direction = new Label();
        Label movieDirection = new Label();

        Label cast = new Label();
        TextArea movieCast = new TextArea();
        movieCast.getStylesheets().add("css/TextAreaStyle.css");
        movieCast.sceneProperty().addListener((observableNewScene, oldScene, newScene) -> {
            if (newScene != null) {
                movieCast.applyCss();
                Node text = movieCast.lookup(".text");

                movieCast.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> movieCast.getFont().getSize() + text.getBoundsInLocal().getHeight(), text.boundsInLocalProperty()));

                text.boundsInLocalProperty().addListener((observableBoundsAfter, boundsBefore, boundsAfter) -> Platform.runLater(movieCast::requestLayout)
                );
            }
        });
        movieCast.getStyleClass().add("movieCastTA");
        movieCast.setWrapText(true);

        Label time = new Label();
        Label movieTime = new Label();

        Label year = new Label();
        Label movieYear = new Label();

        Label programmationsLabel = new Label();

        Font infoFont = new Font("Bebas Neue Regular", 24);
        ImageView poster;
        try {
            FileInputStream fis2 = new FileInputStream(movie.getLocandinaPath());
            poster = new ImageView(new Image(fis2, 1000, 0, true, true));
            poster.setPreserveRatio(true);
            CloseableUtils.close(fis2);
        } catch (FileNotFoundException exce){
            throw new ApplicationException(exce);
        }

        poster.setFitWidth(350);

        title.setText("TITOLO: ");
        title.setTextFill(Color.valueOf("db8f00"));
        title.setLayoutX(poster.getLayoutX() + poster.getFitWidth() + 20);
        title.setLayoutY(poster.getLayoutY() + 25);
        title.setFont(infoFont);

        movieTitle.setText(movie.getTitolo());
        movieTitle.setTextFill(Color.WHITE);
        movieTitle.setLayoutX(title.getLayoutX() + 110);
        movieTitle.setLayoutY(title.getLayoutY());
        movieTitle.setFont(infoFont);

        genre.setText("GENERE: ");
        genre.setTextFill(Color.valueOf("db8f00"));
        genre.setLayoutX(title.getLayoutX());
        genre.setLayoutY(title.getLayoutY() + 50);
        genre.setFont(infoFont);

        movieGenre.setText(movie.getGenere());
        movieGenre.setTextFill(Color.WHITE);
        movieGenre.setLayoutX(movieTitle.getLayoutX());
        movieGenre.setLayoutY(genre.getLayoutY());
        movieGenre.setFont(infoFont);

        direction.setText("REGIA: ");
        direction.setTextFill(Color.valueOf("db8f00"));
        direction.setLayoutX(title.getLayoutX());
        direction.setLayoutY(genre.getLayoutY() + 50);
        direction.setFont(infoFont);

        movieDirection.setText(movie.getRegia());
        movieDirection.setTextFill(Color.WHITE);
        movieDirection.setLayoutX(movieTitle.getLayoutX());
        movieDirection.setLayoutY(direction.getLayoutY());
        movieDirection.setFont(infoFont);

        cast.setText("CAST: ");
        cast.setTextFill(Color.valueOf("db8f00"));
        cast.setLayoutX(title.getLayoutX());
        cast.setLayoutY(direction.getLayoutY() + 50);
        cast.setFont(infoFont);

        movieCast.setText(StringUtils.abbreviate(movie.getCast(),170));
        if(movie.getCast().length()>170) {
            movieCast.setTooltip(new Tooltip(getFormattedTooltipText(movie, ',')));
        }

        movieCast.setEditable(false);
        movieCast.setLayoutX(movieDirection.getLayoutX() -15);
        movieCast.setLayoutY(cast.getLayoutY()-8);
        movieCast.setFont(infoFont);

        singleFilmPane.getChildren().addAll(cast, movieCast);
        singleFilmPane.applyCss();
        singleFilmPane.layout();

        time.setText("DURATA: ");
        time.setTextFill(Color.valueOf("db8f00"));
        time.setLayoutX(title.getLayoutX());
        time.setLayoutY(movieCast.getLayoutY() + movieCast.prefHeightProperty().getValue());
        time.setFont(infoFont);

        movieTime.setText(movie.getDurata() + " minuti");
        movieTime.setTextFill(Color.WHITE);
        movieTime.setLayoutX(movieTitle.getLayoutX());
        movieTime.setLayoutY(time.getLayoutY());
        movieTime.setFont(infoFont);

        year.setText("ANNO: ");
        year.setTextFill(Color.valueOf("db8f00"));
        year.setLayoutX(title.getLayoutX());
        year.setLayoutY(time.getLayoutY() + 50);
        year.setFont(infoFont);

        movieYear.setText(movie.getAnno());
        movieYear.setTextFill(Color.WHITE);
        movieYear.setLayoutX(movieTitle.getLayoutX());
        movieYear.setLayoutY(year.getLayoutY());
        movieYear.setFont(infoFont);

        programmationsLabel.setText("PROGRAMMATO PER: ");
        programmationsLabel.setTextFill(Color.valueOf("db8f00"));
        programmationsLabel.setLayoutX(title.getLayoutX());
        programmationsLabel.setLayoutY(year.getLayoutY()+50);
        programmationsLabel.setFont(infoFont);

        double x = title.getLayoutX()+20;
        double y = programmationsLabel.getLayoutY()+35;
        int count = 0;
        int i = 0;

        //Metto massimo 10 date perché se no si sovrappongono con la trama.. tanto ogni giorno toglie una data vecchia e ne aggiunge una nuova
        List<MovieSchedule> schedules = getProgrammationListFromMovie(movie);
        for(MovieSchedule ms : schedules) {
            if(i<10) {
                if(!ApplicationUtils.checkIfDateIsPassed(ms.getDate())) {
                    Label scheduleLabel = new Label();
                    scheduleLabel.setText("  " + ms.getDate() + "  ");
                    scheduleLabel.setTextFill(Color.WHITE);
                    if(count>=5) {
                        y+=50;
                        x = title.getLayoutX()+20;
                        count = 0;
                    }
                    scheduleLabel.setLayoutY(y);
                    scheduleLabel.setLayoutX(x);
                    scheduleLabel.setFont(infoFont);
                    scheduleLabel.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    scheduleLabel.setOnMouseEntered(event -> {
                        scheduleLabel.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                        scheduleLabel.setCursor(Cursor.HAND);
                    });
                    scheduleLabel.setOnMouseExited(event -> {
                        scheduleLabel.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                        scheduleLabel.setCursor(Cursor.DEFAULT);
                    });

                    scheduleLabel.setOnMouseClicked(event -> {
                        if(loggedUser==null) {
                            GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore", "Devi aver effettuato il login per poter accedere alla prenotazione!");
                        } else if (!isHimANormalUser(loggedUser)){
                            GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore", "Non puoi effettuare una prenotazione con questo account!");
                        } else {
                            openPrenotationStage(movie, scheduleLabel);
                        }
                    });
                    x += 190;
                    count++;
                    singleFilmPane.getChildren().add(scheduleLabel);
                }
            }
            i++;
        }

        Label synopsis = new Label("Trama:");
        synopsis.setTextFill(Color.valueOf("db8f00"));
        synopsis.setLayoutX(poster.getLayoutX() + 15);
        synopsis.setLayoutY(poster.getLayoutY()+515);
        synopsis.setFont(infoFont);

        TextArea movieSynopsis = new TextArea();
        movieSynopsis.setText(movie.getTrama());
        movieSynopsis.getStylesheets().add("css/TextAreaStyle.css");
        movieSynopsis.sceneProperty().addListener((observableNewScene, oldScene, newScene) -> {
            if (newScene != null) {
                movieSynopsis.applyCss();
                Node text = movieSynopsis.lookup(".text");

                movieSynopsis.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> movieSynopsis.getFont().getSize() + text.getBoundsInLocal().getHeight(), text.boundsInLocalProperty()));

                text.boundsInLocalProperty().addListener((observableBoundsAfter, boundsBefore, boundsAfter) -> Platform.runLater(movieSynopsis::requestLayout)
                );
            }
        });
        movieSynopsis.getStyleClass().add("movieSynopsisTA");
        movieSynopsis.setFont(infoFont);
        movieSynopsis.setWrapText(true);
        movieSynopsis.setEditable(false);
        movieSynopsis.setLayoutX(synopsis.getLayoutX()-15);
        movieSynopsis.setLayoutY(synopsis.getLayoutY()+30);
        movieSynopsis.setPrefWidth(1400);

        singleFilmPane.getChildren().addAll( title, movieTitle
                                           , genre, movieGenre
                                           , direction, movieDirection
                                           , time, movieTime
                                           , year, movieYear
                                           , programmationsLabel
                                           , poster
                                           , goBackToHomeButton
                                           , synopsis, movieSynopsis);

        GUIUtils.setScaleTransitionOnControl(goBackToHomeButton);
        goBackToHomeButton.setOnMouseClicked(event -> {
            singleFilmPane.setVisible(false);
            homePane.setVisible(true);
        });

        singleFilmPane.setVisible(true);
    }

    private String getFormattedTooltipText(Movie movie, char escape) {
        StringBuilder res = new StringBuilder();
        char[] temp = movie.getCast().toCharArray();
        int cont = 0;
        for(int i=0; i<movie.getCast().length();i++) {
            if(cont == 5) {
                res.append("\n").append(temp[i]);
                cont = 0;
            } else {
                res.append(temp[i]);
            }
            if(temp[i] == escape) {
                cont++;
            }
        }
        return res.toString();
    }

    private boolean isPrenotationAreaOpened = false;
    private void openPrenotationStage(Movie movie, Label scheduleLabel) {
        if(!isPrenotationAreaOpened) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/MoviePrenotation.fxml"));
                Parent p = loader.load();
                MoviePrenotationController mpc = loader.getController();
                mpc.init(scheduleLabel.getText().trim(), movie, loggedUser);
                Stage stage = new Stage();
                stage.setScene(new Scene(p));
                stage.setResizable(false);
                stage.setTitle("Prenotazione " + movie.getTitolo() + " " + scheduleLabel.getText().trim());
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
                stage.setOnCloseRequest( event -> isPrenotationAreaOpened = false);
                stage.show();
                isPrenotationAreaOpened = true;
            } catch (IOException ex) {
                throw new ApplicationException(ex);
            }
        }
    }

    private List<MovieSchedule> getProgrammationListFromMovie(Movie m) {
        String date = "";
        List<MovieSchedule> allSchedules = CSVToMovieScheduleList.getMovieScheduleListFromCSV(DataReferences.MOVIESCHEDULEFILEPATH);
        Collections.sort(allSchedules);
        List<MovieSchedule> res = new ArrayList<>();
        for(MovieSchedule ms : allSchedules) {
            if(ms.getMovieCode().equals(m.getCodice())) {
                if(!date.equals(ms.getDate())) {
                    res.add(ms);
                    date = ms.getDate();
                }
            }
        }
        return res;
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
                openReservedArea();
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

        homeClick();
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
            stageRegistrazione.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
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
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
            stage.show();
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        }
    }
    
    public void infoPaneClick() {
        homePane.setVisible(false);
        singleFilmPane.setVisible(false);
        filmScroll.setVisible(false);
        filmScrollFiltered.setVisible(false);
        rectangle2D3D.setVisible(false);

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
        if(film.isEmpty()){ initGrid(); }
        genreLabel.setText("genere");
        type = null;
    }

    public void listaSaleClick(){
        anchorInfo.setVisible(false);
        homePane.setVisible(false);
        animationMenu();
    }

    @FXML
    public void areaRiservataClick() {
        openReservedArea();
        animationMenu();
    }

    private void openReservedArea() {
        if(!isHimANormalUser(loggedUser)) {
            doOpenManagerArea();
        } else {
            doOpenReservedArea();
        }
    }

    private boolean isManagerAreaOpened = false;
    private void doOpenManagerArea() {
        if(!isManagerAreaOpened) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/manager/ManagerHome.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Area Manager");
                stage.setMinHeight(850);
                stage.setMinWidth(1100);
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
                stage.setOnCloseRequest( event -> isManagerAreaOpened = false);
                stage.show();
                isManagerAreaOpened = true;
            } catch (IOException e) {
                throw new ApplicationException(e);
            }
        }

    }

    private boolean isReservedAreaOpened = false;
    private void doOpenReservedArea() {
        if(!isReservedAreaOpened) {
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
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
                stage.setOnCloseRequest( event -> isReservedAreaOpened = false);
                stage.show();
                isReservedAreaOpened = true;
            } catch (IOException ex) {
                throw new ApplicationException(ex);
            }
        }
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
        if(!isHimANormalUser(loggedUser)) {
            areaRiservataButton.setText("Area Manager");
        } else {
            areaRiservataButton.setText("Area Riservata");
        }
        areaRiservataButton.setVisible(true);
    }

    private boolean isHimANormalUser(User user) {
        return !( user.getName().equalsIgnoreCase(DataReferences.ADMINUSERNAME)
               &&  user.getPassword().equalsIgnoreCase(DataReferences.ADMINPASSWORD));
    }

    private boolean checkIfThereIsAlreadyUserSaved() {
        return UserInfo.checkIfUserInfoFileExists();
    }

    public void triggerNewLogin(User user) {
        loggedUser = user;
        setupLoggedUser();
    }
}