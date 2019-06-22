package it.unipv.gui.managerarea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.unipv.DB.DBConnection;
import it.unipv.DB.HallOperations;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.common.ICloseablePane;
import it.unipv.utils.ApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;

public class HallPanelController implements ICloseablePane {

    @FXML ScrollPane hallPanel;
    @FXML Label nuovaSalaButton;
    private GridPane grigliaSale = new GridPane();
    private static int rowCount = 0;
    private static int columnCount = 0;
    private int columnMax = 3;
    private List<String> hallNames = new ArrayList<>();
    private List<Image> previews = new ArrayList<>();
    private int hallNamesSize = 0;
    private ManagerHomeController managerHomeController;
    private HallEditor hallEditor;
    private HallOperations ho;
    private DBConnection dbConnection;

    public void init(ManagerHomeController managerHomeController, double initialWidth, DBConnection dbConnection) {
        this.managerHomeController = managerHomeController;
        this.dbConnection = dbConnection;
        ho = new HallOperations(dbConnection);
        initHallNameList();
        initPreview();
        columnMax = getColumnMaxFromPageWidth(initialWidth);
        Platform.runLater(this::createHallGrid);

        checkPageDimension();
    }

    private void initHallNameList() {
        hallNames = ho.retrieveHallNames();
        Collections.sort(hallNames);
        hallNamesSize = hallNames.size();
    }

    private void initPreview() {
        previews.clear();
        for(int i = 0; i<hallNamesSize; i++) {
            previews.add(ho.retrieveHallPreviewAsImage(hallNames.get(i), 150, 0, true, true));
        }
    }

    private void createHallGrid() {
        grigliaSale.getChildren().clear();

        for(int i = 0; i<hallNamesSize; i++) {
            createViewFromPreviews(hallNames.get(i), previews.get(i));
        }

        GUIUtils.setScaleTransitionOnControl(nuovaSalaButton);

        rowCount = 0;
        columnCount = 0;
    }

    private void createViewFromPreviews(String hallName, Image preview) {
        Label nomeSalaLabel = new Label(FilenameUtils.removeExtension(hallName));
        nomeSalaLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        hallNames.add(nomeSalaLabel.getText());
        nomeSalaLabel.setTextFill(Color.WHITE);

        grigliaSale.setHgap(80);
        grigliaSale.setVgap(80);

        ImageView snapHallView = new ImageView(preview);
        snapHallView.setFitWidth(150);

        Label deleteIcon = new Label();
        deleteIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Bin.png")));
        deleteIcon.setTooltip(new Tooltip("Elimina " + nomeSalaLabel.getText().trim()));
        GUIUtils.setFadeInOutOnControl(deleteIcon);

        Label renameIcon = new Label();
        renameIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        renameIcon.setGraphic(GUIUtils.getIconView(getClass().getResourceAsStream("/images/Edit.png")));
        renameIcon.setTooltip(new Tooltip("Rinomina " + nomeSalaLabel.getText().trim()));
        GUIUtils.setFadeInOutOnControl(renameIcon);

        AnchorPane pane = new AnchorPane();
        if (columnCount == columnMax) {
            columnCount = 0;
            rowCount++;
        }
        grigliaSale.add(pane, columnCount, rowCount);
        columnCount++;

        hallPanel.setContent(grigliaSale);
        GridPane.setMargin(pane, new Insets(15, 0, 0, 15));

        nomeSalaLabel.setLayoutY(snapHallView.getLayoutY() + 100);

        deleteIcon.setLayoutY(nomeSalaLabel.getLayoutY() - 2);
        deleteIcon.setLayoutX(nomeSalaLabel.getLayoutX() + 126);

        renameIcon.setLayoutY(nomeSalaLabel.getLayoutY() - 2);
        renameIcon.setLayoutX(nomeSalaLabel.getLayoutX() + 93);

        pane.getChildren().addAll(snapHallView, nomeSalaLabel, deleteIcon, renameIcon);

        snapHallView.setOnMouseClicked(event -> {
            hallEditor = new HallEditor(nomeSalaLabel.getText(), this, true, dbConnection);
            hallEditor.setAlwaysOnTop(true);
        });

        GUIUtils.setScaleTransitionOnControl(snapHallView);
        renameIcon.setOnMouseClicked(event -> renameHall(nomeSalaLabel.getText(), nomeSalaLabel, renameIcon, deleteIcon));
        deleteIcon.setOnMouseClicked(event -> removeHall(nomeSalaLabel.getText()));
    }

    private void removeHall(String hallName) {
        Optional<ButtonType> option =
                GUIUtils.showConfirmationAlert( "Attenzione"
                                              , "Richiesta conferma:"
                                              , "Vuoi davvero eliminare la piantina " + hallName + "?");
        if(option.orElse(null)==ButtonType.YES) {
            ho.removeHallAndPreview(hallName);
            initHallNameList();
            initPreview();
            managerHomeController.triggerToHomeNewHallEvent();
            refreshUIandHallList();
        }
    }

    private void renameHall(String hallName, Label labelToModify, Label renameIcon, Label deleteIcon) {
        String newHallName = GUIUtils.showInputAlert("Rinomina Sala", "Rinomina " + hallName, "Inserisci il nuovo nome della sala").orElse(null);
        if(newHallName!=null) {
            if(!newHallName.trim().equalsIgnoreCase("")) {
                if(checkIfItIsFree(newHallName)) {
                        labelToModify.setText(newHallName);
                        renameIcon.setTooltip(new Tooltip("Rinomina " + newHallName));
                        deleteIcon.setTooltip(new Tooltip("Elimina " + newHallName));

                        ho.renameHallAndPreview(hallName, newHallName);
                        initHallNameList();
                        initPreview();

                        managerHomeController.triggerToHomeNewHallEvent();
                        GUIUtils.showAlert(Alert.AlertType.INFORMATION, "Informazione", "Operazione riuscita: ", "Sala rinominata con successo!");
                } else {
                    GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Esiste già una sala con questo nome!");
                }
            } else {
                GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Devi compilare il campo!");
            }
        }
    }

    private boolean checkIfItIsFree(String name) {
        boolean status = true;
        for(String s : hallNames) {
            if(name.trim().equalsIgnoreCase(s)) {
                status = false;
                break;
            }
        }
        return status;
    }

    @FXML public void newHallListener() {
        String nomeSala = GUIUtils.showInputAlert("Nuova Sala", "Stai creando una nuova sala:", "Inserisci il nome della sala").orElse(null);
        if(nomeSala!=null) {
            if(nomeSala.equalsIgnoreCase("") || nomeSala.trim().length()==0) {
                GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Devi inserire un nome!");
            } else if(!nomeSala.equalsIgnoreCase("")) {
                if(checkIfItIsFree(nomeSala)) {
                    Optional<ButtonType> option =
                            GUIUtils.showConfirmationAlert( "Attenzione"
                                                          , "Richiesta conferma:"
                                                          , "Vuoi creare una griglia preimpostata?");
                    if(option.orElse(null)==ButtonType.NO) {
                        hallEditor = new HallEditor(nomeSala, this, false, dbConnection);
                        hallEditor.setAlwaysOnTop(true);
                    } else {
                        Optional<Pair<String, String>> dialogMenu = configureRowAndColumnDialogRequest();
                        dialogMenu.ifPresent(rowsAndcolumns -> {
                            int rows =  Integer.parseInt(rowsAndcolumns.getKey());
                            int columns = Integer.parseInt(rowsAndcolumns.getValue());

                            if(rows<27) {
                                hallEditor = new HallEditor(nomeSala, this, rows, columns, dbConnection);
                                hallEditor.setAlwaysOnTop(true);
                            } else {
                                GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Numero massimo di righe 26!");
                            }
                        });
                    }
                } else {
                    GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Esiste già una sala con questo nome!");
                }
            }
        }
    }

    private Optional<Pair<String, String>> configureRowAndColumnDialogRequest() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Nuova sala");
        dialog.setHeaderText("Inserisci numero di righe e colonne");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Stage s = (Stage) dialog.getDialogPane().getScene().getWindow();
        s.getIcons().add(new Image(GUIUtils.class.getResourceAsStream("/images/GoldenMovieStudioIcon.png")));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField rows = new TextField();
        makeTextFieldFillableByOnlyDigits(rows);
        rows.setPromptText("0");
        TextField columns = new PasswordField();
        makeTextFieldFillableByOnlyDigits(columns);
        columns.setPromptText("0");

        grid.add(new Label("Righe:"), 0, 0);
        grid.add(rows, 1, 0);
        grid.add(new Label("Colonne:"), 0, 1);
        grid.add(columns, 1, 1);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(rows::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(rows.getText(), columns.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void makeTextFieldFillableByOnlyDigits(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    void triggerModificationToHallList() {
        refreshUIandHallList();
        managerHomeController.triggerToHomeNewHallEvent();
    }

    private void refreshUIandHallList() {
        initHallNameList();
        initPreview();
        createHallGrid();
    }

    private void refreshUI() {
        createHallGrid();
    }

    private int temp = 0;
    private void checkPageDimension() {
        Platform.runLater(() -> {
            Stage stage = (Stage) nuovaSalaButton.getScene().getWindow();
            stage.widthProperty().addListener(e -> {
                columnMax = getColumnMaxFromPageWidth(stage.getWidth());
                if (temp != columnMax) {
                    temp = columnMax;
                    refreshUI();
                }
            });
        });
    }

    //Supporta fino ai 1080p
    private int getColumnMaxFromPageWidth(double width) {
        if(width<800) {
            return 2;
        } else if(width>=800 && width<=1200) {
            return 3;
        } else if(width>1200 && width<=1500) {
            return 4;
        } else if(width>1500 && width<=1700) {
            return 5;
        } else if(width>1700){
            return 6;
        } else {
            throw new ApplicationException("Impossibile settare numero colonne per width: " + width);
        }
    }

    @Override
    public void closeAllSubWindows() {
        if(hallEditor!=null) {
            hallEditor.dispose();
        }
    }
}
