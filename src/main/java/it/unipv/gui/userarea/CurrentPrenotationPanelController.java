package it.unipv.gui.userarea;

import java.io.File;
import java.util.*;

import it.unipv.DB.DBConnection;
import it.unipv.DB.PrenotationOperations;
import it.unipv.conversion.PrenotationToPDF;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.login.User;
import it.unipv.gui.prenotation.Prenotation;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.ApplicationUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

public class CurrentPrenotationPanelController {

    private User user;
    private List<Prenotation> prenotations = new ArrayList<>();
    private static int rowCount = 0;
    private static int columnCount = 0;
    private GridPane grigliaPrenotazioni = new GridPane();
    private PrenotationOperations prenotationOperations;
    @FXML private ScrollPane prenotationsPanel;
    @FXML private TextField searchBarTextfield;
    @FXML private Label searchButton;

    public void init(User user, DBConnection dbConnection) {
        this.user = user;
        this.prenotationOperations = new PrenotationOperations(dbConnection);
        GUIUtils.setScaleTransitionOnControl(searchButton);
        createPrenotationListGrid();
    }

    private void initPrenotationList() {
        prenotations.clear();
        List<Prenotation> x = prenotationOperations.retrievePrenotationList();
        for(Prenotation p : x) {
            if( p.getNomeUtente().equalsIgnoreCase(user.getName())
             && !ApplicationUtils.checkIfDateIsPassed(p.getGiornoFilm())) {
                prenotations.add(p);
            }
        }
        Collections.sort(prenotations);
    }

    private void createPrenotationListGrid() {
        grigliaPrenotazioni.getChildren().clear();
        GUIUtils.setScaleTransitionOnControl(searchButton);

        initPrenotationList();

        for (Prenotation p : prenotations) {
            createGridCellFromPrenotation(p);
        }

        initRowAndColumnCount();
    }

    private void createGridCellFromPrenotation(Prenotation p) {
        Label movieNameLabel = new Label(StringUtils.abbreviate(p.getNomeFilm(), 23));
        if(p.getNomeFilm().length()>23) {
            movieNameLabel.setTooltip(new Tooltip(p.getNomeFilm()));
        }
        movieNameLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        movieNameLabel.setTextFill(Color.WHITE);

        Label dayLabel = new Label(p.getGiornoFilm());
        dayLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        dayLabel.setTextFill(Color.WHITE);

        grigliaPrenotazioni.setHgap(15);
        grigliaPrenotazioni.setVgap(15);

        Label invoiceIcon = new Label();
        invoiceIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        invoiceIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/PDFIcon.png")));
        invoiceIcon.setTooltip(new Tooltip("Scarica fattura del " + p.getGiornoFilm()));
        GUIUtils.setFadeInOutOnControl(invoiceIcon);

        Label deleteIcon = new Label();
        deleteIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Bin.png")));
        deleteIcon.setTooltip(new Tooltip("Annulla la prenotazione del " + p.getGiornoFilm()));
        GUIUtils.setFadeInOutOnControl(deleteIcon);

        AnchorPane pane = new AnchorPane();
        if(columnCount==1) {
            columnCount=0;
            rowCount++;
        }
        grigliaPrenotazioni.add(pane, columnCount, rowCount);
        columnCount++;

        prenotationsPanel.setContent(grigliaPrenotazioni);
        GridPane.setMargin(pane, new Insets(5,5,5,5));

        dayLabel.setLayoutY(movieNameLabel.getLayoutY());
        dayLabel.setLayoutX(movieNameLabel.getLayoutX()+250);

        invoiceIcon.setLayoutY(dayLabel.getLayoutY());
        invoiceIcon.setLayoutX(dayLabel.getLayoutX()+140);
        invoiceIcon.setOnMouseClicked(event -> doSaveInvoicePDF(p));

        deleteIcon.setLayoutY(dayLabel.getLayoutY());
        deleteIcon.setLayoutX(invoiceIcon.getLayoutX()+40);
        deleteIcon.setOnMouseClicked(event -> doDeletePrenotation(p));



        pane.getChildren().addAll(movieNameLabel, dayLabel, invoiceIcon, deleteIcon);
    }

    private void doDeletePrenotation(Prenotation toDelete) {
        Optional<ButtonType> option =
                GUIUtils.showConfirmationAlert( "Attenzione"
                                              , "Richiesta conferma:"
                                              , "Sei sicuro di voler annullare la prenotazione de " + toDelete.getNomeFilm() + " del giorno " + toDelete.getGiornoFilm() + "?");
        if(option.orElse(null)==ButtonType.YES) {
            prenotationOperations.deletePrenotation(toDelete);
            refreshUI();
        }
    }

    private void doSaveInvoicePDF(Prenotation p) {
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
    }

    private void initRowAndColumnCount() {
        rowCount=0;
        columnCount=0;
    }

    private void refreshUI() { createPrenotationListGrid(); }

    @FXML
    public void searchButtonListener() {
        String searchedString = searchBarTextfield.getText();
        if(searchedString!=null) {
            grigliaPrenotazioni.getChildren().clear();
            for(Prenotation p : prenotations) {
                if(p.getNomeFilm().trim().toLowerCase().contains(searchedString.toLowerCase())){
                    createGridCellFromPrenotation(p);
                }
            }
            initRowAndColumnCount();
        } else {
            refreshUI();
        }
    }
}
