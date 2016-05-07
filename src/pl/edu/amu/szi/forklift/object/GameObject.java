package pl.edu.amu.szi.forklift.object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import pl.edu.amu.szi.forklift.Position;

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

    public void render() {
        gc.drawImage(img, position.getX() * tileWidth, position.getY() * tileHeight);
    }

    private void loadImg(String src, float width, float height) {
        this.img = new Image(getClass().getResourceAsStream(src), width, height, false, false);
    }

    private void setPosition(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    public int getXPos() {
        return position.getX();
    }

    public int getYPos() {
        return position.getY();
    }
}
