package pl.edu.amu.szi.forklift;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ForkLiftObj {

    int posX = 0;
    int posY = 0;
    Image img;
    GraphicsContext gc;
    float tileWidth;
    float tileHeight;

    public ForkLiftObj(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY) {
        this.gc = gc;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.loadImg(imageSrc, tileWidth, tileHeight);
        this.setPosition(initX, initY);
    }

    public void setPosition(int x, int y) {
        posX = x;
        posY = y;
    }

    public void moveUp() {
        if (posY > 0) {
            setPosition(posX, posY - 1);
        }
    }

    public void moveDown() {
        if (posY < 14) {
            setPosition(posX, posY + 1);
        }
    }

    public void moveLeft() {
        if (posX > 0) {
            setPosition(posX - 1, posY);
        }
    }

    public void moveRight() {
        if (posX < 14) {
            setPosition(posX + 1, posY);
        }
    }

    public void render() {
        gc.drawImage(img, posX * tileWidth, posY * tileHeight);
    }

    private void loadImg(String src, float width, float height) {
        this.img = new Image(getClass().getResourceAsStream(src), width, height, false, false);
    }

    public int getXPos() {
        return posX;
    }

    public int getYPos() {
        return posY;
    }
}
