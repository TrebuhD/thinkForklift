package pl.edu.amu.szi.forklift.object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Package {
    int posX, posY;
    Image img;
    GraphicsContext gc;
    float tileWidth;
    float tileHeight;

    public Package(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY) {
        this.gc = gc;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.loadImg(imageSrc, tileWidth, tileHeight);
        this.setPosition(initX, initY);
    }

    public void render() {
        gc.drawImage(img, posX * tileWidth, posY * tileHeight);
    }

    private void loadImg(String src, float width, float height) {
        this.img = new Image(getClass().getResourceAsStream(src), width, height, false, false);
    }

    private void setPosition(int x, int y) {
        posX = x;
        posY = y;
    }

    public int getXPos() {
        return posX;
    }

    public int getYPos() {
        return posY;
    }
}
