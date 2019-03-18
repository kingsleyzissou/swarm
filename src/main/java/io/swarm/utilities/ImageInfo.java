package io.swarm.utilities;

public class ImageInfo {

    private final String name;
    private final String fileSize;
    private String dimensions;

    public ImageInfo (String name, Long fileSize, double width, double height) {
        this.name = name;
        Long fileSizeInKb = fileSize / 1024;
        this.fileSize = fileSizeInKb.toString().concat(" Kb");
        this.dimensions = width + " x " + height;
    }

    public String getName() {
        return name;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getDimensions() {
        return dimensions;
    }

}
