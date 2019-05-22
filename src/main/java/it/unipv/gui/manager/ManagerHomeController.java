package it.unipv.gui.manager;

import it.unipv.utils.ApplicationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ManagerHomeController implements Initializable {

    @FXML private Rectangle rectangleOptions;
    @FXML private BorderPane mainPanel;
    @FXML Label hallModifierLabel, schedulerLabel, movieListLabel, userListLabel, pricesModifierLabel, exitLabel;
    private List<Label> labels = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rectangleOptions.setVisible(false);
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

    public void setOnMouseEnteredToLabels() {
        for(Label l : labels) {
            l.setOnMouseEntered(e -> {
                l.setCursor(Cursor.HAND);
                if(!openedPane.toLowerCase().equalsIgnoreCase(l.getText().toLowerCase())) {
                    l.setStyle("-fx-background-color:#efc677;");
                }
            });
        }

    }

    public void setOnMouseExitedToLabels() {
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
                        hpc.init(mainPanel.getWidth());
                        mainPanel.setCenter(hallPanel);
                        openedPane = "MODIFICA SALE";
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
                        ppc.init(mainPanel.getWidth());
                        mainPanel.setCenter(programmationPanel);
                        openedPane = "PROGRAMMAZIONE";
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
                        AnchorPane movieListPanel = FXMLLoader.load(getClass().getResource("/fxml/manager/MovieListPanel.fxml"));
                        mainPanel.setCenter(movieListPanel);
                        openedPane = "LISTA FILM";
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
                        AnchorPane userListPanel = FXMLLoader.load(getClass().getResource("/fxml/manager/UserListPanel.fxml"));
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
                        AnchorPane modifyPricesPanel = FXMLLoader.load(getClass().getResource("/fxml/manager/PricesPanel.fxml"));
                        mainPanel.setCenter(modifyPricesPanel);
                        openedPane = "MODIFICA PREZZI";
                    }
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
                break;

            case "ESCI":
                Stage stage = (Stage) mainPanel.getScene().getWindow();
                stage.close();
                break;

            default:
                break;
        }
    }

    private void setTransparentOtherLabels(String nameToExclude) {
        for(Label l : labels) {
            if(!l.getText().toLowerCase().equalsIgnoreCase(nameToExclude.toLowerCase())) {
                l.setStyle("-fx-background-color:transparent");
            }
        }
    }
}
