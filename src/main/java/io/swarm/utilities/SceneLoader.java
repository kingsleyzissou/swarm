package io.swarm.utilities;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SceneLoader {

    /**
     * Load a scene and provide the relevant data
     * required for the scene's controller
     *
     * @param stage the parent stage element
     * @param location the location of the view resource
     * @param set the disjoint set
     * @param image the corresponding image to the disjoint set
     *
     * @return the scene to be loaded
     * @throws IOException if the scene cannot be loaded
     */
    public Scene load(Stage stage, URL location, DisjointSet set, DisjointImage image) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        Scene scene = new Scene(loader.load(), 1100, 700);
        File stylesheet = new File("./src/main/resources/styles/style.css");
        scene.getStylesheets().add(stylesheet.toURI().toString());
        Switchable controller = loader.getController();
        controller.construct(stage, set, image);
        return scene;
    }

}
