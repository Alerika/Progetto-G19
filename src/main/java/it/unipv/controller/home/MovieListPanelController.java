package it.unipv.controller.home;

import it.unipv.db.DBConnection;
import it.unipv.dao.MovieDao;
import it.unipv.dao.MovieDaoImpl;
import it.unipv.controller.common.*;
import it.unipv.model.Movie;
import it.unipv.model.MovieStatusTYPE;
import it.unipv.model.MovieTYPE;
import it.unipv.utils.ApplicationException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieListPanelController {
    private MovieDao movieDao;
    private GridPane filmGrid = new GridPane();
    private List<Movie> movies = new ArrayList<>();
    private static int rowCount = 0;
    private static int columnCount = 0;
    private static int columnMax = 0;
    private IHomeTrigger homeController;
    @FXML private ScrollPane movieScroll;
    @FXML private Rectangle rectangleGenere, rectangle2D3D;
    @FXML private AnchorPane filterContainer, genereWindow;
    @FXML private Line lineGenere;
    @FXML private Label genreLabel;

    public void init(IHomeTrigger homeController, double initialWidth, DBConnection dbConnection) {
        this.movieDao = new MovieDaoImpl(dbConnection);
        this.homeController = homeController;

        rectangle2D3D.setVisible(false);
        lineGenere.setVisible(false);
        genereWindow.setVisible(false);
        genereWindow.setPickOnBounds(false);
        filterContainer.setPickOnBounds(false);
        columnMax = getColumnMaxFromPageWidth(initialWidth);

        createUI();

        checkPageDimension();
    }

    private void createUI() {
        homeController.triggerStartStatusEvent("Carico i film programmati...");
        Platform.runLater(() -> {
            initMovieList();
            initMovieGrid();
        });
        homeController.triggerEndStatusEvent("Film programmati correttamente caricati!");
    }

    private void initMovieList() {
        movies = movieDao.retrieveCompleteMovieList(1000, 0, true, true);
        Collections.sort(movies);
    }

    private void initMovieGrid(){
        filmGrid.getChildren().clear();
        filmGrid.setHgap(120);
        filmGrid.setVgap(80);

        for (Movie movie : movies) {
            if(movie.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                addMovie(movie, filmGrid, movieScroll);
            }
        }

        initRowAndColumnCount();
    }

    private void initRowAndColumnCount() {
        rowCount = 0;
        columnCount = 0;
    }

    private void addMovie(Movie movie, GridPane grid, ScrollPane scroll){
        ImageView posterPreview = new ImageView(movie.getLocandina());
        posterPreview.setPreserveRatio(true);
        posterPreview.setFitWidth(200);

        AnchorPane anchor = new AnchorPane();

        if (columnCount == columnMax) {
            columnCount = 0;
            rowCount++;
        }

        grid.add(anchor, columnCount, rowCount);
        columnCount++;

        scroll.setContent(grid);
        GridPane.setMargin(anchor, new Insets(15, 0, 5, 15));

        anchor.getChildren().addAll(posterPreview);
        posterPreview.setLayoutX(30);
        if (rowCount == 0) {
            posterPreview.setLayoutY(20);
        }

        posterPreview.setOnMouseClicked(e -> homeController.triggerMovieClicked(movie));

        GUIUtils.setScaleTransitionOnControl(posterPreview);
    }

    public void animationGenere(){
        if(!genereWindow.isVisible()){
            genereWindow.setOpacity(0);
            genereWindow.setVisible(true);
            new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.3), new KeyValue(rectangleGenere.heightProperty(), rectangleGenere.getHeight()+240))).play();
            FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.seconds(0.4), genereWindow);
            fadeIn.setDelay(javafx.util.Duration.seconds(0.2));
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        } else {
            if(genereWindow.isVisible()){
                FadeTransition fadeOut = new FadeTransition(javafx.util.Duration.seconds(0.1), genereWindow);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0);
                fadeOut.play();
                genereWindow.setVisible(false);
                new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.3), new KeyValue(rectangleGenere.heightProperty(), rectangleGenere.getHeight()-240))).play();
            }
        }
    }


    private MovieTYPE type;

    public void hoverGenereEnter(MouseEvent event){
        Label label = (Label) event.getSource();
        if(genereWindow.isVisible()){
            lineGenere.setVisible(true);
        }

        lineGenere.setLayoutY(label.getLayoutY()+30);
        lineGenere.setStartX(-label.getWidth()/3);
        lineGenere.setEndX(label.getWidth()/3);
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
        filmGrid.getChildren().clear();
        for(Movie m : movies) {
            if(m.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                if(m.getTipo().equals(type)) {
                    addMovie(m, filmGrid, movieScroll);
                }
            }
        }
        initRowAndColumnCount();
    }

    private void filterMoviesByMovieGenre(String genere) {
        filmGrid.getChildren().clear();
        for(Movie m : movies) {
            if(m.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                if(m.getGenere().toLowerCase().contains(genere.toLowerCase())) {
                    addMovie(m, filmGrid, movieScroll);
                }
            }
        }
        initRowAndColumnCount();
    }

    private void filterMoviesByMovieTYPEAndMovieGenre(MovieTYPE type, String genere) {
        filmGrid.getChildren().clear();
        for(Movie m : movies) {
            if(m.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                if(m.getGenere().toLowerCase().contains(genere.toLowerCase()) && m.getTipo().equals(type)) {
                    addMovie(m, filmGrid, movieScroll);
                }
            }
        }
        initRowAndColumnCount();
    }

    void triggerNewMovieEvent() { createUI(); }

    private int temp = 0;
    private void checkPageDimension() {
        Platform.runLater(() -> {
            Stage stage = (Stage) movieScroll.getScene().getWindow();
            stage.widthProperty().addListener(e -> {
                columnMax = getColumnMaxFromPageWidth(stage.getWidth());
                if (temp != columnMax) {
                    temp = columnMax;
                    initMovieGrid();
                }
            });
        });
    }

    //Supporta fino ai 1080p
    private int getColumnMaxFromPageWidth(double width) {
        if(width<800) {
            return 2;
        } else if(width>=800 && width<=1360) {
            return 3;
        } else if(width>1360 && width<=1600) {
            return 3;
        } else if(width>1600 && width<=1700) {
            return 4;
        } else if(width>1700){
            return 5;
        } else {
            throw new ApplicationException("Impossibile settare numero colonne per width: " + width);
        }
    }
}
