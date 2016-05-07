package main.pl.edu.amu.szi.forklift.object;

import javafx.scene.canvas.GraphicsContext;

public class Forklift extends GameObject {

    public Forklift(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY) {
        super(gc, imageSrc, tileWidth, tileHeight, initX, initY);
    }

    public void moveUp() {
        if (getYPos() > 0) {
            setPosition(getXPos(), getYPos() - 1);
        }
    }

    public void moveDown() {
        if (getYPos() < 14) {
            setPosition(getXPos(), getYPos() + 1);
        }
    }

    public void moveLeft() {
        if (getXPos() > 0) {
            setPosition(getXPos() - 1, getYPos());
        }
    }

    public void moveRight() {
        if (getXPos() < 14) {
            setPosition(getXPos() + 1, getYPos());
        }
    }
}
