package io.swarm;

import io.helpers.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.openjdk.jmh.annotations.Fork;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Image original;
    private WritableImage grayScale;

    private DisjointSet set;
    private double height;
    private double width;

    @FXML private ImageView image;
    @FXML private StackPane stack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        size.setMin(-1);
//        size.setMax(1);
//        size.setValue(0);
//        sizeListener();

        this.open();
    }

    @FXML
    public void quit() {
        System.exit(0);
    }

//    private void sizeListener() {
//        size.valueProperty().addListener((ov, old_val, new_val) -> {
//
//            double h, w;
//            if(new_val.doubleValue() > 0) {
//                h = height * (1 + new_val.doubleValue());
//                w = width * (1 + new_val.doubleValue());
//            } else {
//                h = height / (1 + Math.abs(new_val.doubleValue()));
//                w = width / (1 + Math.abs(new_val.doubleValue()));
//            }
//
//            resizeImage(h, w, image);
////            resizeImage(h, w, gray);
//        });
//    }

//    private void resizeImage(double height, double width, ImageView image) {
//        image.setFitHeight(height);
//        image.setFitWidth(width);
//        image.setPreserveRatio(true);
//    }

    @FXML
    public void open() {

//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open image");
//        File file = fileChooser.showOpenDialog(null);
        File file = new File("./resources/assets/flock-of-birds.jpg");
//        File file = new File("./resources/assets/v.jpg");
//        File file = new File("./resources/assets/Birds.jpg");
//        File file = new File("./resources/assets/s.png");
        if (file != null) {

            original = new Image(file.toURI().toString(), image.getFitWidth(), image.getFitHeight(), true, false);

            image.setImage(original);

            height = original.getHeight();
            width = original.getWidth();

            set = new DisjointSet((int) width,(int) height);

            this.filter();
            this.image.setImage(grayScale);
            this.count();
            this.drawSquares();

            tryRegression();

        }
    }

    @FXML
    public void filter() {
        this.grayScale = ImageHandler.filter(original, grayScale, set);
    }



    @FXML
    public void count() {
        SetHandler.createSets(this.set, (int) width);
        System.out.println(set.countSets());
    }

    @FXML
    public void drawSquares() {
        (new Tagger(this.set, this.width, this.image, this.stack)).tagImage();
    }

    public void tryRegression() {

        Pane pane = new Pane();

        FlockRegression flock = new FlockRegression(set, (int) width, (int) height);

        RegressionAnalysis overall = flock.overAllRegression();

        System.out.println(overall.getRSquared());

        ArrayList<Line> lines = flock.compareSegments();

        if(!lines.isEmpty()) {
            pane.getChildren().addAll(lines);

            pane.setMaxSize(width, height);
            pane.setClip(new Rectangle(width, height));

            stack.getChildren().add(pane);
        }

    }

}
