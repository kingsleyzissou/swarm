package io.swarm.utilities;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import javafx.stage.Stage;

public interface Switchable {

    /**
     * Custom constructor method for controllers
     *
     * @param stage the parent stage object
     * @param set the disjoint set for the underlying image
     * @param disjointImage the image corresponding to the disjoint set
     */
    void construct(Stage stage, DisjointSet set, DisjointImage disjointImage);

    /**
     * Quit method for gracefully exiting the application
     *
     */
    void quit();

}
