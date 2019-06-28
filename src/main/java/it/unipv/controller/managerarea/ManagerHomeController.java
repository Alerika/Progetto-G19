package it.unipv.controller.managerarea;

import it.unipv.controller.common.*;
import it.unipv.db.DBConnection;
import it.unipv.utils.ApplicationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.unipv.utils.DataReferences;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class ManagerHomeController implements IManagerAreaTrigger, IManagerAreaInitializer {

    @FXML private BorderPane mainPanel;
    @FXML private Label hallModifierLabel, schedulerLabel, movieListLabel, userListLabel, pricesModifierLabel, exitLabel, statusLabel, animatedTipsLabel;
    @FXML private ProgressBar statusPBar;
    private List<Label> labels = new ArrayList<>();
    private IHomeTrigger homeController;
    private List<ICloseablePane> iCloseablePanes = new ArrayList<>();
    private DBConnection dbConnection;
    private Thread animatedTipsThread;

    @Override
    public void init(IHomeTrigger homeController, DBConnection dbConnection) {
        this.homeController = homeController;
        this.dbConnection = dbConnection;
        addLabelsToList();
        setOnMouseEnteredToLabels();
        setOnMouseExitedToLabels();

        statusLabel.setVisible(false);
        statusPBar.setVisible(false);
        animateTipsLabel();
    }

    private void animateTipsLabel() {
        animatedTipsThread = GUIUtils.getTipsThread(DataReferences.MANAGERAREATIPS, animatedTipsLabel, 5000);
        animatedTipsThread.start();
    }

    private void addLabelsToList() {
        labels.add(hallModifierLabel);
        labels.add(schedulerLabel);
        labels.add(movieListLabel);
        labels.add(userListLabel);
        labels.add(pricesModifierLabel);
        labels.add(exitLabel);
    }

    private String openedPane = "";

    private void setOnMouseEnteredToLabels() {
        for(Label l : labels) {
            l.setOnMouseEntered(e -> {
                l.setCursor(Cursor.HAND);
                if(!openedPane.toLowerCase().equalsIgnoreCase(l.getText().toLowerCase())) {
                    l.setStyle("-fx-background-color:#efc677;");
                }
            });
        }

    }

    private void setOnMouseExitedToLabels() {
        for(Label l : labels) {
            l.setCursor(Cursor.DEFAULT);
            l.setOnMouseExited(e -> {
                if(!openedPane.toLowerCase().equalsIgnoreCase(l.getText().toLowerCase())) {
                    l.setStyle("-fx-background-color:transparent;");
                }
            });
        }
    }

    @FXML
    private void filterByOptions(MouseEvent event){
        Label label = (Label)event.getSource();

        switch(label.getText()) {
            case "MODIFICA SALE": {
                openHallPanel();
                break;
            }

            case "PROGRAMMAZIONE":
                openProgrammationPanel();
                break;

            case "LISTA FILM":
                openMovieListPanel();
                break;

            case "LISTA UTENTI":
                openUserListPanel();
                break;

            case "MODIFICA PREZZI":
                openPricesPanel();
                break;

            case "ESCI":
                doExit();
                break;

            default:
                break;
        }
    }

    private void openHallPanel() {
        if(!openedPane.equals("MODIFICA SALE")) {
            HallPanelController hpc = openNewPanel("MODIFICA SALE", hallModifierLabel, "/fxml/managerarea/HallPanel.fxml").getController();
            hpc.init(this, mainPanel.getWidth(), dbConnection);
            if(!iCloseablePanes.contains(hpc)) { iCloseablePanes.add(hpc); }
        }
    }

    private void openProgrammationPanel() {
        if(!openedPane.equals("PROGRAMMAZIONE")) {
            ProgrammationPanelController ppc = openNewPanel("PROGRAMMAZIONE", schedulerLabel, "/fxml/managerarea/ProgrammationPanel.fxml").getController();
            ppc.init(this, mainPanel.getWidth(), dbConnection);
            if(!iCloseablePanes.contains(ppc)) { iCloseablePanes.add(ppc); }
        }
    }

    private void openMovieListPanel() {
        if(!openedPane.equals("LISTA FILM")) {
            MovieListPanelController mlpc = openNewPanel("LISTA FILM", movieListLabel, "/fxml/managerarea/MovieListPanel.fxml").getController();
            mlpc.init(this, dbConnection);
            if(!iCloseablePanes.contains(mlpc)) { iCloseablePanes.add(mlpc); }
        }
    }

    private void openUserListPanel() {
        if(!openedPane.equals("LISTA UTENTI")){
            UserListPanelController ulpc = openNewPanel("LISTA UTENTI", userListLabel, "/fxml/managerarea/UserListPanel.fxml").getController();
            ulpc.init(this, dbConnection);
        }
    }

    private void openPricesPanel() {
        if(!openedPane.equals("MODIFICA PREZZI")){
            PricesPanelController ppc = openNewPanel("MODIFICA PREZZI", pricesModifierLabel, "/fxml/managerarea/PricesPanel.fxml").getController();
            ppc.init(this, dbConnection);
        }
    }

    private void doExit() {
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        closeAllSubWindows();
        stage.close();
    }

    private FXMLLoader openNewPanel(String name, Label label, String fxmlpath) {
        try {
            label.setStyle("-fx-background-color:#db8f00");
            setTransparentOtherLabels(name);
            mainPanel.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
            AnchorPane pane = loader.load();
            mainPanel.setCenter(pane);
            openedPane = name;
            return loader;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public void closeAllSubWindows() {
        for(ICloseablePane i : iCloseablePanes) {
            i.closeAllSubWindows();
        }
        if(animatedTipsThread != null) { animatedTipsThread.interrupt(); }
    }

    private void setTransparentOtherLabels(String nameToExclude) {
        for(Label l : labels) {
            if(!l.getText().toLowerCase().equalsIgnoreCase(nameToExclude.toLowerCase())) {
                l.setStyle("-fx-background-color:transparent");
            }
        }
    }

    @Override public void triggerToHomeNewMovieEvent() { homeController.triggerNewMovieEvent(); }
    @Override public void triggerToHomeNewHallEvent() { homeController.triggerNewHallEvent(); }

    @Override
    public void triggerStartStatusEvent(String text) {
        statusLabel.setVisible(true);
        statusPBar.setVisible(true);
        statusLabel.setText(text);
        statusPBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    }

    private static Timeline timeline;
    @Override
    public void triggerEndStatusEvent(String text) {
        if(timeline!=null) { timeline.stop(); }

        KeyFrame kf1 = new KeyFrame(Duration.millis(100), event -> {
            statusLabel.setText(text);
            statusPBar.setProgress(100);
        });

        KeyFrame kf2 = new KeyFrame(Duration.seconds(4), e -> {
            statusLabel.setVisible(false);
            statusPBar.setVisible(false);
        });

        timeline = new Timeline(kf1, kf2);

        Platform.runLater(timeline::play);
    }
}
