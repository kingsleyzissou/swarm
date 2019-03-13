package io.helpers;

import io.swarm.DisjointImage;
import io.swarm.DisjointSet;
import io.swarm.Switchable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneLoader {

    public Scene load(Stage stage, URL location, DisjointSet set, DisjointImage image) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        Scene scene = new Scene(loader.load(), 1000, 800);
        Switchable controller = loader.getController();
        controller.construct(stage, set, image);
        return scene;
    }

}
