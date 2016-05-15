package pl.edu.amu.szi.forklift.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import pl.edu.amu.szi.forklift.utils.Position;

public class GameObject {
    private Position position;
    private Image img;
    private GraphicsContext gc;

    float tileWidth;
    float tileHeight;

    public GameObject(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY) {
        this.gc = gc;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.position = new Position(initX, initY);
        this.loadImg(imageSrc, tileWidth, tileHeight);
    }

    public int getXPos() {
        return position.getX();
    }

    public int getYPos() {
        return position.getY();
    }

    public void render() {
        gc.drawImage(img, position.getX() * tileWidth, position.getY() * tileHeight);
    }

    public void setPosition(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    private void loadImg(String src, float width, float height) {
        this.img = new Image(getClass().getResourceAsStream(src), width, height, false, false);
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
