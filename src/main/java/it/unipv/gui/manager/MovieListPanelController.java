package it.unipv.gui.manager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.unipv.conversion.CSVToMovieList;
import it.unipv.conversion.CSVToMovieScheduleList;
import it.unipv.conversion.MovieScheduleToCSV;
import it.unipv.conversion.MovieToCSV;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.common.Movie;
import it.unipv.gui.common.MovieSchedule;
import it.unipv.gui.common.MovieStatusTYPE;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

public class MovieListPanelController implements Initializable {

    @FXML TextField searchBarTextfield;
    @FXML Label searchButton;
    @FXML ScrollPane movieListPanel;

    private GridPane grigliaFilm = new GridPane();
    private static int rowCount = 0;
    private static int columnCount = 0;
    private List<Movie> movies = new ArrayList<>();

    private void initMoviesList() {
        movies = CSVToMovieList.getMovieListFromCSV(DataReferences.MOVIEFILEPATH);
        Collections.sort(movies);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) { createMovieListGrid(); }

    private void createMovieListGrid() {
        grigliaFilm.getChildren().clear();

        initMoviesList();

        for (Movie movie : movies) {
            createViewFromMoviesList(movie);
        }

        GUIUtils.setScaleTransitionOnControl(searchButton);

        initRowAndColumnCount();
    }

    private void initRowAndColumnCount() {
        rowCount=0;
        columnCount=0;
    }

    private void createViewFromMoviesList(Movie movie) {
        Label movieTitleLabel = new Label(StringUtils.abbreviate(movie.getTitolo(),30));
        if(movie.getTitolo().length()>30) {
            movieTitleLabel.setTooltip(new Tooltip(movie.getTitolo()));
        }
        movieTitleLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        movieTitleLabel.setTextFill(Color.WHITE);

        grigliaFilm.setHgap(15);
        grigliaFilm.setVgap(15);

        Label deleteIcon = new Label();
        deleteIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Bin.png")));
        deleteIcon.setTooltip(new Tooltip("Elimina " + movie.getTitolo()));
        GUIUtils.setFadeInOutOnControl(deleteIcon);

        Label editIcon = new Label();
        editIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        editIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Edit.png")));
        editIcon.setTooltip(new Tooltip("Modifica " + movie.getTitolo()));
        GUIUtils.setFadeInOutOnControl(editIcon);

        Label setVisibleIcon = new Label();
        setVisibleIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setVisibleIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Visible.png")));
        setVisibleIcon.setTooltip(new Tooltip("Rendi programmabile " + movie.getTitolo()));
        GUIUtils.setFadeInOutOnControl(setVisibleIcon);

        AnchorPane pane = new AnchorPane();
        if(columnCount==1) {
            columnCount=0;
            rowCount++;
        }
        grigliaFilm.add(pane, columnCount, rowCount);
        columnCount++;

        movieListPanel.setContent(grigliaFilm);
        GridPane.setMargin(pane, new Insets(5,5,5,5));

        setVisibleIcon.setLayoutY(movieTitleLabel.getLayoutY());
        setVisibleIcon.setLayoutX(movieTitleLabel.getLayoutX()+270);
        setVisibleIcon.setOnMouseClicked(event -> {
            int reply = JOptionPane.showConfirmDialog( null
                                                     , "Sei sicuro di voler rendere " + movie.getTitolo() + " programmabile?");
            if(reply == JOptionPane.YES_OPTION) {
                movies.get(movies.indexOf(movie)).setStatus(MovieStatusTYPE.AVAILABLE);
                MovieToCSV.createCSVFromMovieList(movies, DataReferences.MOVIEFILEPATH, false);
                refreshUI();
            }
        });

        editIcon.setLayoutY(movieTitleLabel.getLayoutY());
        editIcon.setLayoutX(movieTitleLabel.getLayoutX()+305);
        editIcon.setOnMouseClicked( event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manager/MovieEditor.fxml"));
                Parent p = loader.load();
                MovieEditorController mec = loader.getController();
                mec.init(movie, this);
                Stage stage = new Stage();
                stage.setScene(new Scene(p));
                stage.setTitle("Modifica a: " + movie.getTitolo());
                stage.show();
            } catch (IOException ex) {
                throw new ApplicationException(ex);
            }
        });

        deleteIcon.setLayoutY(movieTitleLabel.getLayoutY());
        deleteIcon.setLayoutX(movieTitleLabel.getLayoutX()+340);
        deleteIcon.setOnMouseClicked(e -> {
            int reply =
                    JOptionPane.showConfirmDialog( null
                                                 , "Sei sicuro di voler eliminare " + movie.getTitolo() +" e le sue relative programmazioni?");
            if(reply == JOptionPane.YES_OPTION) {
                removeAssociatedSchedules(movie);
                movies.remove(movie);
                MovieToCSV.createCSVFromMovieList(movies, DataReferences.MOVIEFILEPATH, false);
                refreshUI();
            }

        });

        pane.getChildren().add(movieTitleLabel);
        if(!movie.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
            pane.getChildren().add(setVisibleIcon);
        }
        pane.getChildren().addAll(editIcon, deleteIcon);
    }
    private void removeAssociatedSchedules(Movie movie) {
        List<MovieSchedule> movieSchedules = CSVToMovieScheduleList.getMovieScheduleListFromCSV(DataReferences.MOVIESCHEDULEFILEPATH);
        List<MovieSchedule> toRemove = new ArrayList<>();
        for(MovieSchedule ms : movieSchedules) {
            if(movie.getCodice().equalsIgnoreCase(ms.getMovieCode())) {
                toRemove.add(ms);
            }
        }
        if(toRemove.size()!=0) {
            for(MovieSchedule ms : toRemove) {
                movieSchedules.remove(ms);
            }
            MovieScheduleToCSV.createCSVFromMovieScheduleList(movieSchedules, DataReferences.MOVIESCHEDULEFILEPATH, false);
        }
    }

    private void refreshUI() {
        grigliaFilm.getChildren().clear();
        createMovieListGrid();
    }

    @FXML public void searchButtonListener() {
        String searchedMovieTitle = searchBarTextfield.getText();
        if(searchedMovieTitle!=null || searchedMovieTitle.trim().equalsIgnoreCase("")) {
            grigliaFilm.getChildren().clear();
            for(Movie m : movies) {
                if(m.getTitolo().toLowerCase().trim().contains(searchedMovieTitle.toLowerCase())){
                    createViewFromMoviesList(m);
                }
            }
            initRowAndColumnCount();
        } else {
            refreshUI();
        }
    }

    void triggerOverwriteMovieEvent(Movie movie) {
        Movie toRemove = null;
        for(Movie m : movies) {
            if(m.getCodice().trim().equalsIgnoreCase(movie.getCodice().trim())) {
                toRemove = m;
                break;
            }
        }
        if(toRemove!=null) {
            movies.remove(toRemove);
            movies.add(movie);
            MovieToCSV.createCSVFromMovieList(movies, DataReferences.MOVIEFILEPATH, false);
            refreshUI();
        }
    }
}
