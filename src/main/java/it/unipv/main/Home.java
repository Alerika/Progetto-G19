package it.unipv.main;

import it.unipv.controller.common.IHomeInitializer;
import it.unipv.db.DBConnection;
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
        openHome(stage);
    }

    private void openHome(Stage stage) throws java.io.IOException {
        this.dbConnection = new DBConnection();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home/home.fxml"));
        stage.setScene(new Scene(loader.load()));
        IHomeInitializer hc = loader.getController();
        hc.init(dbConnection);
        stage.setTitle("Golden Movie Studio");
        stage.setResizable(true);
        stage.setMinHeight(768);
        stage.setMinWidth(1360);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
        stage.show();
        stage.setOnHidden(e -> {
            hc.closeAll();
            dbConnection.close();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
