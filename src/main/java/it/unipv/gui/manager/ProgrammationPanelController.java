package it.unipv.gui.manager;

import java.io.*;
import java.util.*;

import it.unipv.conversion.CSVToMovieList;
import it.unipv.conversion.CSVToMovieScheduleList;
import it.unipv.conversion.MovieScheduleToCSV;
import it.unipv.conversion.MovieToCSV;
import it.unipv.gui.common.*;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;
import it.unipv.utils.DataReferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
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

public class ProgrammationPanelController implements IPane {

    @FXML Label nuovoFilmButton;
    @FXML ScrollPane moviePanel;
    private GridPane grigliaFilm = new GridPane();
    private static int rowCount = 0;
    private static int columnCount = 0;
    private int columnMax = 2;
    private List<Movie> movies = new ArrayList<>();
    private ManagerHomeController managerHomeController;
    private Stage movieEditorStage, movieSchedulerStage;
    private MovieSchedulerController msc;

    public void init(ManagerHomeController managerHomeController, double initialWidth) {
        this.managerHomeController = managerHomeController;
        initMoviesList();
        columnMax = getColumnMaxFromPageWidth(initialWidth);
        createMovieGrid();
        checkPageDimension();

    }

    private void initMoviesList() {
        movies = CSVToMovieList.getMovieListFromCSV(DataReferences.MOVIEFILEPATH);
        Collections.sort(movies);
    }

    private void createMovieGrid() {
        grigliaFilm.getChildren().clear();

        for (Movie movie : movies) {
            if(movie.getStatus().equals(MovieStatusTYPE.AVAILABLE)) {
                createViewFromMoviesList(movie);
            }
        }

        GUIUtils.setScaleTransitionOnControl(nuovoFilmButton);
        initRowAndColumnCount();
    }

    private void initRowAndColumnCount() {
        rowCount = 0;
        columnCount = 0;
    }

    private void createViewFromMoviesList(Movie movie) {
        try{
            Label nomeFilmLabel = new Label(StringUtils.abbreviate(movie.getTitolo(), 17));
            if(movie.getTitolo().length()>17) {
                nomeFilmLabel.setTooltip(new Tooltip(movie.getTitolo()));
            }
            nomeFilmLabel.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 20));
            nomeFilmLabel.setTextFill(Color.WHITE);

            grigliaFilm.setHgap(80);
            grigliaFilm.setVgap(80);

            FileInputStream fis = new FileInputStream(movie.getLocandinaPath());
            ImageView posterPreview = new ImageView(new Image(fis, 130, 0, true, true));
            posterPreview.setFitWidth(130);
            CloseableUtils.close(fis);

            posterPreview.setOnMouseClicked(e -> openMovieEditor(movie, false));

            Label genereFilmLabel = new Label(StringUtils.abbreviate("Genere: " + movie.getGenere(), 28));
            genereFilmLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            genereFilmLabel.setTextFill(Color.WHITE);

            Label regiaFilmLabel = new Label(StringUtils.abbreviate("Regia: " + movie.getRegia(),28));
            regiaFilmLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            regiaFilmLabel.setTextFill(Color.WHITE);

            Label castFilmLabel = new Label(StringUtils.abbreviate("Cast: " + movie.getCast(), 28));
            castFilmLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            castFilmLabel.setTextFill(Color.WHITE);

            Label annoFilmLabel = new Label(StringUtils.abbreviate("Anno: " + movie.getAnno(),28));
            annoFilmLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            annoFilmLabel.setTextFill(Color.WHITE);

            Label deleteIcon = new Label();
            deleteIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            deleteIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Bin.png")));
            deleteIcon.setTooltip(new Tooltip("Elimina " + movie.getTitolo()));
            GUIUtils.setFadeInOutOnControl(deleteIcon);

            Label showSchedulesIcon = new Label();
            showSchedulesIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            showSchedulesIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Schedule.png")));
            showSchedulesIcon.setTooltip(new Tooltip("Programma " + movie.getTitolo()));
            GUIUtils.setFadeInOutOnControl(showSchedulesIcon);

            Label hideMovieIcon = new Label();
            hideMovieIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            hideMovieIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Hide.png")));
            hideMovieIcon.setTooltip(new Tooltip("Togli dalle programmazioni " + movie.getTitolo()));
            GUIUtils.setFadeInOutOnControl(hideMovieIcon);

            AnchorPane pane = new AnchorPane();
            if(columnCount==columnMax) {
                columnCount=0;
                rowCount++;
            }
            grigliaFilm.add(pane, columnCount, rowCount);
            columnCount++;

            moviePanel.setContent(grigliaFilm);
            GridPane.setMargin(pane, new Insets(15,0,5,15));

            nomeFilmLabel.setLayoutX(posterPreview.getLayoutX() + 150);
            nomeFilmLabel.setLayoutY(posterPreview.getLayoutY()-5);

            genereFilmLabel.setLayoutY(nomeFilmLabel.getLayoutY()+40);
            genereFilmLabel.setLayoutX(nomeFilmLabel.getLayoutX());

            regiaFilmLabel.setLayoutY(nomeFilmLabel.getLayoutY()+70);
            regiaFilmLabel.setLayoutX(nomeFilmLabel.getLayoutX());

            castFilmLabel.setLayoutY(nomeFilmLabel.getLayoutY()+100);
            castFilmLabel.setLayoutX(nomeFilmLabel.getLayoutX());

            annoFilmLabel.setLayoutY(nomeFilmLabel.getLayoutY()+130);
            annoFilmLabel.setLayoutX(nomeFilmLabel.getLayoutX());

            hideMovieIcon.setLayoutY(nomeFilmLabel.getLayoutY()+167);
            hideMovieIcon.setLayoutX(nomeFilmLabel.getLayoutX());
            hideMovieIcon.setOnMouseClicked(event -> {
                int reply = JOptionPane.showConfirmDialog( null
                                                         , "Sei sicuro di voler nascondere " + movie.getTitolo() + " dai film programmabili?");
                if(reply == JOptionPane.YES_OPTION) {
                    movies.get(movies.indexOf(movie)).setStatus(MovieStatusTYPE.NOT_AVAILABLE);
                    MovieToCSV.createCSVFromMovieList(movies, DataReferences.MOVIEFILEPATH, false);
                    managerHomeController.triggerToHomeNewMovieEvent();
                    refreshUIandMovieList();
                }
            });

            showSchedulesIcon.setLayoutY(nomeFilmLabel.getLayoutY()+167);
            showSchedulesIcon.setLayoutX(nomeFilmLabel.getLayoutX()+40);
            showSchedulesIcon.setOnMouseClicked(e -> openMovieScheduler(movie));

            deleteIcon.setLayoutY(nomeFilmLabel.getLayoutY()+167);
            deleteIcon.setLayoutX(nomeFilmLabel.getLayoutX()+80);
            deleteIcon.setOnMouseClicked(e -> {
                int reply =
                        JOptionPane.showConfirmDialog( null
                                , "Sei sicuro di voler eliminare il film " + movie.getTitolo() +"?");
                if(reply == JOptionPane.YES_OPTION) {
                    removeAssociatedSchedules(movie);
                    movies.remove(movie);
                    MovieToCSV.createCSVFromMovieList(movies, DataReferences.MOVIEFILEPATH, false);
                    managerHomeController.triggerToHomeNewMovieEvent();
                    refreshUIandMovieList();
                }
            });

            pane.getChildren().addAll( posterPreview
                                     , nomeFilmLabel
                                     , genereFilmLabel
                                     , regiaFilmLabel
                                     , castFilmLabel
                                     , annoFilmLabel
                                     , hideMovieIcon
                                     , deleteIcon
                                     , showSchedulesIcon);

            GUIUtils.setScaleTransitionOnControl(posterPreview);

        } catch(FileNotFoundException ex) {
            throw new ApplicationException(ex);
        }
    }

    private boolean isMovieSchedulerAlreadyOpened = false;
    private void openMovieScheduler(Movie movie) {
        if(!isMovieSchedulerAlreadyOpened) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manager/MovieScheduler.fxml"));
                Parent p = loader.load();
                msc = loader.getController();
                msc.init(movie);
                movieSchedulerStage = new Stage();
                movieSchedulerStage.setScene(new Scene(p));
                movieSchedulerStage.setTitle("Programmazione " + movie.getTitolo());
                movieSchedulerStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
                movieSchedulerStage.setOnCloseRequest(event -> {
                    isMovieSchedulerAlreadyOpened = false;
                    msc.closeAllSubWindows();
                });
                movieSchedulerStage.show();
                isMovieSchedulerAlreadyOpened = true;
            } catch (IOException ex) {
                throw new ApplicationException(ex);
            }
        }
    }

    private boolean isMovieEditorAlreadyOpened = false;
    private void openMovieEditor(Movie movie, boolean isANewFilm) {
        if(!isMovieEditorAlreadyOpened) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manager/MovieEditor.fxml"));
                Parent p = loader.load();
                MovieEditorController mec = loader.getController();
                movieEditorStage = new Stage();
                movieEditorStage.setScene(new Scene(p));
                if(isANewFilm) {
                    mec.init(this);
                    movieEditorStage.setTitle("Editor Film");

                } else {
                    mec.init(movie, this);
                    movieEditorStage.setTitle("Modifica: " + movie.getTitolo());
                }
                movieEditorStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
                movieEditorStage.setOnCloseRequest(event -> isMovieEditorAlreadyOpened=false);
                movieEditorStage.show();
                isMovieEditorAlreadyOpened = true;
            } catch (IOException ex) {
                throw new ApplicationException(ex);
            }
        }
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

    @FXML public void nuovoFilmButtonListener() {
       openMovieEditor(null, true);
    }

    void triggerNewMovieEvent() {
        refreshUIandMovieList();
        managerHomeController.triggerToHomeNewMovieEvent();
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
            managerHomeController.triggerToHomeNewMovieEvent();
            refreshUIandMovieList();
        }
    }

    private void refreshUIandMovieList() {
        grigliaFilm.getChildren().clear();
        initMoviesList();
        createMovieGrid();
    }

    private void refreshUI() {
        grigliaFilm.getChildren().clear();
        createMovieGrid();
    }

    private int temp = 0;
    private void checkPageDimension() {
        Platform.runLater(() -> {
            Stage stage = (Stage) moviePanel.getScene().getWindow();
            stage.widthProperty().addListener(e -> {
                columnMax = getColumnMaxFromPageWidth(stage.getWidth());
                if (temp != columnMax) {
                    temp = columnMax;
                    refreshUI();
                }
            });
        });
    }

    private int getColumnMaxFromPageWidth(double width) {
        if(width<800) {
            return 1;
        } else if(width>800 && width<=1360) {
            return 2;
        } else if(width>1360 && width<=1600) {
            return 3;
        } else if(width>1600) {
            return 4;
        } else {
            return 5;
        }
    }

    @Override
    public void closeAllSubWindows() {
        if(movieEditorStage != null) {
            if(movieEditorStage.isShowing()) {
                isMovieEditorAlreadyOpened = false;
                movieEditorStage.close();
            }
        }

        if(movieSchedulerStage != null) {
            if(movieSchedulerStage.isShowing()) {
                msc.closeAllSubWindows();
                movieSchedulerStage.close();
                isMovieSchedulerAlreadyOpened = false;
            }
        }
    }
}
