package io.swarm;

import io.helpers.SceneWrapper;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneTwo implements Switchable {

    Stage stage;
    DisjointSet set;
    DisjointImage disjointImage;
    @FXML ImageView image;

    @Override
    public void quit() {
        System.exit(0);
    }

    @Override
    public void construct(Stage stage, DisjointSet set, DisjointImage disjointImage) {
        this.stage = stage;
        this.set = set;
        this.disjointImage = disjointImage;
        image.setImage(disjointImage.filter());
    }

    @FXML
    public void back() throws IOException {
        SceneWrapper scene = Main.scenes.get(SceneName.ONE);
        stage.setScene(scene.loadScene(null, null));
    }

    @FXML
    public void count() throws IOException {
        SceneWrapper scene = Main.scenes.get(SceneName.THREE);
        stage.setScene(scene.loadScene(set, disjointImage));
    }

}