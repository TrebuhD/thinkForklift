package pl.edu.amu.szi.forklift.objects;

import javafx.scene.canvas.GraphicsContext;

public class Forklift extends GameObject {
    private Package cargo;

    private int noTasks;

    public Forklift(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY) {
        super(gc, imageSrc, tileWidth, tileHeight, initX, initY);
        this.cargo = null;
    }

    public Package getCargo() {
        return cargo;
    }

    public void pickUpCargo(Package cargo) {
        this.cargo = cargo;
    }

    public boolean hasCargo() {
        return cargo != null;
    }

    public void dropCargo() {
        this.cargo = null;
    }

    @Override
    public void moveDown() {
        super.moveDown();
        if (hasCargo()) {
            cargo.moveDown();
        }
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        if (hasCargo()) {
            cargo.moveLeft();
        }
    }

    @Override
    public void moveRight() {
        super.moveRight();
        if (hasCargo()) {
            cargo.moveRight();
        }
    }

    @Override
    public void moveUp() {
        super.moveUp();
        if (hasCargo()) {
            cargo.moveUp();
        }
    }

    public int getNoTasks() {
        return noTasks;
    }

    public void setNoTasks(int noTasks) {
        this.noTasks = noTasks;
    }

}
