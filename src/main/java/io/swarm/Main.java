package io.swarm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        URL url = new File("./resources/views/main.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        stage.setTitle("Swarm");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
