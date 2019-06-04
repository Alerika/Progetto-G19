package it.unipv.gui.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import it.unipv.conversion.CSVToMovieScheduleList;
import it.unipv.conversion.CSVToPrenotationList;
import it.unipv.conversion.CSVToPrices;
import it.unipv.conversion.PrenotationsToCSV;
import it.unipv.gui.common.*;
import it.unipv.gui.login.User;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;
import it.unipv.utils.DataReferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FilenameUtils;

public class MoviePrenotationController implements Initializable {

    private List<MovieSchedule> schedules;
    private List<Prenotation> prenotations;
    private List<Seat> selectedMDS = new ArrayList<>();
    private File[] listOfPreviews;
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
    private HomeController homeController;
    @FXML private Label closeButton, confirmButton;
    @FXML private AnchorPane orariPanel, salaHeader, summaryPanel;
    @FXML private ScrollPane salaPanel;

    @Override public void initialize(URL url, ResourceBundle rb) { }

    public void init(HomeController homeController, String date, Movie m, User user) {
        this.homeController = homeController;
        this.movie = m;
        this.scheduleDate = date;
        this.user = user;
        schedules = initializeHoursList(date, m);
        initListOfPreviews();
        initComponents();
        initPrices();
        initPrenotationList();
    }

    private void initListOfPreviews() {
        listOfPreviews = new File(DataReferences.PIANTINEPREVIEWSFOLDERPATH).listFiles();
    }

    private void initPrices() {
        prices = CSVToPrices.getPricesFromCSV(DataReferences.PRICESFILEPATH);
    }

    private void initPrenotationList() {
        prenotations = CSVToPrenotationList.getPrenotationListFromCSV(DataReferences.PRENOTATIONSFILEPATH);
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
            if(finalPrenotation!=null) {
                PrenotationsToCSV.appendInfoMovieToCSV(finalPrenotation, DataReferences.PRENOTATIONSFILEPATH, true);
                openAvvisoPrenotazioneController();
                doClose();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/AvvisoPrenotazione.fxml"));
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
        for(MovieSchedule ms : schedules) {
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
                if(!hourLabel.getText().trim().equalsIgnoreCase(clickedHour)) {
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
                List<String> hallNames = test(hourLabel.getText().trim());
                for (File file : Objects.requireNonNull(listOfPreviews)) {
                    if(hallNames.contains(FilenameUtils.removeExtension(file.getName()))) {
                        createHallViews(file);
                    }
                }
                rowCount = 0;
                columnCount = 0;
                clickedHour = hourLabel.getText().trim();
            });

            if(count>=5) {
                y+=50;
                x = initalX + 230;
                count = 0;
            }
            hourLabel.setLayoutX(x);
            hourLabel.setLayoutY(y);
            x+=100;
            count++;
            listOfHourLabels.add(hourLabel);
            orariPanel.getChildren().add(hourLabel);
        }
    }

    private void setToWhiteBorderOtherLabels(String nameToExclude) {
        for(Label l : listOfHourLabels) {
            if(!l.getText().trim().toLowerCase().equalsIgnoreCase(nameToExclude.trim().toLowerCase())) {
                l.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            }
        }
    }

    private void createHallViews(File file) {
        try{
            Label nomeSalaLabel = new Label(FilenameUtils.removeExtension(file.getName()));
            nomeSalaLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            nomeSalaLabel.setTextFill(Color.WHITE);

            grigliaSale.setHgap(5);
            grigliaSale.setVgap(50);

            FileInputStream fis = new FileInputStream(file);
            ImageView snapHallView = new ImageView(new Image(fis, 150, 0, true, true));
            snapHallView.setFitWidth(150);
            CloseableUtils.close(fis);

            AnchorPane pane = new AnchorPane();
            if(columnCount==3) {
                columnCount=0;
                rowCount++;
            }
            grigliaSale.add(pane, columnCount, rowCount);
            columnCount++;

            salaPanel.setContent(grigliaSale);
            GridPane.setMargin(pane, new Insets(15,0,0,15));

            snapHallView.setLayoutX(80);
            nomeSalaLabel.setLayoutY(snapHallView.getLayoutY() + 100);
            nomeSalaLabel.setLayoutX(snapHallView.getLayoutX()+50);

            snapHallView.setOnMouseClicked(event -> {
                if(!opened) {
                    clickedHall = nomeSalaLabel.getText().trim();
                    if(selectedMDS.size()>0) {
                        new HallViewer(this, nomeSalaLabel.getText().trim(), selectedMDS, getOccupiedSeatNames());
                    } else {
                        new HallViewer(this, nomeSalaLabel.getText().trim(), getOccupiedSeatNames());
                    }
                    opened = true;
                }
            });

            pane.getChildren().addAll(snapHallView);
            pane.getChildren().addAll(nomeSalaLabel);

            GUIUtils.setScaleTransitionOnControl(snapHallView);
        } catch(FileNotFoundException ex) {
            throw new ApplicationException(ex);
        }
    }

    private List<String> getOccupiedSeatNames() {
        List<String> occupiedSeat = new ArrayList<>();
        for(Prenotation p : prenotations) {
            if( p.getGiornoFilm().equalsIgnoreCase(scheduleDate.trim())
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
        for(String s : listaPostiOccupati) {
            String[] supp = s.split("-");
            res.addAll(Arrays.asList(supp));
        }
        return res;
    }

    private List<String> test(String orario) {
        List<String> res = new ArrayList<>();
        for(MovieSchedule ms : schedules) {
            if(ms.getTime().equals(orario)) {
                res.add(ms.getHallName());
            }
        }
        return res;
    }

    private List<MovieSchedule> initializeHoursList(String date, Movie movie) {
        List<MovieSchedule> movieSchedules = CSVToMovieScheduleList.getMovieScheduleListFromCSV(DataReferences.MOVIESCHEDULEFILEPATH);
        Collections.sort(movieSchedules);
        String ora = "";
        List<MovieSchedule> res =  new ArrayList<>();
        for(MovieSchedule ms : movieSchedules) {
            if(ms.getDate().equals(date) && ms.getMovieCode().equals(movie.getCodice())) {
                if(!ora.equals(ms.getTime())) {
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
            if(selectedMDS.size()>0) {
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
                for(int i=1; i<selectedMDS.size(); i++) {
                    selectedMDSName.append("-").append(selectedMDS.get(i).getText());
                }
                text = text + selectedMDSName + "    Prezzo totale: " + calculateTotalPrices() + "€";
                actualSummaryLabel.setText(text);
                summaryPanel.getChildren().addAll(summaryLabel, actualSummaryLabel);
                finalPrenotation = new Prenotation( user.getName()
                                                  , movie.getTitolo()
                                                  , movie.getCodice()
                                                  , scheduleDate
                                                  , clickedHour
                                                  , clickedHall
                                                  , selectedMDSName.toString()
                                                  , calculateTotalPrices()+"€");
            }
        });
    }

    private String calculateTotalPrices() {
        double res = 0;
        for(Seat mds : selectedMDS) {
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

            if(movie.getTipo().equals(MovieTYPE.THREED)) {
                res += prices.getThreed();
            }
        }

        return res%1==0 ? String.valueOf((int)res) : ""+res+"0";
    }

    void triggerClosingHallViewer() {
        opened = false;
    }
}
