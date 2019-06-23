package it.unipv.controller.managerarea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.unipv.db.DBConnection;
import it.unipv.dao.ScheduleDao;
import it.unipv.dao.ScheduleDaoImpl;
import it.unipv.controller.common.GUIUtils;
import it.unipv.controller.common.ICloseablePane;
import it.unipv.model.Movie;
import it.unipv.model.Schedule;
import it.unipv.utils.ApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MovieSchedulerController implements ICloseablePane {

    @FXML Label nuovaProgrammazioneButton;
    @FXML ScrollPane schedulerPanel;
    private Stage movieSchedulerEditorStage;
    private GridPane grigliaProgrammazione = new GridPane();
    private static int rowCount = 0;
    private static int columnCount = 0;
    private List<Schedule> schedules = new ArrayList<>();
    private List<Schedule> actualSchedules = new ArrayList<>();
    private Movie movie;
    private DBConnection dbConnection;
    private ScheduleDao scheduleDao;

    void init(Movie movie, DBConnection dbConnection) {
        this.dbConnection = dbConnection;
        scheduleDao = new ScheduleDaoImpl(dbConnection);
        initScheduleGrid(movie);
    }

    private void initScheduleGrid(Movie movie) {
        grigliaProgrammazione.getChildren().clear();

        this.movie = movie;
        initMovieSchedulesList();

        for (Schedule schedule : actualSchedules) {
            createViewFromMovieSchedulesList(schedule);
        }

        GUIUtils.setScaleTransitionOnControl(nuovaProgrammazioneButton);

        rowCount = 0;
        columnCount = 0;
    }

    private boolean isMovieSchedulerEditorAlreadyOpened = false;
    @FXML public void nuovaProgrammazioneButtonListener() {
        if(!isMovieSchedulerEditorAlreadyOpened) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/managerarea/MovieScheduleEditor.fxml"));
                Parent p = loader.load();
                MovieScheduleEditorController msec = loader.getController();
                msec.init(this, movie, dbConnection);
                movieSchedulerEditorStage = new Stage();
                movieSchedulerEditorStage.setScene(new Scene(p));
                movieSchedulerEditorStage.setTitle("Nuova programmazione per " + movie.getTitolo());
                movieSchedulerEditorStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
                movieSchedulerEditorStage.setOnCloseRequest(event -> isMovieSchedulerEditorAlreadyOpened = false);
                movieSchedulerEditorStage.show();
                isMovieSchedulerEditorAlreadyOpened = true;
            } catch (IOException ex) {
                throw new ApplicationException(ex);
            }
        }
    }

    private void createViewFromMovieSchedulesList(Schedule schedule) {
        Label scheduleLabel = new Label(schedule.getDate() + "   " +  schedule.getTime() + "   " + schedule.getHallName());
        scheduleLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        scheduleLabel.setTextFill(Color.WHITE);

        grigliaProgrammazione.setHgap(15);
        grigliaProgrammazione.setVgap(15);

        Label deleteIcon = new Label();
        deleteIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Bin.png")));
        deleteIcon.setTooltip(new Tooltip("Elimina"));
        GUIUtils.setFadeInOutOnControl(deleteIcon);

        AnchorPane pane = new AnchorPane();
        if(columnCount==1) {
            columnCount=0;
            rowCount++;
        }
        grigliaProgrammazione.add(pane, columnCount, rowCount);
        columnCount++;

        schedulerPanel.setContent(grigliaProgrammazione);
        GridPane.setMargin(pane, new Insets(5,5,5,5));

        deleteIcon.setLayoutY(scheduleLabel.getLayoutY());
        deleteIcon.setLayoutX(scheduleLabel.getLayoutX()+200);
        deleteIcon.setOnMouseClicked(e -> {
            Optional<ButtonType> option =
                    GUIUtils.showConfirmationAlert( "Attenzione"
                                                  , "Richiesta conferma:"
                                                  , "Sei sicuro di voler eliminare dalla lista questa programmazione?");
            if(option.orElse(null)==ButtonType.YES) {
                scheduleDao.deleteMovieSchedule(schedule);
                refreshUI();
            }
        });

        pane.getChildren().addAll(scheduleLabel, deleteIcon);
    }

    private void initMovieSchedulesList() {
        schedules.clear();
        actualSchedules.clear();
        schedules = scheduleDao.retrieveMovieSchedules();
        Collections.sort(schedules);
        for(Schedule ms : schedules) {
            if(ms.getMovieCode().equalsIgnoreCase(movie.getCodice())) {
                actualSchedules.add(ms);
            }
        }
    }

    void triggerNewScheduleEvent() { refreshUI(); }

    private void refreshUI() {
        grigliaProgrammazione.getChildren().clear();
        initScheduleGrid(movie);
    }

    @Override
    public void closeAllSubWindows() {
        if(movieSchedulerEditorStage != null) {
            if(movieSchedulerEditorStage.isShowing()) {
                isMovieSchedulerEditorAlreadyOpened = false;
                movieSchedulerEditorStage.close();
            }
        }
    }
}
