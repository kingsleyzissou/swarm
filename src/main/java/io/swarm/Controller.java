package io.swarm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import io.helpers.Tagger;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Image original;
    private WritableImage grayscale;

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
        File file = new File("./resources/assets/s.png");
        if (file != null) {

            original = new Image(file.toURI().toString(), image.getFitWidth(), image.getFitHeight(), true, false);

            image.setImage(original);

            height = original.getHeight();
            width = original.getWidth();

            set = new DisjointSet((int) width,(int) height);

            this.filter();
            this.image.setImage(grayscale);
            this.count();
            this.drawSquares();

//            this.reduceNoise();
//            this.generateSets();
//
//            System.out.println("Number of birds: " + set.filterOutliers());
//            System.out.println("Average bird size: " + set.getAverage());
//
//            this.drawSquares();
        }
    }

    @FXML
    public void filter() {
        PixelReader reader = original.getPixelReader();

        grayscale = new WritableImage((int) this.width, (int) this.height);
        PixelWriter writer = grayscale.getPixelWriter();

        for(int y=0; y < height ; y++) {
            for(int x = 0; x < width; x++) {
                Color color = this.filterImage(x, y, reader, writer);
                this.initialiseDisjointSet(x, y, color);
            }
        }

    }

    private void initialiseDisjointSet(int x, int y, Color color) {
        double lum = color.getBrightness() * 100;
        int value= (lum <= 50) ? set.getRootValue(1) : -1;
        this.set.setValue(x + (int) width * y, value);
    }

    private Color filterImage(int x, int y, PixelReader reader, PixelWriter writer) {
        Color color = reader.getColor(x, y);
        double lum = color.getBrightness() * 100;
        Color newColor = lum <= 50 ? Color.BLACK : Color.WHITE;
        writer.setColor(x,y, newColor);
        return newColor;
    }

    private void generateSets() {
        for(int index = 0; index < this.set.getSize(); index++) {
            int right = index + 1;
            int down = index + (int) width;

            if(this.set.getValue(index) != -1) {
                if(right < this.set.getSize() - 1 && this.set.getValue(right) != -1)
                    set.quickUnion(index, right);
                if(down < this.set.getSize() - 1 && this.set.getValue(down) != -1)
                    set.quickUnion(index, down);
            }

        }
    }

    @FXML
    public void count() {
        this.generateSets();
        set.filterOutliers();
//        System.out.println(set.filterOutliers());
    }

    @FXML
    public void drawSquares() {
        (new Tagger(this.set, this.width, this.image, this.grayscale, this.stack)).tagImage();
    }


}
