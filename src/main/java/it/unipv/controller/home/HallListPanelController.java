package it.unipv.controller.home;

import it.unipv.db.DBConnection;
import it.unipv.dao.HallDao;
import it.unipv.dao.HallDaoImpl;
import it.unipv.controller.common.GUIUtils;
import it.unipv.controller.common.ICloseablePane;
import it.unipv.model.Seat;
import it.unipv.model.SeatTYPE;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HallListPanelController implements ICloseablePane {

    @FXML private ScrollPane hallPanel;
    private HallDao hallDao;
    private List<String> hallNames = new ArrayList<>();
    private List<Image> previews = new ArrayList<>();
    private int hallNamesSize = 0;
    private static int hallRowCount = 0;
    private static int hallColumnCount = 0;
    private static int columnMax = 0;
    private GridPane grigliaSale = new GridPane();
    private Stage hallPreviewStage;

    public void init(double initialWidth, DBConnection dbConnection) {
        this.hallDao = new HallDaoImpl(dbConnection);
        initHallNameList();
        initPreview();
        columnMax = getColumnMaxFromPageWidth(initialWidth);
        Platform.runLater(this::initHallGrid);
        checkPageDimension();
    }

    private void initHallNameList() {
        hallNames = hallDao.retrieveHallNames();
        Collections.sort(hallNames);
        hallNamesSize = hallNames.size();
    }

    private void initPreview() {
        previews.clear();
        for(int i = 0; i<hallNamesSize; i++) {
            previews.add(hallDao.retrieveHallPreviewAsImage(hallNames.get(i), 220, 395, true, true));
        }
    }

    private void initHallGrid() {
        grigliaSale.getChildren().clear();

        for(int i = 0; i<hallNamesSize; i++) {
            createViewFromPreviews(hallNames.get(i), previews.get(i));
        }

        hallRowCount = 0;
        hallColumnCount = 0;
    }

    private void createViewFromPreviews(String hallName, Image preview) {
        Font font = Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15);

        Label nomeSalaLabel = new Label(FilenameUtils.removeExtension(hallName));
        nomeSalaLabel.setFont(font);
        nomeSalaLabel.setTextFill(Color.WHITE);

        List<Seat> seatList = initDraggableSeatsList(nomeSalaLabel.getText().trim());

        Label numPostiTotaliLabel = new Label("Capienza: " + seatList.size() + " posti");
        numPostiTotaliLabel.setFont(font);
        numPostiTotaliLabel.setTextFill(Color.WHITE);

        int numPostiVIP = getSeatNumberPerType(seatList, SeatTYPE.VIP);
        int numPostiDisabili = getSeatNumberPerType(seatList, SeatTYPE.DISABILE);

        Label numPostiDisabiliLabel = new Label("Posti per disabili: " + numPostiDisabili);
        numPostiDisabiliLabel.setFont(font);
        numPostiDisabiliLabel.setTextFill(Color.WHITE);
        if (numPostiDisabili == 0) {
            numPostiDisabiliLabel.setVisible(false);
        }

        Label numPostiVIPLabel = new Label("Posti VIP: " + numPostiVIP);
        numPostiVIPLabel.setFont(font);
        numPostiVIPLabel.setTextFill(Color.WHITE);
        if (numPostiVIP == 0) {
            numPostiVIPLabel.setVisible(false);
        }

        grigliaSale.setHgap(150);
        grigliaSale.setVgap(60);

        ImageView snapHallView = new ImageView(preview);
        snapHallView.setOnMouseClicked(event -> openHallPreview(nomeSalaLabel.getText()));


        AnchorPane pane = new AnchorPane();
        if (hallColumnCount == columnMax) {
            hallColumnCount = 0;
            hallRowCount++;
        }
        grigliaSale.add(pane, hallColumnCount, hallRowCount);
        hallColumnCount++;

        hallPanel.setContent(grigliaSale);
        GridPane.setMargin(pane, new Insets(15, 0, 0, 15));

        nomeSalaLabel.setLayoutY(snapHallView.getLayoutY() + 133);
        numPostiTotaliLabel.setLayoutY(nomeSalaLabel.getLayoutY() + 15);
        numPostiDisabiliLabel.setLayoutY(numPostiTotaliLabel.getLayoutY() + 15);
        numPostiVIPLabel.setLayoutY(numPostiDisabiliLabel.getLayoutY() + 15);

        pane.getChildren().addAll( snapHallView
                , nomeSalaLabel
                , numPostiTotaliLabel
                , numPostiDisabiliLabel
                , numPostiVIPLabel);

        GUIUtils.setScaleTransitionOnControl(snapHallView);
    }

    private void openHallPreview(String nomeSala) {
        BorderPane borderPane = new BorderPane();

        Image image;
        InputStream fis = null;
        try {
            fis = hallDao.retrieveHallPreviewAsStream(nomeSala);
            image = new Image(fis);
        } finally {
            CloseableUtils.close(fis);
        }

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        borderPane.setCenter(imageView);
        hallPreviewStage = new Stage();
        hallPreviewStage.setTitle(nomeSala);
        Scene scene = new Scene(borderPane);
        hallPreviewStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
        hallPreviewStage.setScene(scene);
        hallPreviewStage.show();
    }

    private int getSeatNumberPerType(List<Seat> mdsList, SeatTYPE type) {
        int res = 0;
        for(Seat mds : mdsList) {
            if(mds.getType().equals(type)) {
                res++;
            }
        }
        return res;
    }

    private List<Seat> initDraggableSeatsList(String nomeSala) { return hallDao.retrieveSeats(nomeSala); }

    void triggerNewHallEvent() {
        initHallNameList();
        initPreview();
        initHallGrid();
    }

    private int temp = 0;
    private void checkPageDimension() {
        Platform.runLater(() -> {
            Stage stage = (Stage) hallPanel.getScene().getWindow();
            stage.widthProperty().addListener(e -> {
                columnMax = getColumnMaxFromPageWidth(stage.getWidth());
                if (temp != columnMax) {
                    temp = columnMax;
                    initHallGrid();
                }
            });
        });
    }

    //Supporta fino ai 1080p
    private int getColumnMaxFromPageWidth(double width) {
        if(width<800) {
            return 2;
        } else if(width>=800 && width<=1360) {
            return 3;
        } else if(width>1360 && width<=1600) {
            return 4;
        } else if(width>1600 && width<=1700) {
            return 5;
        } else if(width>1700){
            return 6;
        } else {
            throw new ApplicationException("Impossibile settare numero colonne per width: " + width);
        }
    }

    @Override
    public void closeAllSubWindows() {
        if(hallPreviewStage!=null) { hallPreviewStage.close(); }
    }
}
