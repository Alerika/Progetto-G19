package it.unipv.controller.managerarea;

import java.io.*;
import java.util.*;

import it.unipv.db.*;
import it.unipv.dao.MovieDao;
import it.unipv.dao.ScheduleDao;
import it.unipv.dao.MovieDaoImpl;
import it.unipv.dao.ScheduleDaoImpl;
import it.unipv.controller.common.*;
import it.unipv.model.Movie;
import it.unipv.model.Schedule;
import it.unipv.model.MovieStatusTYPE;
import it.unipv.utils.ApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class ProgrammationPanelController implements ICloseablePane {

    @FXML private Label nuovoFilmButton;
    @FXML private ScrollPane moviePanel;
    private GridPane grigliaFilm = new GridPane();
    private static int rowCount = 0;
    private static int columnCount = 0;
    private int columnMax = 2;
    private List<Movie> movies = new ArrayList<>();
    private IManagerAreaTrigger managerHomeController;
    private Stage movieEditorStage, movieSchedulerStage;
    private MovieSchedulerController msc;
    private MovieDao movieDao;
    private ScheduleDao scheduleDao;
    private DBConnection dbConnection;

    public void init(IManagerAreaTrigger managerHomeController, double initialWidth, DBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.movieDao = new MovieDaoImpl(dbConnection);
        this.scheduleDao = new ScheduleDaoImpl(dbConnection);
        this.managerHomeController = managerHomeController;
        columnMax = getColumnMaxFromPageWidth(initialWidth);
        createUI();
        checkPageDimension();

    }

    private void createUI() {
        managerHomeController.triggerStartStatusEvent("Carico film attualmente programmati...");
        initMoviesList();
        createMovieGrid();
        managerHomeController.triggerEndStatusEvent("Film programmati correttamente caricati!");
    }

    private void initMoviesList() {
        movies = movieDao.retrieveCompleteMovieList(130, 0, true, true);
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
        Label nomeFilmLabel = new Label(StringUtils.abbreviate(movie.getTitolo(), 17));
        if(movie.getTitolo().length()>17) {
            nomeFilmLabel.setTooltip(new Tooltip(movie.getTitolo()));
        }
        nomeFilmLabel.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 20));
        nomeFilmLabel.setTextFill(Color.WHITE);

        grigliaFilm.setHgap(80);
        grigliaFilm.setVgap(80);

        ImageView posterPreview = new ImageView(movie.getLocandina());

        posterPreview.setOnMouseClicked(e -> openMovieEditor(movie, false));

        Font infoFont = Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15);

        Label genereFilmLabel = new Label(StringUtils.abbreviate("Genere: " + movie.getGenere(), 28));
        genereFilmLabel.setFont(infoFont);
        genereFilmLabel.setTextFill(Color.WHITE);

        Label regiaFilmLabel = new Label(StringUtils.abbreviate("Regia: " + movie.getRegia(),28));
        regiaFilmLabel.setFont(infoFont);
        regiaFilmLabel.setTextFill(Color.WHITE);

        Label castFilmLabel = new Label(StringUtils.abbreviate("Cast: " + movie.getCast(), 28));
        castFilmLabel.setFont(infoFont);
        castFilmLabel.setTextFill(Color.WHITE);

        Label annoFilmLabel = new Label(StringUtils.abbreviate("Anno: " + movie.getAnno(),28));
        annoFilmLabel.setFont(infoFont);
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
        hideMovieIcon.setOnMouseClicked(event -> doHideMovie(movie));

        showSchedulesIcon.setLayoutY(nomeFilmLabel.getLayoutY()+167);
        showSchedulesIcon.setLayoutX(nomeFilmLabel.getLayoutX()+40);
        showSchedulesIcon.setOnMouseClicked(e -> openMovieScheduler(movie));

        deleteIcon.setLayoutY(nomeFilmLabel.getLayoutY()+167);
        deleteIcon.setLayoutX(nomeFilmLabel.getLayoutX()+80);
        deleteIcon.setOnMouseClicked(e -> doDeleteMovie(movie));

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
    }

    private void doDeleteMovie(Movie movie) {
        Optional<ButtonType> option =
                GUIUtils.showConfirmationAlert( "Attenzione"
                                              , "Richiesta conferma:"
                                              , "Sei sicuro di voler eliminare il film " + movie.getTitolo() +"?");
        if(option.orElse(null)==ButtonType.YES) {
            managerHomeController.triggerStartStatusEvent("Elimino il film " + movie.getTitolo() + "...");
            removeAssociatedSchedules(movie);
            movieDao.deleteMovie(movie);
            managerHomeController.triggerToHomeNewMovieEvent();
            refreshUIandMovieList();
            managerHomeController.triggerEndStatusEvent(movie.getTitolo() + " correttamente eliminato!");
        }
    }

    private void doHideMovie(Movie movie) {
        Optional<ButtonType> option =
                GUIUtils.showConfirmationAlert( "Attenzione"
                                              , "Richiesta conferma:"
                                              , "Sei sicuro di voler nascondere " + movie.getTitolo() + " dai film programmabili?");
        if(option.orElse(null)==ButtonType.YES) {
            managerHomeController.triggerStartStatusEvent("Nascondo " + movie.getTitolo() + " dai film programmabili...");
            movie.setStatus(MovieStatusTYPE.NOT_AVAILABLE);
            movieDao.updateMovieButNotPoster(movie);
            managerHomeController.triggerToHomeNewMovieEvent();
            refreshUIandMovieList();
            managerHomeController.triggerEndStatusEvent(movie.getTitolo() + " correttamente nascosto dai film programmabili!");
        }
    }

    private boolean isMovieSchedulerAlreadyOpened = false;
    private void openMovieScheduler(Movie movie) {
        if(!isMovieSchedulerAlreadyOpened) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/managerarea/MovieScheduler.fxml"));
                Parent p = loader.load();
                msc = loader.getController();
                msc.init(movie, dbConnection);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/managerarea/MovieEditor.fxml"));
                Parent p = loader.load();
                MovieEditorController mec = loader.getController();
                movieEditorStage = new Stage();
                movieEditorStage.setScene(new Scene(p));
                if(isANewFilm) {
                    mec.init(this, dbConnection);
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
        List<Schedule> schedules = scheduleDao.retrieveMovieSchedules();
        for(Schedule ms : schedules) {
            if(movie.getCodice().equalsIgnoreCase(ms.getMovieCode())) {
                scheduleDao.deleteMovieSchedule(ms);
            }
        }
    }

    @FXML private void nuovoFilmButtonListener() {
       openMovieEditor(null, true);
    }

    void triggerNewMovieEvent(Movie movie, FileInputStream posterStream) {
        managerHomeController.triggerStartStatusEvent("Inserisco " + movie.getTitolo() + " a sistema...");
        movieDao.insertNewMovie(movie, posterStream);
        managerHomeController.triggerToHomeNewMovieEvent();
        refreshUIandMovieList();
        managerHomeController.triggerEndStatusEvent(movie.getTitolo() + " correttamente inserito a sistema!");
    }

    void triggerOverwriteMovieButNotPosterEvent(Movie movie) {
        triggerToHome(movie, null);
    }

    void triggerOverwriteMovieEvent(Movie movie, FileInputStream posterStream) {
        triggerToHome(movie, posterStream);
    }

    private void triggerToHome(Movie movie, FileInputStream posterStream) {
        managerHomeController.triggerStartStatusEvent("Aggiorno " + movie.getTitolo() + "...");
        if(posterStream == null) {
            movieDao.updateMovieButNotPoster(movie);
            managerHomeController.triggerToHomeNewMovieEvent();
            refreshUIandMovieList();
        } else {
            movieDao.updateMovie(movie, posterStream);
            managerHomeController.triggerToHomeNewMovieEvent();
            refreshUIandMovieList();
        }
        managerHomeController.triggerEndStatusEvent(movie.getTitolo() + " correttamente aggiornato!");
    }

    private void refreshUIandMovieList() { createUI(); }

    private void refreshUI() { createMovieGrid(); }

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
