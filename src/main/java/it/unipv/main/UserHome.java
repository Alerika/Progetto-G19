package it.unipv.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserHome extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/UserHome.fxml"));
                
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Golden Movie Studio");
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
