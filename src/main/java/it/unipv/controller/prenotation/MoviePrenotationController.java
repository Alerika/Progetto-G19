package it.unipv.controller.prenotation;

import java.io.IOException;
import java.util.*;

import it.unipv.controller.common.IHomeTrigger;
import it.unipv.db.*;
import it.unipv.dao.HallDao;
import it.unipv.dao.PrenotationDao;
import it.unipv.dao.PricesDao;
import it.unipv.dao.ScheduleDao;
import it.unipv.dao.HallDaoImpl;
import it.unipv.dao.PrenotationDaoImpl;
import it.unipv.dao.PricesDaoImpl;
import it.unipv.dao.ScheduleDaoImpl;
import it.unipv.controller.common.*;
import it.unipv.model.*;
import it.unipv.utils.ApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

public class MoviePrenotationController implements ICloseablePane {

    private List<Schedule> schedules;
    private List<Prenotation> prenotations;
    private List<Seat> selectedMDS = new ArrayList<>();
    private List<String> completeHallNameList = new ArrayList<>();
    private GridPane grigliaSale = new GridPane();
    private static int rowCount = 0;
    private static int columnCount = 0;
    private Movie movie;
    private Prices prices;
    private String scheduleDate;
    private String clickedHour;
    private String clickedHall;
    private Prenotation finalPrenotation;
    private User user;
    private boolean opened = false;
    private IHomeTrigger homeController;
    private HallViewer hallViewer;
    private PricesDao pricesDao;
    private HallDao hallDao;
    private ScheduleDao scheduleDao;
    private PrenotationDao prenotationDao;
    private DBConnection dbConnection;
    @FXML private Label closeButton, confirmButton;
    @FXML private AnchorPane orariPanel, salaHeader, summaryPanel;
    @FXML private ScrollPane salaPanel;

    public void init(IHomeTrigger homeController, String date, Movie m, User user, DBConnection dbConnection) {
        this.homeController = homeController;
        this.movie = m;
        this.scheduleDate = date;
        this.user = user;
        this.dbConnection = dbConnection;
        this.pricesDao = new PricesDaoImpl(dbConnection);
        this.hallDao = new HallDaoImpl(dbConnection);
        this.scheduleDao = new ScheduleDaoImpl(dbConnection);
        this.prenotationDao = new PrenotationDaoImpl(dbConnection);
        schedules = initializeHoursList(date, m);
        initListOfHallNames();
        initComponents();
        initPrices();
        initPrenotationList();
    }

    private void initListOfHallNames() {
        completeHallNameList = hallDao.retrieveHallNames();
    }

    private void initPrices() {
        prices = pricesDao.retrievePrices();
    }

    private void initPrenotationList() {
        prenotations = prenotationDao.retrievePrenotationList();
    }

    private void initComponents() {
        Font infoFont = new Font("Bebas Neue", 24);

        Label disponibleHoursLabel = new Label("ORARI DISPONIBILI: ");
        disponibleHoursLabel.setFont(infoFont);
        disponibleHoursLabel.setTextFill(Color.valueOf("db8f00"));
        disponibleHoursLabel.setLayoutY(50);
        disponibleHoursLabel.setLayoutX(50);

        createHourLabels(infoFont, disponibleHoursLabel.getLayoutX());

        orariPanel.getChildren().add(disponibleHoursLabel);

        GUIUtils.setScaleTransitionOnControl(closeButton);
        closeButton.setOnMouseClicked(event -> doClose());

        GUIUtils.setScaleTransitionOnControl(confirmButton);
        confirmButton.setOnMouseClicked(event -> {
            if (finalPrenotation != null) {
                prenotationDao.insertNewPrenotation(finalPrenotation);
                openAvvisoPrenotazioneController();
                doClose();
            } else {
                GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore: ", "Non è ancora stata creata una prenotazione!");
            }
        });
    }

    private void doClose() {
        Stage stage = (Stage) orariPanel.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }

    private void openAvvisoPrenotazioneController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/prenotation/AvvisoPrenotazione.fxml"));
            Parent p = loader.load();
            AvvisoPrenotazioneController apc = loader.getController();
            apc.init(homeController);
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
            stage.setTitle("Grazie per la prenotazione!");
            stage.show();
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        }
    }

    private List<Label> listOfHourLabels = new ArrayList<>();

    private void createHourLabels(Font font, double initalX) {
        double x = initalX + 230;
        double y = 50;
        int count = 0;
        for (Schedule ms : schedules) {
            Label hourLabel = new Label("  " + ms.getTime() + "  ");
            hourLabel.setTextFill(Color.WHITE);
            hourLabel.setFont(font);
            hourLabel.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));

            hourLabel.setOnMouseEntered(event -> {
                hourLabel.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                hourLabel.setCursor(Cursor.HAND);
            });

            hourLabel.setOnMouseExited(event -> {
                hourLabel.setCursor(Cursor.DEFAULT);
                if (!hourLabel.getText().trim().equalsIgnoreCase(clickedHour)) {
                    hourLabel.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                }

            });

            hourLabel.setOnMouseClicked(event -> {
                summaryPanel.getChildren().clear();
                selectedMDS.clear();
                finalPrenotation = null;
                hourLabel.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                setToWhiteBorderOtherLabels(hourLabel.getText().trim());
                salaHeader.getChildren().clear();
                grigliaSale.getChildren().clear();
                Label hallListLabel = new Label("LISTA SALE DISPONIBILI: ");
                hallListLabel.setFont(font);
                hallListLabel.setTextFill(Color.valueOf("db8f00"));
                hallListLabel.setLayoutY(50);
                hallListLabel.setLayoutX(50);
                salaHeader.getChildren().add(hallListLabel);
                List<String> hallNames = getHallsInvolvedInThatHour(hourLabel.getText().trim());
                for (String s : completeHallNameList) {
                    if (hallNames.contains(s)) {
                        createHallViews(s, hallDao.retrieveHallPreviewAsImage(s, 150, 0, true, true));
                    }
                }
                rowCount = 0;
                columnCount = 0;
                clickedHour = hourLabel.getText().trim();
            });

            if (count >= 5) {
                y += 50;
                x = initalX + 230;
                count = 0;
            }
            hourLabel.setLayoutX(x);
            hourLabel.setLayoutY(y);
            x += 100;
            count++;
            listOfHourLabels.add(hourLabel);
            orariPanel.getChildren().add(hourLabel);
        }
    }

    private void setToWhiteBorderOtherLabels(String nameToExclude) {
        for (Label l : listOfHourLabels) {
            if (!l.getText().trim().toLowerCase().equalsIgnoreCase(nameToExclude.trim().toLowerCase())) {
                l.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            }
        }
    }

    private void createHallViews(String nomeSala, Image image) {
        Label nomeSalaLabel = new Label(nomeSala);
        nomeSalaLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        nomeSalaLabel.setTextFill(Color.WHITE);

        grigliaSale.setHgap(5);
        grigliaSale.setVgap(50);

        ImageView snapHallView = new ImageView(image);
        snapHallView.setFitWidth(150);

        AnchorPane pane = new AnchorPane();
        if (columnCount == 3) {
            columnCount = 0;
            rowCount++;
        }
        grigliaSale.add(pane, columnCount, rowCount);
        columnCount++;

        salaPanel.setContent(grigliaSale);
        GridPane.setMargin(pane, new Insets(15, 0, 0, 15));

        snapHallView.setLayoutX(80);
        nomeSalaLabel.setLayoutY(snapHallView.getLayoutY() + 100);
        nomeSalaLabel.setLayoutX(snapHallView.getLayoutX() + 50);

        snapHallView.setOnMouseClicked(event -> {
            if (!opened) {
                clickedHall = nomeSalaLabel.getText().trim();
                if (selectedMDS.size() > 0) {
                    hallViewer = new HallViewer(this, nomeSalaLabel.getText().trim(), selectedMDS, getOccupiedSeatNames(), dbConnection);
                    hallViewer.setAlwaysOnTop(true);
                } else {
                    hallViewer = new HallViewer(this, nomeSalaLabel.getText().trim(), getOccupiedSeatNames(), dbConnection);
                    hallViewer.setAlwaysOnTop(true);
                }
                opened = true;
            }
        });

        pane.getChildren().addAll(snapHallView);
        pane.getChildren().addAll(nomeSalaLabel);

        GUIUtils.setScaleTransitionOnControl(snapHallView);
    }

    private List<String> getOccupiedSeatNames() {
        List<String> occupiedSeat = new ArrayList<>();
        for (Prenotation p : prenotations) {
            if (p.getGiornoFilm().equalsIgnoreCase(scheduleDate.trim())
                    && p.getOraFilm().equalsIgnoreCase(clickedHour.trim())
                    && p.getNomeFilm().equalsIgnoreCase(movie.getTitolo())
                    && p.getCodiceFilm().equalsIgnoreCase(movie.getCodice())
                    && p.getSalaFilm().equalsIgnoreCase(clickedHall.trim())) {
                occupiedSeat.add(p.getPostiSelezionati());
            }
        }
        return getActualOccupiedSeatsList(occupiedSeat);
    }

    private List<String> getActualOccupiedSeatsList(List<String> listaPostiOccupati) {
        List<String> res = new ArrayList<>();
        for (String s : listaPostiOccupati) {
            String[] supp = s.split("-");
            res.addAll(Arrays.asList(supp));
        }
        return res;
    }

    private List<String> getHallsInvolvedInThatHour(String orario) {
        List<String> res = new ArrayList<>();
        for (Schedule ms : schedules) {
            if (ms.getTime().equals(orario)) {
                res.add(ms.getHallName());
            }
        }
        return res;
    }

    private List<Schedule> initializeHoursList(String date, Movie movie) {
        List<Schedule> schedules = scheduleDao.retrieveMovieSchedules();
        Collections.sort(schedules);
        String ora = "";
        List<Schedule> res = new ArrayList<>();
        for (Schedule ms : schedules) {
            if (ms.getDate().equals(date) && ms.getMovieCode().equals(movie.getCodice())) {
                if (!ora.equals(ms.getTime())) {
                    res.add(ms);
                    ora = ms.getTime();
                }
            }
        }
        return res;
    }

    void triggerSelectedSeats(List<Seat> selectedMDS) {
        this.selectedMDS = selectedMDS;
        createSummaryPanel();
    }

    private void createSummaryPanel() {
        Platform.runLater(() -> {
            summaryPanel.getChildren().clear();
            if (selectedMDS.size() > 0) {
                Label summaryLabel = new Label("RIEPILOGO: ");
                summaryLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
                summaryLabel.setTextFill(Color.valueOf("db8f00"));
                summaryLabel.setLayoutX(50);

                Label actualSummaryLabel = new Label();
                actualSummaryLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
                actualSummaryLabel.setTextFill(Color.valueOf("20b510"));
                actualSummaryLabel.setLayoutX(summaryLabel.getLayoutX() + 100);
                String text = movie.getTitolo() + "    "
                        + scheduleDate + "    "
                        + clickedHour + "    "
                        + clickedHall + ": ";

                StringBuilder selectedMDSName = new StringBuilder(selectedMDS.get(0).getText());
                for (int i = 1; i < selectedMDS.size(); i++) {
                    selectedMDSName.append("-").append(selectedMDS.get(i).getText());
                }
                text = text + selectedMDSName + "    Prezzo totale: " + calculateTotalPrices() + "€";
                actualSummaryLabel.setText(StringUtils.abbreviate(text, 94));
                if (text.length() > 94) {
                    actualSummaryLabel.setTooltip(new Tooltip(text));
                }
                summaryPanel.getChildren().addAll(summaryLabel, actualSummaryLabel);
                finalPrenotation = new Prenotation(user.getNome()
                        , movie.getTitolo()
                        , movie.getCodice()
                        , scheduleDate
                        , clickedHour
                        , clickedHall
                        , selectedMDSName.toString()
                        , calculateTotalPrices() + "€");
            }
        });
    }

    private String calculateTotalPrices() {
        double res = 0;
        for (Seat mds : selectedMDS) {
            switch (mds.getType()) {
                case NORMALE:
                    res += prices.getBase();
                    break;

                case DISABILE:
                    res += prices.getReduced();
                    break;

                case VIP:
                    res += prices.getBase() + prices.getVip();
                    break;
            }

            if (movie.getTipo().equals(MovieTYPE.THREED)) {
                res += prices.getThreed();
            }
        }

        return res % 1 == 0 ? String.valueOf((int) res) : "" + res + "0";
    }

    void triggerClosingHallViewer() {
        opened = false;
        hallViewer.dispose();
    }

    @Override
    public void closeAllSubWindows() {
        if (hallViewer != null) {
            hallViewer.dispose();
        }
    }
}
