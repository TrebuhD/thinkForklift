package pl.edu.amu.szi.forklift.objects;

import javafx.scene.canvas.GraphicsContext;

public class Package extends GameObject {

    public Package(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY, int weight, String type) {
        super(gc, imageSrc, tileWidth, tileHeight, initX, initY);
        this.weight = weight;
        this.type = type;
    }

    private Integer weight;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }


}
