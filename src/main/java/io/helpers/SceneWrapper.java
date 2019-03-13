package io.helpers;

import io.swarm.DisjointImage;
import io.swarm.DisjointSet;
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

    public Scene loadScene(DisjointSet set, DisjointImage image) throws IOException {
        if(scene == null) setScene(loader.load(stage, location, set, image));
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
