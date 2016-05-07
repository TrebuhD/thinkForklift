package main.pl.edu.amu.szi.forklift.object;

import javafx.scene.canvas.GraphicsContext;

public class Package extends GameObject {
    public Package(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY) {
        super(gc, imageSrc, tileWidth, tileHeight, initX, initY);
    }
}
