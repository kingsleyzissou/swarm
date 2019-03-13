package io.swarm;

import javafx.stage.Stage;

public interface Switchable {

    void quit();

    void construct(Stage stage, DisjointSet set, DisjointImage disjointImage);

}
