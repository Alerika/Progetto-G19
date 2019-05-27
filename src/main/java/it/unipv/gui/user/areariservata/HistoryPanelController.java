package it.unipv.gui.user.areariservata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.unipv.conversion.CSVToMovieList;
import it.unipv.conversion.CSVToPrenotationList;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.common.Movie;
import it.unipv.gui.login.User;
import it.unipv.gui.user.Prenotation;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.ApplicationUtils;
import it.unipv.utils.CloseableUtils;
import it.unipv.utils.DataReferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class HistoryPanelController implements Initializable {

    private User loggedUser;
    private static int rowCount = 0;
    private static int columnCount = 0;
    private static int columnMax = 2;
    private List<Movie> movies = new ArrayList<>();
    private List<Prenotation> prenotations = new ArrayList<>();
    private GridPane grigliaFilm = new GridPane();
    @FXML private ScrollPane historyPanel;
    @FXML private TextField searchBarTextfield;
    @FXML private Label searchButton;

    @Override public void initialize(URL url, ResourceBundle rb) { }

    public void init(User loggedUser, double initialWidth) {
        this.loggedUser = loggedUser;
        GUIUtils.setScaleTransitionOnControl(searchButton);
        initMovieAndPrenotationList();
        columnMax = getColumnMaxFromPageWidth(initialWidth);
        createMovieGrid();
        checkPageDimension();
    }


    private void refreshUI() {
        grigliaFilm.getChildren().clear();
        createMovieGrid();
    }

    private int getColumnMaxFromPageWidth(double width) {
        if(width<800) {
            return 2;
        } else if(width>800 && width<=1360) {
            return 3;
        } else if(width>1360 && width<=1600) {
            return 4;
        } else if(width>1600) {
            return 5;
        } else {
            return 6;
        }
    }

    private int temp = 0;
    private void checkPageDimension() {
        Platform.runLater(() -> {
            Stage stage = (Stage) historyPanel.getScene().getWindow();
            stage.widthProperty().addListener(e -> {
                columnMax = getColumnMaxFromPageWidth(stage.getWidth());
                if (temp != columnMax) {
                    temp = columnMax;
                    refreshUI();
                }
            });
        });
    }

    private void initMovieAndPrenotationList() {
        List<Prenotation> x = CSVToPrenotationList.getPrenotationListFromCSV(DataReferences.PRENOTATIONSFILEPATH);
        for(Prenotation p : x) {
            if(p.getNomeUtente().equalsIgnoreCase(loggedUser.getName())) {
                prenotations.add(p);
            }
        }
        Collections.sort(prenotations);

        List<Movie> y = CSVToMovieList.getMovieListFromCSV(DataReferences.MOVIEFILEPATH);
        String temp = "";
        for(Movie m : y) {
            for(Prenotation p : prenotations) {
                if(m.getCodice().equalsIgnoreCase(p.getCodiceFilm()) && ApplicationUtils.checkIfDateIsPassed(p.getGiornoFilm())) {
                    if(!m.getTitolo().equalsIgnoreCase(temp)) {
                        movies.add(m);
                        temp = m.getTitolo();
                    }
                }
            }
        }
    }

    private void createMovieGrid() {
        grigliaFilm.getChildren().clear();

        for (Movie movie : movies) {
            createViewFromMoviesList(movie);
        }

        initRowAndColumnCount();
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


            Label oldestPrenotationIcon = new Label();
            oldestPrenotationIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            oldestPrenotationIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Schedule.png")));
            oldestPrenotationIcon.setTooltip(new Tooltip("Storico fatture"));
            GUIUtils.setFadeInOutOnControl(oldestPrenotationIcon);

            AnchorPane pane = new AnchorPane();
            if(columnCount==columnMax) {
                columnCount=0;
                rowCount++;
            }
            grigliaFilm.add(pane, columnCount, rowCount);
            columnCount++;

            historyPanel.setContent(grigliaFilm);
            GridPane.setMargin(pane, new Insets(15,0,5,15));

            posterPreview.setLayoutX(48);

            nomeFilmLabel.setLayoutY(posterPreview.getLayoutY()+215);

            oldestPrenotationIcon.setLayoutY(nomeFilmLabel.getLayoutY());
            oldestPrenotationIcon.setLayoutX(nomeFilmLabel.getLayoutX()+200);
            oldestPrenotationIcon.setOnMouseClicked(e -> {
                openOldestPrenotationWindow(movie);
            });

            pane.getChildren().addAll(posterPreview);
            pane.getChildren().addAll(nomeFilmLabel);
            pane.getChildren().addAll(oldestPrenotationIcon);

        } catch(FileNotFoundException ex) {
            throw new ApplicationException(ex);
        }
    }

    private void openOldestPrenotationWindow(Movie movie) {
        List<Prenotation> toInject = new ArrayList<>();
        for(Prenotation p : prenotations) {
            if(p.getCodiceFilm().equalsIgnoreCase(movie.getCodice())) {
                toInject.add(p);
            }
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/areariservata/OldestPrenotation.fxml"));
            Parent p = loader.load();
            OldestPrenotationController opc = loader.getController();
            opc.init(toInject);
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.setTitle("Storico prenotazioni " + movie.getTitolo());
            stage.show();
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        }
    }

    @FXML
    public void searchButtonListener() {
        String searchedString = searchBarTextfield.getText();
        if(searchedString!=null || searchedString.trim().equalsIgnoreCase("")) {
            grigliaFilm.getChildren().clear();
            for(Movie m : movies) {
                if( m.getTitolo().trim().toLowerCase().contains(searchedString.toLowerCase())){
                    createViewFromMoviesList(m);
                }
            }
            initRowAndColumnCount();
        } else {
            refreshUI();
        }
    }

    private void initRowAndColumnCount() {
        rowCount=0;
        columnCount=0;
    }

}
