package io.swarm;

import io.helpers.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
//import org.openjdk.jmh.annotations.Fork;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable, Switchable {

    private Stage stage;
    private DisjointSet set;
    private DisjointImage disjointImage;

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
        stage.setScene(Main.getScenes().get(SceneName.SCENE1).getScene());
//        System.exit(0);
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
//        File file = new File("./resources/assets/flock-of-birds.jpg");
//        File file = new File("./resources/assets/v.jpg");
        File file = new File("./src/main/resources/assets/Birds.jpg");
//        File file = new File("./resources/assets/s.png");
        if (file != null) {
            String fileToString = file.toURI().toString();
            Image img = new Image(fileToString, image.getFitWidth(), image.getFitHeight(),
                    true, false);
            set = new DisjointSet((int) img.getWidth(), (int) img.getHeight());
            disjointImage = new DisjointImage(img, set);
            image.setImage(img);

            this.filter();
            this.image.setImage(disjointImage.filter());
            this.count();
            this.drawSquares();

//            try {
//                save();
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.exit(0);
//            }

            tryRegression();

        }
    }

    private void save() throws IOException {
        FileOutputStream fos = new FileOutputStream("vset.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(set);
        oos.close();
    }

    @FXML
    public void filter() {
        image.setImage(disjointImage.filter());
    }



    @FXML
    public void count() {
        set.generateClusters();
        System.out.println(set.countSets());
    }

    @FXML
    public void drawSquares() {
        (new Tagger(this.set, disjointImage.getWidth(), this.image, this.stack)).tagImage();
    }

    public void tryRegression() {

        Pane pane = new Pane();

        FlockRegression flock = new FlockRegression(set, (int) disjointImage.getWidth(), (int) disjointImage.getHeight());

        RegressionAnalysis overall = flock.overAllRegression();

        System.out.println(overall.getRSquared());

        ArrayList<Line> lines = flock.compareSegments();

        if(!lines.isEmpty()) {
            pane.getChildren().addAll(lines);

            pane.setMaxSize(disjointImage.getWidth(), disjointImage.getHeight());
            pane.setClip(new Rectangle(disjointImage.getWidth(), disjointImage.getHeight()));

            stack.getChildren().add(pane);
        }

    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
