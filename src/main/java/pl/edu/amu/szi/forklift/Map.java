package pl.edu.amu.szi.forklift;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import pl.edu.amu.szi.forklift.objects.Package;
import pl.edu.amu.szi.forklift.objects.Shelf;

import java.util.ArrayList;

import static pl.edu.amu.szi.forklift.utils.Constants.MAP_SIZE_X;
import static pl.edu.amu.szi.forklift.utils.Constants.MAP_SIZE_Y;

public class Map {
    public static ArrayList<Shelf> shelfList;
    public static ArrayList<Package> packageList;

    static boolean packageFound(int xPos, int yPos) {
        for (Package pkg : packageList) {
            if (pkg.getXPos() == xPos && pkg.getYPos() == yPos) {
                return true;
            }
        }
        return false;
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

    public static Package getPackageAtPos(int xPos, int yPos) {
        for (Package pkg : packageList) {
            if (pkg.getXPos() == xPos && pkg.getYPos() == yPos) {
                return pkg;
            }
        }
        return null;
    }
}
