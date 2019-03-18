package io.swarm;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import io.swarm.utilities.ImageInfo;
import io.swarm.utilities.SceneName;
import io.swarm.utilities.SceneWrapper;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

// TODO Refactor tagging
// TODO Add comments
// TODO Finish JUnit tests
// TODO Finish Benchmark tests

public class Main extends Application{

    public static Map<SceneName, SceneWrapper> scenes;

    private static final String LANDING_FXML = "./src/main/resources/views/landing.fxml";
    private static final String IMAGE_FXML = "./src/main/resources/views/image.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        scenes = new HashMap<>();
        collectScenes(stage);
        stage.setTitle("Swarm");
//        File file = new File("./src/main/resources/assets/Birds.jpg");
//        Image img = new Image(file.toURI().toString(), 800, 600, true, false);
//        ImageInfo info = new ImageInfo(file.getName(), file.length(), img.getWidth(), img.getHeight());
//        DisjointSet set = new DisjointSet(img.getWidth(), img.getHeight());
//        DisjointImage di = new DisjointImage(img, set, info);
        SceneWrapper scene = scenes.get(SceneName.LANDING);
        stage.setScene(scene.loadScene(null, null));
        stage.show();
    }

    private void collectScenes(Stage stage) throws Exception {
        scenes.put(SceneName.LANDING, new SceneWrapper(LANDING_FXML, stage));
        scenes.put(SceneName.IMAGE, new SceneWrapper(IMAGE_FXML, stage));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
