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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
        movieTitleLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        movieTitleLabel.setTextFill(Color.WHITE);

        grigliaFilm.setHgap(15);
        grigliaFilm.setVgap(15);

        ImageView deleteIconView = GUIUtils.getIconView(getClass().getResourceAsStream("/images/Bin.png"));
        GUIUtils.setFadeInOutOnControl(deleteIconView);

        ImageView editIconView = GUIUtils.getIconView(getClass().getResourceAsStream("/images/Edit.png"));
        GUIUtils.setFadeInOutOnControl(editIconView);

        ImageView setVisibleIconView = GUIUtils.getIconView(getClass().getResourceAsStream("/images/Visible.png"));
        GUIUtils.setFadeInOutOnControl(setVisibleIconView);

        AnchorPane pane = new AnchorPane();
        if(columnCount==1) {
            columnCount=0;
            rowCount++;
        }
        grigliaFilm.add(pane, columnCount, rowCount);
        columnCount++;

        movieListPanel.setContent(grigliaFilm);
        GridPane.setMargin(pane, new Insets(5,5,5,5));

        setVisibleIconView.setY(movieTitleLabel.getLayoutY());
        setVisibleIconView.setX(movieTitleLabel.getLayoutX()+270);
        setVisibleIconView.setOnMouseClicked(event -> {
            int reply = JOptionPane.showConfirmDialog( null
                                                     , "Sei sicuro di voler rendere " + movie.getTitolo() + " programmabile?");
            if(reply == JOptionPane.YES_OPTION) {
                movies.get(movies.indexOf(movie)).setStatus(MovieStatusTYPE.AVAILABLE);
                MovieToCSV.createCSVFromMovieList(movies, DataReferences.MOVIEFILEPATH, false);
                refreshUI();
            }
        });

        editIconView.setY(movieTitleLabel.getLayoutY());
        editIconView.setX(movieTitleLabel.getLayoutX()+305);
        editIconView.setOnMouseClicked( event -> {
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

        deleteIconView.setY(movieTitleLabel.getLayoutY());
        deleteIconView.setX(movieTitleLabel.getLayoutX()+340);
        deleteIconView.setOnMouseClicked(e -> {
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

        pane.getChildren().addAll(movieTitleLabel);
        if(!movie.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
            pane.getChildren().addAll(setVisibleIconView);
        }
        pane.getChildren().addAll(editIconView);
        pane.getChildren().addAll(deleteIconView);
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
