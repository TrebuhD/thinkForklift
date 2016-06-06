package pl.edu.amu.szi.forklift;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import pl.edu.amu.szi.forklift.objects.Forklift;
import pl.edu.amu.szi.forklift.objects.GameObject;
import pl.edu.amu.szi.forklift.objects.Package;
import pl.edu.amu.szi.forklift.objects.Shelf;

import java.util.ArrayList;
import java.util.Random;

import static pl.edu.amu.szi.forklift.utils.Constants.*;

// Singleton map class
public class Map {
    private static Map map = new Map();

    public static ArrayList<Shelf> shelfList;
    public static ArrayList<Package> packageList;

    private float tileWidth;
    private float tileHeight;

    private Image floorImg;
    private Forklift forklift;
    private GraphicsContext gc;

    private Map() {
        shelfList = new ArrayList<>();
        packageList = new ArrayList<>();

        tileWidth = CANVAS_WIDTH / MAP_SIZE_X;
        tileHeight = CANVAS_HEIGHT / MAP_SIZE_Y;
    }

    public static Map getInstance() {
        return map;
    }

    public ArrayList<Shelf> getShelfList() {
        return shelfList;
    }

    public ArrayList<Package> getPackageList() {
        return packageList;
    }

    public void prepareObjects(GraphicsContext gc) {
        this.setGc(gc);
        floorImg = new Image(getClass().getResourceAsStream(IMG_PODLOGA), tileWidth, tileHeight, false, false);
        forklift = new Forklift(gc, IMG_FORKLIFT, map.getTileWidth(), map.getTileHeight(), 0, 0);

        create_shelves(gc);
        create_packages(IMG_PACZKA12, gc);
        create_packages(IMG_PACZKA22, gc);
        create_packages(IMG_PACZKA32, gc);

    }

    public boolean packageFound(int xPos, int yPos) {
        for (Package pkg : packageList) {
            if (pkg.getXPos() == xPos && pkg.getYPos() == yPos) {
                return true;
            }
        }
        return false;
    }

    public boolean isPassable(int nextXPos, int nextYPos) {
        // collision with shelf
        for (Shelf shelf : shelfList) {
            if ((shelf.getXPos() == nextXPos) && (shelf.getYPos() == nextYPos)) {
                return false;
            }
        }
        return true;
    }

    public void drawFloor(GraphicsContext gc, Image image, float width, float height) {
        for (int i = 0; i < MAP_SIZE_X; i++) {
            for (int j = 0; j < MAP_SIZE_Y; j++) {
                gc.drawImage(image, i * width, j * height);
            }
        }
    }

    public Package getPackageAtPos(int xPos, int yPos) {
        for (Package pkg : packageList) {
            if (pkg.getXPos() == xPos && pkg.getYPos() == yPos) {
                System.out.println(
                        "PKG at x:"+Integer.toString(xPos)+
                                " y:"+ Integer.toString(yPos)+
                                " has weight: "+Integer.toString(pkg.getWeight())+
                                " and is categorized as: "+pkg.getType()
                );
                return pkg;
            }
        }
        System.out.println("Package not found here");
        return null;
    }

    public Package hidePackageAtPos(int xPos, int yPos){
        for (Package pkg : packageList) {
            if (pkg.getXPos() == xPos && pkg.getYPos() == yPos) {
                System.out.println("Removing pkg from drawing!");
                pkg.setHidden(true);
                return pkg;
            }
        }
        return null;
    }

    public void spawnPackageAtPos(int xPos, int yPos, Package pkg){
        pkg.setPosition(xPos, yPos);
        packageList.add(pkg);
        System.out.println("Pkg added to display");
    }

    public Shelf getShelfAtPos(int xPos, int yPos){
        for (Shelf shelf : shelfList) {
            if ((shelf.getXPos() == xPos) && (shelf.getYPos() == yPos)) {
                return shelf;
            }
        }
        return null;
    }

    private void create_shelves(GraphicsContext gc) {
        for (int k = 0; k < MAP_SIZE_X; k = k + 3) {
            for (int j = 5; j < MAP_SIZE_Y; j++) {
                shelfList.add(new Shelf(gc, IMG_POLKIDWIE, tileWidth, tileHeight, j, k));
            }
        }
    }

    private void create_packages(String packageType, GraphicsContext gc) {

        Random ran = new Random();
        int weight;
        String type;
        for (int k = 0; k < PACKAGE_COUNT; k++) {
            int posX;
            int posY;

            Boolean test;

            do {
                test = false;
                Random r = new Random();
                posX = r.nextInt(MAP_SIZE_X);
                posY = r.nextInt(MAP_SIZE_Y);

                for (Shelf shelf : shelfList) {
                    if ((shelf.getXPos() == posX) && (shelf.getYPos() == posY)) {
                        test = true;
                        break;
                    }
                }
            } while (test);

            weight = ran.nextInt(20) + 5;
            if (weight >= 10)
            {
                type = "Heavy";
            } else {
                type = "Light";
            }

            packageList.add(new Package(gc, packageType, tileWidth, tileHeight, posX, posY, weight, type));
        }
    }

    public void renderMap() {
        drawFloor(getGc(), floorImg, tileWidth, tileHeight);
        shelfList.forEach(GameObject::render);
        for (Package pkg : packageList) {
            if (!pkg.isHidden()) {
                pkg.render();
            }
        }
        forklift.render();
    }

    public Forklift getForklift() {
        return forklift;
    }

    public float getTileHeight() {
        return tileHeight;
    }

    public float getTileWidth() {
        return tileWidth;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }
}