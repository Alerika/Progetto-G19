package it.unipv.gui.user.areariservata;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.unipv.conversion.CSVToPrenotationList;
import it.unipv.conversion.PrenotationToPDF;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.login.User;
import it.unipv.gui.user.Prenotation;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

public class PrenotationPanelController implements Initializable {

    private User user;
    private List<Prenotation> prenotations = new ArrayList<>();
    private static int rowCount = 0;
    private static int columnCount = 0;
    @FXML private GridPane grigliaProgrammazioni = new GridPane();
    @FXML private ScrollPane prenotationsPanel;
    @FXML private TextField searchBarTextfield;
    @FXML private Label searchButton;

    @Override public void initialize(URL url, ResourceBundle rb) { }

    public void init(User user) {
        this.user = user;
        GUIUtils.setScaleTransitionOnControl(searchButton);
        createProgrammationListGrid();
    }

    private void initProgrammationList() {
        List<Prenotation> x = CSVToPrenotationList.getPrenotationListFromCSV(DataReferences.PRENOTATIONSFILEPATH);
        for(Prenotation p : x) {
            if(p.getNomeUtente().equalsIgnoreCase(user.getName())) {
                prenotations.add(p);
            }
        }
        Collections.sort(prenotations);
    }

    private void createProgrammationListGrid() {
        grigliaProgrammazioni.getChildren().clear();
        GUIUtils.setScaleTransitionOnControl(searchButton);

        initProgrammationList();

        for (Prenotation p : prenotations) {
            createGridCellFromPrenotation(p);
        }

        initRowAndColumnCount();
    }

    private void createGridCellFromPrenotation(Prenotation p) {
        Label movieNameLabel = new Label(StringUtils.abbreviate(p.getNomeFilm(), 25));
        if(p.getNomeFilm().length()>25) {
            movieNameLabel.setTooltip(new Tooltip(p.getNomeFilm()));
        }
        movieNameLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        movieNameLabel.setTextFill(Color.WHITE);

        Label dayLabel = new Label(p.getGiornoFilm());
        dayLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        dayLabel.setTextFill(Color.WHITE);

        grigliaProgrammazioni.setHgap(15);
        grigliaProgrammazioni.setVgap(15);

        ImageView invoiceIconView = GUIUtils.getIconView(getClass().getResourceAsStream("/images/PDFIcon.png"));
        GUIUtils.setFadeInOutOnControl(invoiceIconView);

        AnchorPane pane = new AnchorPane();
        if(columnCount==1) {
            columnCount=0;
            rowCount++;
        }
        grigliaProgrammazioni.add(pane, columnCount, rowCount);
        columnCount++;

        prenotationsPanel.setContent(grigliaProgrammazioni);
        GridPane.setMargin(pane, new Insets(5,5,5,5));

        dayLabel.setLayoutY(movieNameLabel.getLayoutY());
        dayLabel.setLayoutX(movieNameLabel.getLayoutX()+250);

        invoiceIconView.setY(dayLabel.getLayoutY());
        invoiceIconView.setX(dayLabel.getLayoutX()+140);
        invoiceIconView.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documenti PDF", "*.pdf"));
            fileChooser.setInitialFileName( "Prenotazione "
                                          + p.getNomeFilm().replaceAll("[-+.^:,]","")
                                          + " - " + (p.getGiornoFilm()+p.getOraFilm()).replaceAll("[-+/.^:,]","")
                                          + " - " + p.getNomeUtente()
                                          + ".pdf");
            File f = fileChooser.showSaveDialog(null);

            try{
                if(f!=null) {
                    PrenotationToPDF.generatePDF(f.getPath(), "UTF-8", p);
                    GUIUtils.showAlert(Alert.AlertType.CONFIRMATION, "Conferma", "Operazione riuscita:", "Prenotazione correttamente salvata!\nPer pagare presentarsi con la fattura alla reception!");
                }
            } catch (Exception ex) {
                GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore durante la creazione del PDF: ", ex.getMessage());
                throw new ApplicationException(ex);
            }

        });


        pane.getChildren().addAll(movieNameLabel, dayLabel, invoiceIconView);
    }

    private void initRowAndColumnCount() {
        rowCount=0;
        columnCount=0;
    }

    private void refreshUI() {
        grigliaProgrammazioni.getChildren().clear();
        createProgrammationListGrid();
    }

    @FXML
    public void searchButtonListener() {
        String searchedString = searchBarTextfield.getText();
        if(searchedString!=null || searchedString.trim().equalsIgnoreCase("")) {
            grigliaProgrammazioni.getChildren().clear();
            for(Prenotation p : prenotations) {
                if( p.getNomeFilm().trim().toLowerCase().contains(searchedString.toLowerCase())
                 || p.getGiornoFilm().trim().toLowerCase().contains(searchedString.toLowerCase())){
                    createGridCellFromPrenotation(p);
                }
            }
            initRowAndColumnCount();
        } else {
            refreshUI();
        }
    }
}
