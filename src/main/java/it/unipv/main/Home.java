package it.unipv.main;

import it.unipv.DB.DBConnection;
import it.unipv.gui.user.HomeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Home extends Application {

    private DBConnection dbConnection;

    @Override
    public void start(Stage stage) throws Exception {
        this.dbConnection = new DBConnection();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/Home.fxml"));
        stage.setScene(new Scene(loader.load()));
        HomeController hc = loader.getController();
        hc.init(dbConnection);
        stage.setTitle("Golden Movie Studio");
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
        stage.show();
        stage.setOnHidden(e -> {
            dbConnection.close();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
