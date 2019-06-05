package it.unipv.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Home extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/user/Home.fxml")));
        stage.setScene(scene);
        stage.setTitle("Golden Movie Studio");
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
        stage.show();

        stage.setOnHidden(e -> Platform.exit());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
