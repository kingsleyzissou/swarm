package io.swarm;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import io.swarm.stats.BestFit;
import io.swarm.stats.FlockRegression;
import javafx.scene.image.Image;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;



public class VFlockTest extends ApplicationTest {

    private DisjointSet set;
    private DisjointImage disjointImage;


    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) {
        Image original = new Image((new File("./src/main/resources/assets/Birds.jpg")).toURI().toString());
        set = new DisjointSet((int) original.getWidth(),(int) original.getHeight());
        disjointImage = new DisjointImage(original, set, null);
        disjointImage.filter();
        set.generateClusters();
        stage.show();
    }

    @Test
    public void imageWithNoFormationReturnsCluster() {
        FlockRegression flock = new FlockRegression(set, (int) disjointImage.getWidth());
        ArrayList<Line> lines = flock.compareSegments();
        assertFalse(!lines.isEmpty());
    }

}

