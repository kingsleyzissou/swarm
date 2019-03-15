package io.swarm.utilities;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SceneWrapper {

    private URL location;
    private Stage stage;
    private Scene scene;
    private SceneLoader loader;

    public SceneWrapper(String view, Stage stage) throws Exception {
        this.location = new File(view).toURL();
        this.stage = stage;
        this.loader = new SceneLoader();
    }

    /**
     * Load the scene and return this for use in the stage
     *
     * @param set the disjoint set for the underlying image
     * @param image the image corresponding to the disjoint set
     *
     * @return the scene to be loaded
     * @throws IOException if the scene cannot be loaded
     */
    public Scene loadScene(DisjointSet set, DisjointImage image) throws IOException {
        if(scene == null) setScene(loader.load(stage, location, set, image));
        return scene;
    }

    /**
     * Setter method for setting the scene in the SceneWrapper
     *
     * @param scene the scene to be added
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
