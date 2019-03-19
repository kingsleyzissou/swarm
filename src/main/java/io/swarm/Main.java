package io.swarm;

import io.swarm.utilities.SceneName;
import io.swarm.utilities.SceneWrapper;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Swarm: Bird Flock Analyser
 * Swarm App analyses images of birds loaded into the the program. The app determines the number
 * of birds in the image, tags them & then analyses the birds for a flock formation
 *
 * @author: Gianluca Zuccarelli <20079110>
 * @version : 1.0
 *
 * References:
 * - https://github.com/mkgiles/AerialSheepRecognition (Tagger class)
 * - https://github.com/ksnortum/javafx-multi-scene-fxml (Scene switching)
 *
 */
public class Main extends Application{

    public static Map<SceneName, SceneWrapper> scenes;

    private static final String LANDING_FXML = "./src/main/resources/views/landing.fxml";
    private static final String IMAGE_FXML = "./src/main/resources/views/image.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        scenes = new HashMap<>();
        collectScenes(stage);
        stage.setTitle("Swarm");
        SceneWrapper scene = scenes.get(SceneName.LANDING);
        stage.setScene(scene.loadScene(null, null));
        stage.show();
    }

    /**
     * Create map with different scenes
     *
     * @param stage parent stage
     * @throws Exception if scene can't be loaded
     */
    private void collectScenes(Stage stage) throws Exception {
        scenes.put(SceneName.LANDING, new SceneWrapper(LANDING_FXML, stage));
        scenes.put(SceneName.IMAGE, new SceneWrapper(IMAGE_FXML, stage));
    }

}
