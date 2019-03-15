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

    /**
     * Custom constructor used to provide the Controller with
     * the required data
     *
     * @param stage the parent stage to the scene
     * @param set the disjoint set for the image
     * @param disjointImage wrapper for the image being manipulated
     */
    @Override
    public void construct(Stage stage, DisjointSet set, DisjointImage disjointImage) {
        this.stage = stage;
        this.set = set;
        this.disjointImage = disjointImage;
        image.setImage(disjointImage.getOriginalImage());
//        setImageToMonochrome();
//        countBirds();
//        image.setImage(disjointImage.recolorImage());
//        tagBirds();
//        analyseFormation();
    }

    /**
     * Gracefully exit the application
     *
     */
    @Override
    public void quit() {
        System.exit(0);
    }

    /**
     * Open an image from file
     *
     */
    public void open() {
        DisjointImage newImage = FileSelect.load((int) image.getFitWidth(), (int) image.getFitHeight());
        if(newImage != null) {
            disjointImage = newImage;
            set = newImage.getSet();
            image.setImage(newImage.getOriginalImage());
            this.reset();
        }
    }

    /**
     * Reset and clear settings
     *
     */
    private void reset() {
        tagPane.getChildren().clear();
        linePane.getChildren().clear();
        brightness.setValue(50);
    }
    /**
     * Convert an image to black and white
     *
     */
    public void setImageToMonochrome() {
        image.setImage(disjointImage.filter());
        brightness.valueProperty().addListener((ov, old_val, new_val) -> disjointImage.filter(new_val.doubleValue()));
    }

    /**
     * Count the birds in the set
     */
    public void countBirds() {
        set.generateClusters();
        System.out.println(set.countSets());
    }

    /**
     * Tag the birds and draw rectangles around them
     *
     */
    public void tagBirds() {
        image.setImage(disjointImage.recolorImage());
        tagPane.getChildren().clear();
        new Tagger(set, disjointImage.getWidth(), tagPane).tagImage();
    }

    /**
     * Analyse the formation fo the birds and
     * draw the lines of best fit (if appropriate)
     *
     */
    public void analyseFormation() {
        FlockRegression flock = new FlockRegression(set, (int) disjointImage.getWidth());
        ArrayList<Line> lines = flock.compareSegments();
        if(!lines.isEmpty()) {
            linePane.getChildren().addAll(lines);
            linePane.setMaxSize(disjointImage.getWidth(), disjointImage.getHeight());
            linePane.setClip(new Rectangle(disjointImage.getWidth(), disjointImage.getHeight()));
        }
    }

}
