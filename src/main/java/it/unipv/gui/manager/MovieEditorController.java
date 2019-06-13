package it.unipv.gui.manager;

import java.io.File;
import java.util.Arrays;

import it.unipv.conversion.MovieToCSV;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.common.Movie;
import it.unipv.gui.common.MovieStatusTYPE;
import it.unipv.gui.common.MovieTYPE;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.ApplicationUtils;
import it.unipv.utils.DataReferences;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


public class MovieEditorController {

    @FXML TextField imgTextField;
    @FXML Button searchButton;
    @FXML TextField titleTextField;
    @FXML TextField genreTextField;
    @FXML TextField directionTextField;
    @FXML TextField castTextField;
    @FXML TextField timeTextField;
    @FXML TextField yearTextField;
    @FXML ComboBox movieTypeComboBox;
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
        initMovieTypeComboBox();
        GUIUtils.setScaleTransitionOnControl(saveButton);
        setFileChooser();
        setMaxCharToPlotTextArea();
        setTextfieldToNumericOnlyTextfield(timeTextField, yearTextField);
    }

    void init(Movie movie, ProgrammationPanelController programmationPanelController) {
        this.programmationPanelController = programmationPanelController;
        this.movie = movie;
        wasItAlreadyCreated = true;
        GUIUtils.setScaleTransitionOnControl(saveButton);
        initMovieTypeComboBox();
        setComponents();
        setFileChooser();
        setMaxCharToPlotTextArea();
        setTextfieldToNumericOnlyTextfield(timeTextField, yearTextField);
    }

    private void setMaxCharToPlotTextArea() {
        plotTextArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 1100 ? change : null));

    }

    private void setTextfieldToNumericOnlyTextfield(TextField... tf) {
        for(TextField t : tf) {
            t.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    t.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
    }

    void init(Movie movie, MovieListPanelController movieListPanelController) {
        this.movieListPanelController = movieListPanelController;
        this.movie = movie;
        wasItAlreadyCreated = true;
        GUIUtils.setScaleTransitionOnControl(saveButton);
        initMovieTypeComboBox();
        setComponents();
        setFileChooser();
    }

    private void initMovieTypeComboBox() {
        movieTypeComboBox.getItems().clear();
        movieTypeComboBox.setItems(FXCollections.observableList(Arrays.asList("2D", "3D")));
    }

    private void setComponents() {
        imgTextField.setText(movie.getLocandinaPath());
        titleTextField.setText(movie.getTitolo());
        genreTextField.setText(movie.getGenere());
        directionTextField.setText(movie.getRegia());
        castTextField.setText(movie.getCast());
        timeTextField.setText(movie.getDurata());
        yearTextField.setText(movie.getAnno());
        if(movie.getTipo().equals(MovieTYPE.TWOD)) {
            movieTypeComboBox.getSelectionModel().select("2D");
        } else {
            movieTypeComboBox.getSelectionModel().select("3D");
        }
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
         || yearTextField.getText().trim().equalsIgnoreCase("")
         || movieTypeComboBox.getValue() == null
         || plotTextArea.getText().trim().equalsIgnoreCase("")){
            GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore", "Devi compilare tutti i campi!");
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
            GUIUtils.showAlert(Alert.AlertType.INFORMATION, "Successo", "Operazione riuscita: ", "Salvataggio film riuscito con successo!");
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
        if(movieTypeComboBox.getValue().toString().equalsIgnoreCase("2D")) {
            m.setTipo(MovieTYPE.TWOD);
        } else if(movieTypeComboBox.getValue().toString().equalsIgnoreCase("3D")){
            m.setTipo(MovieTYPE.THREED);
        }

        if(!wasItAlreadyCreated) {
            m.setStatus(MovieStatusTYPE.AVAILABLE);
            m.setCodice(ApplicationUtils.getUUID());
        } else {
            m.setStatus(movie.getStatus());
            m.setCodice(movie.getCodice());
        }
        return m;
    }

}
