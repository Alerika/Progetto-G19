package it.unipv.gui.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unipv.conversion.PrenotationToPDF;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.prenotation.Prenotation;
import it.unipv.utils.ApplicationException;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class OldestPrenotationController {

    private List<Prenotation> prenotations = new ArrayList<>();
    @FXML private ScrollPane prenotationPanel;
    @FXML private Label closeButton;
    private GridPane grigliaPrenotazioni = new GridPane();
    private static int rowCount = 0;
    private static int columnCount = 0;

    public void init(List<Prenotation> prenotations) {
        this.prenotations = prenotations;
        Collections.sort(prenotations);
        GUIUtils.setScaleTransitionOnControl(closeButton);
        createPrenotationListGrid();
    }

    private void createPrenotationListGrid() {
        grigliaPrenotazioni.getChildren().clear();

        for (Prenotation p : prenotations) {
            createGridCellFromPrenotation(p);
        }

        initRowAndColumnCount();
    }

    private void initRowAndColumnCount() {
        rowCount=0;
        columnCount=0;
    }

    private void createGridCellFromPrenotation(Prenotation p) {
        Label dayLabel = new Label(p.getGiornoFilm());
        dayLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        dayLabel.setTextFill(Color.WHITE);

        grigliaPrenotazioni.setHgap(15);
        grigliaPrenotazioni.setVgap(15);

        Label invoceIcon = new Label();
        invoceIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        invoceIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/PDFIcon.png")));
        invoceIcon.setTooltip(new Tooltip("Scarica fattura del " + p.getGiornoFilm()));
        GUIUtils.setFadeInOutOnControl(invoceIcon);

        AnchorPane pane = new AnchorPane();
        if(columnCount==1) {
            columnCount=0;
            rowCount++;
        }
        grigliaPrenotazioni.add(pane, columnCount, rowCount);
        columnCount++;

        prenotationPanel.setContent(grigliaPrenotazioni);
        GridPane.setMargin(pane, new Insets(5,5,5,5));


        invoceIcon.setLayoutY(dayLabel.getLayoutY());
        invoceIcon.setLayoutX(dayLabel.getLayoutX()+140);
        invoceIcon.setOnMouseClicked(event -> {
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


        pane.getChildren().addAll(dayLabel, invoceIcon);
    }

    @FXML
    public void doClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }
}
