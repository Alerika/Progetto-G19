package it.unipv.gui.manager;

import it.unipv.utils.ApplicationException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class ManagerHomeController implements Initializable {

    @FXML private Rectangle rectangleOptions;
    
    @FXML private AnchorPane mainPanel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rectangleOptions.setVisible(false);
    } 

    private String openedPane = "";

    @FXML
    public void filterByOptions(MouseEvent event){
        Label label = (Label)event.getSource();

        TranslateTransition transition = new TranslateTransition(javafx.util.Duration.seconds(0.2), rectangleOptions);
        Timeline timelineWidth = new Timeline(
                new KeyFrame( javafx.util.Duration.seconds(0.3)
                            , new KeyValue(rectangleOptions.heightProperty(), label.getHeight())));
        transition.setToY(label.getLayoutY());
        if(!rectangleOptions.isVisible()){
            rectangleOptions.setVisible(true);
            rectangleOptions.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.seconds(0.4), rectangleOptions);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
        timelineWidth.play();
        transition.play();


        switch(label.getText()) {
            case "MODIFICA SALE": {
                try {
                    if(!openedPane.equals("MODIFICA SALE")) {
                        mainPanel.getChildren().clear();
                        AnchorPane hallPanell = FXMLLoader.load(getClass().getResource("/fxml/manager/HallPanel.fxml"));
                        hallPanell.prefHeightProperty().bind(mainPanel.heightProperty());
                        hallPanell.prefWidthProperty().bind(mainPanel.widthProperty());
                        mainPanel.getChildren().setAll(hallPanell);
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
                        mainPanel.getChildren().clear();
                        AnchorPane programmationPanel = FXMLLoader.load(getClass().getResource("/fxml/manager/ProgrammationPanel.fxml"));
                        programmationPanel.prefHeightProperty().bind(mainPanel.heightProperty());
                        programmationPanel.prefWidthProperty().bind(mainPanel.widthProperty());
                        mainPanel.getChildren().setAll(programmationPanel);
                        openedPane = "PROGRAMMAZIONE";
                    }
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
                break;

            case "LISTA FILM":
                try {
                    if(!openedPane.equals("LISTA FILM")) {
                        mainPanel.getChildren().clear();
                        AnchorPane movieListPanel = FXMLLoader.load(getClass().getResource("/fxml/manager/MovieListPanel.fxml"));
                        movieListPanel.prefHeightProperty().bind(mainPanel.heightProperty());
                        movieListPanel.prefWidthProperty().bind(mainPanel.widthProperty());
                        mainPanel.getChildren().setAll(movieListPanel);
                        openedPane = "LISTA FILM";
                    }
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
                break;

            case "LISTA UTENTI":
                try {
                    if(!openedPane.equals("LISTA UTENTI")){
                        mainPanel.getChildren().clear();
                        AnchorPane userListPanel = FXMLLoader.load(getClass().getResource("/fxml/manager/UserListPanel.fxml"));
                        userListPanel.prefHeightProperty().bind(mainPanel.heightProperty());
                        userListPanel.prefWidthProperty().bind(mainPanel.widthProperty());
                        mainPanel.getChildren().setAll(userListPanel);
                        openedPane = "LISTA UTENTI";
                    }
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
                break;

            case "MODIFICA PREZZI":
                try {
                    if(!openedPane.equals("MODIFICA PREZZI")){
                        mainPanel.getChildren().clear();
                        AnchorPane modifyPricesPanel = FXMLLoader.load(getClass().getResource("/fxml/manager/PricesPanel.fxml"));
                        modifyPricesPanel.prefHeightProperty().bind(mainPanel.heightProperty());
                        modifyPricesPanel.prefWidthProperty().bind(mainPanel.widthProperty());
                        mainPanel.getChildren().setAll(modifyPricesPanel);
                        openedPane = "MODIFICA PREZZI";
                    }
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
                break;

            case "ESCI":
                System.exit(0);
                break;

            default:
                break;
        }
    }
}
