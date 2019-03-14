package io.swarm.controllers;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import io.swarm.Main;
import io.swarm.utilities.SceneName;
import io.swarm.utilities.*;
import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;

public class LandingController implements Switchable {

    private Stage stage;
    private DisjointSet set;
    private DisjointImage disjointImage;

    @FXML private ImageView image;
    @FXML private StackPane stack;

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        size.setMin(-1);
//        size.setMax(1);
//        size.setValue(0);
//        sizeListener();

//        this.open();
//    }

    @FXML
    public void quit(){
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
//            resizeImage(h, w, disjointImage);
////            resizeImage(h, w, gray);
//        });
//    }

//    private void resizeImage(double height, double width, ImageView disjointImage) {
//        disjointImage.setFitHeight(height);
//        disjointImage.setFitWidth(width);
//        disjointImage.setPreserveRatio(true);
//    }

    @FXML
    public void open() throws IOException {

        DisjointImage disjointImage = FileSelect.load(800, 600);

        if(disjointImage != null) {
            SceneWrapper scene = Main.scenes.get(SceneName.IMAGE);
            stage.setScene(scene.loadScene(disjointImage.getSet(), disjointImage));
        }


//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open disjointImage");
//        File file = fileChooser.showOpenDialog(null);
//        File file = new File("./resources/assets/flock-of-birds.jpg");
//        File file = new File("./resources/assets/v.jpg");
//        File file = new File("./src/main/resources/assets/Birds.jpg");
//        File file = new File("./resources/assets/s.png");
//        if (file != null) {
//            String fileToString = file.toURI().toString();
//            Image img = new Image(fileToString, 400, 300,true, false);
//            set = new DisjointSet((int) img.getWidth(), (int) img.getHeight());
//            disjointImage = new DisjointImage(img, set);
//            SceneWrapper scene = Main.scenes.get(SceneName.IMAGE);
//            stage.setScene(scene.loadScene(set, disjointImage));


//            this.filter();
//            this.disjointImage.setImage(disjointImage.filter());
//            this.count();
//            this.drawSquares();

//            try {
//                save();
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.exit(0);
//            }

//            tryRegression();

//        }
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
//        System.out.println(set.countSets());
    }

//    @FXML
//    public void drawSquares() {
//        (new Tagger(this.set, disjointImage.getWidth(), this.image, this.stack)).tagImage();
//    }
//
//    public void tryRegression() {
//
//        Pane pane = new Pane();
//
//        FlockRegression flock = new FlockRegression(set, (int) disjointImage.getWidth(), (int) disjointImage.getHeight());
//
//        BestFit overall = flock.overAllRegression();
//
//        System.out.println(overall.getRSquared());
//
//        ArrayList<Line> lines = flock.compareSegments();
//
//        if(!lines.isEmpty()) {
//            pane.getChildren().addAll(lines);
//
//            pane.setMaxSize(disjointImage.getWidth(), disjointImage.getHeight());
//            pane.setClip(new Rectangle(disjointImage.getWidth(), disjointImage.getHeight()));
//
//            stack.getChildren().add(pane);
//        }
//
//    }

    @Override
    public void construct(Stage stage, DisjointSet set, DisjointImage disjointImage) {
        this.stage = stage;
    }

}
