package io.swarm;

import io.helpers.FlockRegression;
import io.helpers.ImageHandler;
import io.helpers.RegressionAnalysis;
import io.helpers.SetHandler;
import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(JavaFXTestRunner.class)
public class BasicFlockTest {

    private Image original;
    private WritableImage grayScale;

    private DisjointSet set;
    private double width;
    private double height;

    @Before
    public void setup() {
        File file = new File("./resources/assets/b.png");
        original = new Image(file.toURI().toString());
        height = original.getHeight();
        width = original.getWidth();
        set = new DisjointSet((int) width,(int) height);
        ImageHandler.filter(original, grayScale, set);
        SetHandler.createSets(this.set, (int) width);
    }

    @Test
    public void imageWithFourBirdsReturnsCountOfFour() {
        assertEquals(4, set.countSets());
    }

    @Test
    public void imageWithNoFormationReturnsCluster() {
        FlockRegression flock = new FlockRegression(set, (int) width, (int) height);
        RegressionAnalysis overall = flock.overAllRegression();
        assertFalse(overall.getRSquared() > 0.5);
    }

}
