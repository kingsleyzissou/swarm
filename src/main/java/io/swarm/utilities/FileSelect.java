package io.swarm.utilities;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;

public class FileSelect {

    /**
     * File selection helper
     *
     * @param width of the ImageView
     * @param height of the ImageView
     * @return selected file
     */
    public static DisjointImage load(int width, int height) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open disjointImage");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String fileToString = file.toURI().toString();
            Image img = new Image(fileToString, width, height,true, false);
            DisjointSet set = new DisjointSet((int) img.getWidth(), (int) img.getHeight());
            return new DisjointImage(img, set);
        }
        return null;
    }

}
