package io.swarm.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import io.swarm.utilities.*;
import io.swarm.stats.FlockRegression;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ImageController implements Switchable {

    private DisjointSet set;
    private DisjointImage disjointImage;

    @FXML ImageView image;
    @FXML StackPane stack;
    @FXML Pane tagPane;
    @FXML Pane linePane;
    @FXML JFXSlider brightness;
    @FXML JFXButton contrast, calculate, cube, chart, folder;
    @FXML Label count, formation, name, dimensions, size;

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
        this.set = set;
        this.disjointImage = disjointImage;
        image.setImage(disjointImage.getOriginalImage());
        this.updateInfo(disjointImage.getInfo());
        updateStep(0);
    }

    /**
     * Disable buttons to prevent later steps from being actioned
     * too soon
     *
     * @param step current step in the process
     */
    private void updateStep(int step) {
        switch (step) {
            case 0: calculate.setDisable(true);
                    cube.setDisable(true);
                    chart.setDisable(true);
                    break;
            case 1: calculate.setDisable(false);
                    cube.setDisable(true);
                    chart.setDisable(true);
                    break;
            case 2: cube.setDisable(false);
                    chart.setDisable(true);
                    break;
            case 3: chart.setDisable(false);
                    break;
            default: calculate.setDisable(true);
                    cube.setDisable(true);
                    chart.setDisable(true);
                    break;
        }
    }

    /**
     * Update the information to new photo info
     *
     * @param info image about the selected photo
     */
    private void updateInfo(ImageInfo info) {
        name.setText(info.getName());
        size.setText(info.getFileSize());
        dimensions.setText(info.getDimensions());
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
            this.updateInfo(disjointImage.getInfo());
            this.reset();
        }
    }

    /**
     * Reset all steps
     *
     */
    private void reset() {
        clearPanes();
        count.setText(" ");
        formation.setText(" ");
        brightness.setValue(50);
        updateStep(0);
    }

    /**
     * Clear panes of squares and lines
     */
    private void clearPanes() {
        tagPane.getChildren().clear();
        linePane.getChildren().clear();
    }

    /**
     * Convert an image to black and white
     *
     */
    public void setImageToMonochrome() {
        clearPanes();
        image.setImage(disjointImage.filter());
        brightness.valueProperty().addListener((ov, old_val, new_val) -> disjointImage.filter(new_val.doubleValue()));
        updateStep(1);
    }

    /**
     * Count the birds in the set
     */
    public void countBirds() {
        clearPanes();
        set.generateClusters();
        count.setText(new Integer(set.countSets()).toString());
        updateStep(2);
    }

    /**
     * Tag the birds and draw rectangles around them
     *
     */
    public void tagBirds() {
        tagPane.getChildren().clear();
        image.setImage(disjointImage.recolorImage());
        new Tagger(set, disjointImage.getWidth(), tagPane).tagImage();
        updateStep(3);
    }

    /**
     * Analyse the formation fo the birds and
     * draw the lines of best fit (if appropriate)
     *
     */
    public void analyseFormation() {
        FlockRegression flock = new FlockRegression(set, (int) disjointImage.getWidth());
        ArrayList<Line> lines = flock.compareSegments();

        System.out.println(flock.overAllRegression().getRSquared());

        if(lines.isEmpty() && !flock.overAllRegression().isGoodFit()) {
            formation.setText("No formation");
            return;
        }

        if(flock.overAllRegression().isGoodFit()) {
            formation.setText("Linear formation");
            return;
        }

        linePane.getChildren().addAll(lines);
        linePane.setMaxSize(disjointImage.getWidth(), disjointImage.getHeight());
        linePane.setClip(new Rectangle(disjointImage.getWidth(), disjointImage.getHeight()));
        formation.setText("V-Formation");
    }

}
