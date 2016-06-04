package pl.edu.amu.szi.forklift.objects;

import javafx.scene.canvas.GraphicsContext;

public class Package extends GameObject {

    private Integer weight;

    public Package(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY, int weight) {
        super(gc, imageSrc, tileWidth, tileHeight, initX, initY);
        this.weight = weight;
    }

    public int getWeight() { return weight; }
}
