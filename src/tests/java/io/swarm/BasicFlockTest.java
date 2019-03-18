package io.swarm;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import io.swarm.stats.FlockRegression;
import io.swarm.stats.BestFit;
import javafx.scene.image.*;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BasicFlockTest extends ApplicationTest {

    private DisjointSet set;
    private DisjointImage disjointImage;

    @Override
    public void start(Stage stage) {
        Image original = new Image((new File("./src/main/resources/assets/b.png")).toURI().toString());
        set = new DisjointSet((int) original.getWidth(),(int) original.getHeight());
        disjointImage = new DisjointImage(original, set, null);
        disjointImage.filter();
        set.generateClusters();
        stage.show();
    }

    @Test
    public void imageWithFourBirdsReturnsCountOfFour() {
        assertEquals(4, set.countSets());
    }

    @Test
    public void imageWithNoFormationReturnsCluster() {
        FlockRegression flock = new FlockRegression(set, (int) disjointImage.getWidth());
        BestFit overall = flock.overAllRegression();
        assertFalse(overall.getRSquared() > 0.5);
    }

}
