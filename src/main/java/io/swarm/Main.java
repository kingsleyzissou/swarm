package io.swarm;

import io.helpers.SceneWrapper;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application{

    public static Map<SceneName, SceneWrapper> scenes;

    private static final String MAIN_FXML = "./src/main/resources/views/main.fxml";
    private static final String SCENE_ONE = "./src/main/resources/views/one.fxml";
    private static final String SCENE_TWO = "./src/main/resources/views/two.fxml";
    private static final String SCENE_THREE = "./src/main/resources/views/three.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        scenes = new HashMap<>();
        collectScenes(stage);
        stage.setTitle("Swarm");
        stage.setScene(scenes.get(SceneName.MAIN).loadScene(null, null));
        stage.show();
    }

    private void collectScenes(Stage stage) throws Exception {
        scenes.put(SceneName.MAIN, new SceneWrapper(MAIN_FXML, stage));
        scenes.put(SceneName.ONE, new SceneWrapper(SCENE_ONE, stage));
        scenes.put(SceneName.TWO, new SceneWrapper(SCENE_TWO, stage));
        scenes.put(SceneName.THREE, new SceneWrapper(SCENE_THREE, stage));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
