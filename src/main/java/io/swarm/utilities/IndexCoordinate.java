package io.swarm.utilities;

public class IndexCoordinate {

    public static int getXCoordinate(int index, int width) {
        return index % width;
    }

    public static int getYCoordinate(int index, int width) {
        return  index / width;
    }

}
