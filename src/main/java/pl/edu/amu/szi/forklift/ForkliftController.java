package pl.edu.amu.szi.forklift;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import pl.edu.amu.szi.forklift.objects.Forklift;
import pl.edu.amu.szi.forklift.objects.Shelf;

import java.util.ArrayList;
import java.util.HashSet;

import static pl.edu.amu.szi.forklift.Constants.*;

public class ForkliftController {
    public static void handleInput(HashSet currentKeys, Forklift forklift, ArrayList<Shelf> shelfList) {
        if (currentKeys.contains("LEFT")) {
            if (!collisionFound(forklift.getXPos() - 1, forklift.getYPos(), shelfList)) {
                forklift.moveLeft();
            }
        } else if (currentKeys.contains("RIGHT")) {
            if (!collisionFound(forklift.getXPos() + 1, forklift.getYPos(), shelfList)) {
                forklift.moveRight();
            }
        } else if (currentKeys.contains("UP")) {
            if (!collisionFound(forklift.getXPos(), forklift.getYPos() - 1, shelfList)) {
                forklift.moveUp();
            }
        } else if (currentKeys.contains("DOWN")) {
            if (!collisionFound(forklift.getXPos(), forklift.getYPos() + 1, shelfList)) {
                forklift.moveDown();
            }
        }
    }

    public static boolean collisionFound(int nextXPos, int nextYPos, ArrayList<Shelf> shelfList) {
        // collision with shelf
        for (Shelf shelf : shelfList) {
            if ((shelf.getXPos() == nextXPos) && (shelf.getYPos() == nextYPos)) {
                return true;
            }
        }
        return false;
    }

    static void drawFloor(GraphicsContext gc, Image image, float width, float height) {
        for (int i = 0; i < MAP_SIZE_X; i++) {
            for (int j = 0; j < MAP_SIZE_Y; j++) {
                gc.drawImage(image, i * width, j * height);
            }
        }
    }
}
