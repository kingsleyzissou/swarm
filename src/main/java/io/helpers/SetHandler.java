package io.helpers;

import io.swarm.DisjointSet;
import javafx.scene.paint.Color;

public class SetHandler {

    public static void initialiseSets(DisjointSet set, int x, int y, int width, Color color) {
        double lum = color.getBrightness() * 100;
        int value= (lum <= 50) ? set.getRootValue(1) : -1;
        set.setValue(x + width * y, value);
    }

    public static void createSets(DisjointSet set, int width) {
        for(int index = 0; index < set.getSize(); index++) {
            int right = index + 1;
            int down = index + width;

            if(set.getValue(index) != -1) {
                if(right < set.getSize() - 1 && set.getValue(right) != -1)
                    set.quickUnion(index, right);
                if(down < set.getSize() - 1 && set.getValue(down) != -1)
                    set.quickUnion(index, down);
            }

        }
    }

}
