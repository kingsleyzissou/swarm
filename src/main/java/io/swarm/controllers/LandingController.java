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

    /**
     * Custom constructor used to provide the Controller with
     * the required data
     *
     * @param stage the parent stage to the scene
     * @param set the disjoint set for the image
     * @param disjointImage wrapper for the image being manipulated
     */
    @Override
    public void construct(Stage stage, DisjointSet set, DisjointImage disjointImage) {
        this.stage = stage;
    }

    /**
     * Open image from file and switch to image manipulation scene
     *
     * @throws IOException if it is not possible to switch scenes
     */
    public void open() throws IOException {
        DisjointImage disjointImage = FileSelect.load(800, 600);
        if(disjointImage != null) {
            SceneWrapper scene = Main.scenes.get(SceneName.IMAGE);
            stage.setScene(scene.loadScene(disjointImage.getSet(), disjointImage));
        }
    }

    /**
     * Gracefully exit the application
     *
     */
    public void quit(){
        System.exit(0);
    }


}
