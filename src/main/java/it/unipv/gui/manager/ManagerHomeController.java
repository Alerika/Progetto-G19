package it.unipv.gui.manager;

import it.unipv.DB.DBConnection;
import it.unipv.gui.common.ICloseablePane;
import it.unipv.gui.home.HomeController;
import it.unipv.utils.ApplicationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ManagerHomeController {

    @FXML private BorderPane mainPanel;
    @FXML Label hallModifierLabel, schedulerLabel, movieListLabel, userListLabel, pricesModifierLabel, exitLabel;
    private List<Label> labels = new ArrayList<>();
    private HomeController homeController;
    private List<ICloseablePane> iCloseablePanes = new ArrayList<>();
    private DBConnection dbConnection;

    public void init(HomeController homeController, DBConnection dbConnection) {
        this.homeController = homeController;
        this.dbConnection = dbConnection;
        addLabelsToList();
        setOnMouseEnteredToLabels();
        setOnMouseExitedToLabels();
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
    public void filterByOptions(MouseEvent event){
        Label label = (Label)event.getSource();

        switch(label.getText()) {
            case "MODIFICA SALE": {
                try {
                    if(!openedPane.equals("MODIFICA SALE")) {
                        hallModifierLabel.setStyle("-fx-background-color:#db8f00");
                        setTransparentOtherLabels("MODIFICA SALE");
                        mainPanel.getChildren().clear();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manager/HallPanel.fxml"));
                        AnchorPane hallPanel = loader.load();
                        HallPanelController hpc = loader.getController();
                        hpc.init(this, mainPanel.getWidth(), dbConnection);
                        mainPanel.setCenter(hallPanel);
                        openedPane = "MODIFICA SALE";
                        if(!iCloseablePanes.contains(hpc)) { iCloseablePanes.add(hpc); }
                    }
                } catch (IOException ex) {
                    throw new ApplicationException(ex);
                }
                break;
            }

            case "PROGRAMMAZIONE":
                try {
                    if(!openedPane.equals("PROGRAMMAZIONE")) {
                        schedulerLabel.setStyle("-fx-background-color:#db8f00");
                        setTransparentOtherLabels("PROGRAMMAZIONE");
                        mainPanel.getChildren().clear();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manager/ProgrammationPanel.fxml"));
                        AnchorPane programmationPanel = loader.load();
                        ProgrammationPanelController ppc = loader.getController();
                        ppc.init(this, mainPanel.getWidth(), dbConnection);
                        mainPanel.setCenter(programmationPanel);
                        openedPane = "PROGRAMMAZIONE";
                        if(!iCloseablePanes.contains(ppc)) { iCloseablePanes.add(ppc); }
                    }
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
                break;

            case "LISTA FILM":
                try {
                    if(!openedPane.equals("LISTA FILM")) {
                        movieListLabel.setStyle("-fx-background-color:#db8f00");
                        setTransparentOtherLabels("LISTA FILM");
                        mainPanel.getChildren().clear();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manager/MovieListPanel.fxml"));
                        AnchorPane movieListPanel = loader.load();
                        MovieListPanelController mlpc = loader.getController();
                        mlpc.init(this, dbConnection);
                        mainPanel.setCenter(movieListPanel);
                        openedPane = "LISTA FILM";
                        if(!iCloseablePanes.contains(mlpc)) { iCloseablePanes.add(mlpc); }
                    }
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
                break;

            case "LISTA UTENTI":
                try {
                    if(!openedPane.equals("LISTA UTENTI")){
                        userListLabel.setStyle("-fx-background-color:#db8f00");
                        setTransparentOtherLabels("LISTA UTENTI");
                        mainPanel.getChildren().clear();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manager/UserListPanel.fxml"));
                        AnchorPane userListPanel = loader.load();
                        UserListPanelController ulpc = loader.getController();
                        ulpc.init(dbConnection);
                        mainPanel.setCenter(userListPanel);
                        openedPane = "LISTA UTENTI";
                    }
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
                break;

            case "MODIFICA PREZZI":
                try {
                    if(!openedPane.equals("MODIFICA PREZZI")){
                        pricesModifierLabel.setStyle("-fx-background-color:#db8f00");
                        setTransparentOtherLabels("MODIFICA PREZZI");
                        mainPanel.getChildren().clear();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manager/PricesPanel.fxml"));
                        AnchorPane modifyPricesPanel = loader.load();
                        PricesPanelController ppc = loader.getController();
                        ppc.init(dbConnection);
                        mainPanel.setCenter(modifyPricesPanel);
                        openedPane = "MODIFICA PREZZI";
                        if(!iCloseablePanes.contains(ppc)) { iCloseablePanes.add(ppc); }
                    }
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
                break;

            case "ESCI":
                Stage stage = (Stage) mainPanel.getScene().getWindow();
                stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                closeAllSubWindows();
                stage.close();
                break;

            default:
                break;
        }
    }

    public void closeAllSubWindows() {
        for(ICloseablePane i : iCloseablePanes) {
            i.closeAllSubWindows();
        }
    }

    private void setTransparentOtherLabels(String nameToExclude) {
        for(Label l : labels) {
            if(!l.getText().toLowerCase().equalsIgnoreCase(nameToExclude.toLowerCase())) {
                l.setStyle("-fx-background-color:transparent");
            }
        }
    }

    void triggerToHomeNewMovieEvent() { homeController.triggerNewMovieEvent(); }
    void triggerToHomeNewHallEvent() { homeController.triggerNewHallEvent(); }
}
