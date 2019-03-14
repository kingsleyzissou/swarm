package io.swarm.utilities;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import javafx.stage.Stage;

public interface Switchable {

    void quit();

    void construct(Stage stage, DisjointSet set, DisjointImage disjointImage);

}
