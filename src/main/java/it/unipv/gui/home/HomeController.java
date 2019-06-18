package it.unipv.gui.home;

import it.unipv.DB.DBConnection;
import it.unipv.conversion.UserInfo;
import it.unipv.gui.common.ICloseablePane;
import it.unipv.gui.common.Movie;
import it.unipv.gui.login.LoginController;
import it.unipv.gui.login.RegistrazioneController;
import it.unipv.gui.login.User;
import it.unipv.gui.manager.ManagerHomeController;
import it.unipv.gui.user.AreaRiservataHomeController;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeController {
    @FXML private Rectangle rectangleMenu;
    @FXML private AnchorPane menuWindow, menuContainer;
    @FXML private Label logLabel, nonRegistratoQuestionLabel, registerButton, areaRiservataButton;
    @FXML private AnchorPane logoutPane;
    @FXML private BorderPane homePanel;
    private final Stage stageRegistrazione = new Stage();
    private DBConnection dbConnection;
    private final Stage stageLogin = new Stage();
    private User loggedUser;
    private ManagerHomeController mhc;
    private AreaRiservataHomeController arhc;
    private HallListPanelController hlpc;
    private MovieListPanelController mlpc;
    private Stage reservedAreaStage, managerAreaStage;
    private List<ICloseablePane> iCloseablePanes = new ArrayList<>();

    public void init(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
        if(checkIfThereIsAlreadyUserSaved()) {
            loggedUser = UserInfo.getUserInfo();
            setupLoggedUser();
        } else {
            logoutPane.setVisible(false);
            areaRiservataButton.setVisible(false);
        }
        initWelcomePage(loggedUser);
        menuWindow.setVisible(false);
        menuWindow.setPickOnBounds(false);
        menuContainer.setPickOnBounds(false);
    }

    @FXML
    private void animationMenu(){
        KeyValue widthValueForward = new KeyValue(rectangleMenu.widthProperty(), rectangleMenu.getWidth() +81);
        KeyValue widthValueBackwards = new KeyValue(rectangleMenu.widthProperty(), rectangleMenu.getWidth() -81);
        KeyValue heightValueForward = new KeyValue(rectangleMenu.heightProperty(), rectangleMenu.getHeight()+244);
        KeyValue heightValueBackwards = new KeyValue(rectangleMenu.heightProperty(), rectangleMenu.getHeight()-244);
        KeyFrame forwardW = new KeyFrame(javafx.util.Duration.seconds(0.3), widthValueForward);
        KeyFrame backwardW = new KeyFrame(javafx.util.Duration.seconds(0.15), widthValueBackwards);
        KeyFrame forwardH = new KeyFrame(javafx.util.Duration.seconds(0.3), heightValueForward);
        KeyFrame backwardH = new KeyFrame(javafx.util.Duration.seconds(0.15), heightValueBackwards);
        Timeline timelineForwardH = new Timeline(forwardH);
        Timeline timelineBackwardH = new Timeline(backwardH);
        Timeline timelineForwardW = new Timeline(forwardW);
        Timeline timelineBackwardW = new Timeline(backwardW);
        FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.seconds(0.4), menuWindow);
        FadeTransition fadeOut = new FadeTransition(javafx.util.Duration.seconds(0.1), menuWindow);

        if(!menuWindow.isVisible()){
            menuWindow.setOpacity(0);
            menuWindow.setVisible(true);
            timelineForwardW.play();
            timelineForwardH.play();

            fadeIn.setDelay(javafx.util.Duration.seconds(0.2));
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        } else {
            if(menuWindow.isVisible()){
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0);
                fadeOut.play();
                menuWindow.setVisible(false);

                timelineBackwardH.play();
                timelineBackwardW.play();
            }
        }
    }

    private void initWelcomePage(User user) {
        try {
            homePanel.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home/welcome.fxml"));
            AnchorPane welcomePanel = loader.load();
            welcomePanel.prefWidthProperty().bind(homePanel.widthProperty());
            welcomePanel.prefHeightProperty().bind(homePanel.heightProperty());
            WelcomePanelController wpc = loader.getController();
            wpc.init(user, dbConnection);
            homePanel.setCenter(welcomePanel);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    @FXML
    private void homeClick() {
        closeAllSubWindows();
        openHome();
        animationMenu();
    }

    @FXML
    private void salaClick() {
        closeAllSubWindows();
        openHallList();
        animationMenu();
    }

    @FXML
    private void infoClick() {
        closeAllSubWindows();
        openInfo();
        animationMenu();
    }

    @FXML
    private void areaRiservataClick() {
        openReservedArea();
        animationMenu();
    }

    @FXML
    private void registrationWindow(){
        if(!stageRegistrazione.isShowing()){
            if(loggedUser==null) {
                openRegistrazione();
                animationMenu();
            } else {
                doLogout();
                animationMenu();
            }
        }
    }

    @FXML
    private void loginWindow(){
        if(!stageLogin.isShowing()){
            if(loggedUser==null) {
                openLogin();
            } else {
                openReservedArea();
            }
        }
    }

    @FXML private void logoutListener() { doLogout(); }

    void openHome() {
        try {
            homePanel.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home/movieList.fxml"));
            AnchorPane moviePanel = loader.load();
            mlpc = loader.getController();
            mlpc.init(this, homePanel.getWidth(), dbConnection);
            moviePanel.prefWidthProperty().bind(homePanel.widthProperty());
            moviePanel.prefHeightProperty().bind(homePanel.heightProperty());
            homePanel.setCenter(moviePanel);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void openHallList() {
        try {
            homePanel.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home/hallList.fxml"));
            AnchorPane hallPanel = loader.load();
            hlpc = loader.getController();
            hlpc.init(homePanel.getWidth(), dbConnection);
            hallPanel.prefWidthProperty().bind(homePanel.widthProperty());
            hallPanel.prefHeightProperty().bind(homePanel.heightProperty());
            homePanel.setCenter(hallPanel);
            if(!iCloseablePanes.contains(hlpc)) { iCloseablePanes.add(hlpc); }
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void openInfo() {
        try {
            homePanel.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home/info.fxml"));
            AnchorPane infoPanel = loader.load();
            infoPanel.prefWidthProperty().bind(homePanel.widthProperty());
            infoPanel.prefHeightProperty().bind(homePanel.heightProperty());
            homePanel.setCenter(infoPanel);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void openRegistrazione(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/Registrazione.fxml"));
            Parent p = loader.load();
            RegistrazioneController rc = loader.getController();
            rc.init(dbConnection);
            stageRegistrazione.setScene(new Scene(p));
            stageRegistrazione.setResizable(false);
            stageRegistrazione.setTitle("Registrazione");
            stageRegistrazione.show();
            stageRegistrazione.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void doLogout() {
        logLabel.setText("effettua il login");
        nonRegistratoQuestionLabel.setText("non sei registrato?");
        registerButton.setText("Registrati");
        areaRiservataButton.setVisible(false);
        logoutPane.setVisible(false);
        loggedUser = null;

        if(checkIfThereIsAlreadyUserSaved()) {
            UserInfo.deleteUserInfoFileInUserDir();
        }

        if(reservedAreaStage != null) {
            if(reservedAreaStage.isShowing()) {
                isReservedAreaOpened = false;
                arhc.closeAllSubWindows();
                reservedAreaStage.close();
            }
        }

        if(managerAreaStage != null) {
            if(managerAreaStage.isShowing()) {
                isManagerAreaOpened = false;
                mhc.closeAllSubWindows();
                managerAreaStage.close();
            }
        }

        closeAllSubWindows();

        initWelcomePage(loggedUser);
    }

    private void closeAllSubWindows() {
        for(ICloseablePane i : iCloseablePanes) {
            i.closeAllSubWindows();
        }
    }

    private void openLogin(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/Login.fxml"));
            Parent p = loader.load();
            LoginController lc = loader.getController();
            lc.init(this, dbConnection);
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
            stage.show();
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        }
    }

    private void openReservedArea() {
        if(!isHimANormalUser(loggedUser)) {
            doOpenManagerArea();
        } else {
            doOpenReservedArea();
        }
    }

    private boolean isManagerAreaOpened = false;
    private void doOpenManagerArea() {
        if(!isManagerAreaOpened) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manager/ManagerHome.fxml"));
                Parent root = loader.load();
                mhc = loader.getController();
                mhc.init(this, dbConnection);
                managerAreaStage = new Stage();
                managerAreaStage.setScene(new Scene(root));
                managerAreaStage.setTitle("Area Manager");
                managerAreaStage.setMinHeight(850);
                managerAreaStage.setMinWidth(1100);
                managerAreaStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
                managerAreaStage.setOnCloseRequest( event -> {
                    isManagerAreaOpened = false;
                    mhc.closeAllSubWindows();
                });
                managerAreaStage.show();
                isManagerAreaOpened = true;
            } catch (IOException e) {
                throw new ApplicationException(e);
            }
        }

    }

    private boolean isReservedAreaOpened = false;
    public void doOpenReservedArea() {
        if(!isReservedAreaOpened) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/AreaRiservataHome.fxml"));
                Parent p = loader.load();
                arhc = loader.getController();
                arhc.init(loggedUser, true, dbConnection);
                reservedAreaStage = new Stage();
                reservedAreaStage.setScene(new Scene(p));
                reservedAreaStage.setMinHeight(850);
                reservedAreaStage.setMinWidth(1200);
                reservedAreaStage.setTitle("Area riservata di " + loggedUser.getName());
                reservedAreaStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
                reservedAreaStage.setOnCloseRequest( event -> {
                    isReservedAreaOpened = false;
                    arhc.closeAllSubWindows();
                });
                reservedAreaStage.show();
                isReservedAreaOpened = true;
            } catch (IOException ex) {
                throw new ApplicationException(ex);
            }
        }
    }

    private void setupLoggedUser() {
        logLabel.setText(loggedUser.getName());
        logoutPane.setVisible(true);
        nonRegistratoQuestionLabel.setText("Vuoi uscire?");
        registerButton.setText("logout");
        if(!isHimANormalUser(loggedUser)) {
            areaRiservataButton.setText("Area Manager");
        } else {
            areaRiservataButton.setText("Area Riservata");
        }
        areaRiservataButton.setVisible(true);
        initWelcomePage(loggedUser);
    }

    private boolean isHimANormalUser(User user) {
        return !( user.getName().equalsIgnoreCase(DataReferences.ADMINUSERNAME)
                &&  user.getPassword().equalsIgnoreCase(DataReferences.ADMINPASSWORD));
    }

    private boolean checkIfThereIsAlreadyUserSaved() {
        return UserInfo.checkIfUserInfoFileExists();
    }

    public void triggerNewLogin(User user) {
        closeAllSubWindows();
        loggedUser = user;
        setupLoggedUser();
    }

    public void triggerNewMovieEvent() { mlpc.triggerNewMovieEvent(); }

    public void triggerNewHallEvent() {
        hlpc.triggerNewHallEvent();
    }

    void triggerMovieClicked(Movie movie) {
        openSingleMoviePanel(movie);
    }

    private void openSingleMoviePanel(Movie movie) {
        try {
            homePanel.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home/singleMoviePanel.fxml"));
            AnchorPane singleMoviePanel = loader.load();
            SingleMoviePanelController smpc = loader.getController();
            smpc.init(this, movie, loggedUser, dbConnection);
            singleMoviePanel.prefWidthProperty().bind(homePanel.widthProperty());
            singleMoviePanel.prefHeightProperty().bind(homePanel.heightProperty());
            if(!iCloseablePanes.contains(smpc)) { iCloseablePanes.add(smpc); }
            homePanel.setCenter(singleMoviePanel);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }
}
