package io.swarm.controllers;

import com.jfoenix.controls.JFXSlider;
import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import io.swarm.Main;
import io.swarm.utilities.SceneName;
import io.swarm.utilities.*;
import io.swarm.stats.FlockRegression;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ImageController implements Switchable {

    private Stage stage;
    private DisjointSet set;
    private DisjointImage disjointImage;

    @FXML ImageView image;
    @FXML StackPane stack;
    @FXML Pane tagPane;
    @FXML Pane linePane;
    @FXML JFXSlider brightness;

    @Override
    public void quit() {
        System.exit(0);
    }

    @Override
    public void construct(Stage stage, DisjointSet set, DisjointImage disjointImage) {
        this.stage = stage;
        this.set = set;
        this.disjointImage = disjointImage;
        image.setImage(disjointImage.getOriginalImage());
        setImageToMonochrome();
        countBirds();
        tagBirds();
        analyseFormation();
    }

    public void open() {
        DisjointImage newImage = FileSelect.load((int) image.getFitWidth(), (int) image.getFitHeight());
        if(newImage != null) {
            disjointImage = newImage;
            set = newImage.getSet();
            image.setImage(newImage.getOriginalImage());
            tagPane.getChildren().clear();
            linePane.getChildren().clear();
        }
    }

    public void setImageToMonochrome() {
        image.setImage(disjointImage.filter());
        brightness.valueProperty().addListener((ov, old_val, new_val) -> disjointImage.filter(new_val.doubleValue()));
    }

    public void countBirds() {
        set.generateClusters();
        System.out.println(set.countSets());
    }

    public void tagBirds() {
        tagPane.getChildren().clear();
        new Tagger(set, disjointImage.getWidth(), tagPane).tagImage();
    }

    public void analyseFormation() {

        FlockRegression flock = new FlockRegression(set, (int) disjointImage.getWidth());
        ArrayList<Line> lines = flock.compareSegments();

        if(!lines.isEmpty()) {
            linePane.getChildren().addAll(lines);
            linePane.setMaxSize(disjointImage.getWidth(), disjointImage.getHeight());
            linePane.setClip(new Rectangle(disjointImage.getWidth(), disjointImage.getHeight()));
        }

    }

    public void back() throws IOException {
        SceneWrapper scene = Main.scenes.get(SceneName.LANDING);
        stage.setScene(scene.loadScene(null, null));
    }


//    @FXML
//    public void filter() throws IOException {
//        SceneWrapper scene = Main.scenes.get(SceneName.TWO);
//        stage.setScene(scene.loadScene(set, disjointImage));
//    }

}
