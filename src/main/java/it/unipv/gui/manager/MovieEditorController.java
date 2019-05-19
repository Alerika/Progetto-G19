package it.unipv.gui.manager;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import it.unipv.conversion.MovieToCSV;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.common.Movie;
import it.unipv.gui.common.MovieStatus;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.ApplicationUtils;
import it.unipv.utils.DataReferences;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.*;

public class MovieEditorController implements Initializable {

    @Override public void initialize(URL url, ResourceBundle rb) { }

    @FXML TextField imgTextField;
    @FXML Button searchButton;
    @FXML TextField titleTextField;
    @FXML TextField genreTextField;
    @FXML TextField directionTextField;
    @FXML TextField castTextField;
    @FXML TextField timeTextField;
    @FXML TextField yearTextField;
    @FXML TextArea plotTextArea;
    @FXML Label saveButton;
    private boolean wasItAlreadyCreated;
    private Movie movie;


    private ProgrammationPanelController programmationPanelController;
    private MovieListPanelController movieListPanelController;

    public MovieEditorController() {}

    void init(ProgrammationPanelController programmationPanelController) {
        this.programmationPanelController = programmationPanelController;
        wasItAlreadyCreated = false;
        GUIUtils.setScaleTransitionOnControl(saveButton);
        setFileChooser();
    }

    void init(Movie movie, ProgrammationPanelController programmationPanelController) {
        this.programmationPanelController = programmationPanelController;
        this.movie = movie;
        wasItAlreadyCreated = true;
        GUIUtils.setScaleTransitionOnControl(saveButton);
        setComponents();
        setFileChooser();
    }

    void init(Movie movie, MovieListPanelController movieListPanelController) {
        this.movieListPanelController = movieListPanelController;
        this.movie = movie;
        wasItAlreadyCreated = true;
        GUIUtils.setScaleTransitionOnControl(saveButton);
        setComponents();
        setFileChooser();
    }

    private void setComponents() {
        imgTextField.setText(movie.getLocandinaPath());
        titleTextField.setText(movie.getTitolo());
        genreTextField.setText(movie.getGenere());
        directionTextField.setText(movie.getRegia());
        castTextField.setText(movie.getCast());
        timeTextField.setText(movie.getDurata());
        yearTextField.setText(movie.getAnno());
        plotTextArea.setText(movie.getTrama());
    }

    private void setFileChooser() {
        searchButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagini JPEG", "*.jpg", "*.JPG", ".*JPEG"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagini PNG", "*.png", "*.PNG"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagini GIF", "*.gif", "*.GIF"));

            File file = fileChooser.showOpenDialog(null);
            if(file != null) {
                imgTextField.setText(file.getPath());
            }
        });
    }

    @FXML public void saveButtonListener() {
        if( imgTextField.getText().trim().equalsIgnoreCase("")
         || titleTextField.getText().trim().equalsIgnoreCase("")
         || genreTextField.getText().trim().equalsIgnoreCase("")
         || directionTextField.getText().trim().equalsIgnoreCase("")
         || castTextField.getText().trim().equalsIgnoreCase("")
         || timeTextField.getText().trim().equalsIgnoreCase("")
         || yearTextField.getText().trim().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(null, "Devi compilare tutti i campi!");
        } else {
            if(!wasItAlreadyCreated) {
                Movie m = getMovieFromTextFields();
                MovieToCSV.appendInfoMovieToCSV(m, DataReferences.MOVIEFILEPATH, true);
                programmationPanelController.triggerNewMovieEvent();
                wasItAlreadyCreated = true;
                movie = m;
            } else {
                if(programmationPanelController ==null) {
                    movieListPanelController.triggerOverwriteMovieEvent(getMovieFromTextFields());
                } else if(movieListPanelController==null) {
                    programmationPanelController.triggerOverwriteMovieEvent(getMovieFromTextFields());
                } else {
                    throw new ApplicationException("Unknown summoner!");
                }
            }
            JOptionPane.showMessageDialog(null, "Salvataggio film riuscito con successo!");
        }
    }

    private Movie getMovieFromTextFields() {
        Movie m = new Movie();
        m.setLocandinaPath(imgTextField.getText());
        m.setTitolo(titleTextField.getText());
        m.setGenere(genreTextField.getText());
        m.setRegia(directionTextField.getText());
        m.setCast(castTextField.getText());
        m.setDurata(timeTextField.getText());
        m.setAnno(yearTextField.getText());
        m.setTrama(plotTextArea.getText());
        if(!wasItAlreadyCreated) {
            m.setStatus(MovieStatus.AVAILABLE);
            m.setCodice(ApplicationUtils.getUUID());
        } else {
            m.setStatus(movie.getStatus());
            m.setCodice(movie.getCodice());
        }
        return m;
    }

}