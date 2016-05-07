package main.pl.edu.amu.szi.forklift;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.pl.edu.amu.szi.forklift.object.Forklift;
import main.pl.edu.amu.szi.forklift.object.GameObject;
import main.pl.edu.amu.szi.forklift.object.Package;
import main.pl.edu.amu.szi.forklift.object.Shelf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class ThinkForklift extends Application {
    public static final int MAP_SIZE_X = 15;
    public static final int MAP_SIZE_Y = 15;
    public static final int PACKAGE_COUNT = 15;
    public static final int SHELF_COUNT = 20;
    public static final int CANVAS_WIDTH = 600;
    public static final int CANVAS_HEIGHT = 600;

    private GraphicsContext gc;
    private HashSet<String> currentlyActiveKeys;

    private ArrayList<Shelf> shelfList;
    private ArrayList<Package> packageList;

    private Image floorImg;
    private Forklift forkLift;

    private float tileWidth;
    private float tileHeight;
    double screenWidthResolution;
    double screenHeightResolution;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        Scene theScene = prepareScene(theStage);
        prepareObjects();

        new MainGameLoop().start();

        theStage.show();
    }

    private Scene prepareScene(Stage theStage) {
        tileWidth = CANVAS_WIDTH / MAP_SIZE_X;
        tileHeight = CANVAS_HEIGHT / MAP_SIZE_Y;

        screenWidthResolution = Screen.getPrimary().getVisualBounds().getWidth();
        screenHeightResolution = Screen.getPrimary().getVisualBounds().getHeight();

        theStage.setTitle("Think Forklift");

        Group root = new Group();
        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);

        Scene theScene = new Scene(root);
        theScene.setFill(Color.BLACK);

        theStage.setScene(theScene);

        currentlyActiveKeys = new HashSet<>();
        StackPane holder = new StackPane();
        holder.setPrefSize(screenWidthResolution, screenHeightResolution);

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setCache(true);
        canvas.setCacheHint(CacheHint.SPEED);

        holder.getChildren().add(canvas);
        holder.setCache(true);
        holder.setCacheHint(CacheHint.SPEED);

        root.getChildren().add(holder);

        gc = canvas.getGraphicsContext2D();

        theStage.setWidth(screenWidthResolution);
        theStage.setHeight(screenHeightResolution);

        theScene.setOnKeyPressed(event -> currentlyActiveKeys.add(event.getCode().toString()));

        return theScene;
    }

    private void prepareObjects() {
        shelfList = new ArrayList<>();
        packageList = new ArrayList<>();

        floorImg = new Image(getClass().getResourceAsStream("/img/podloga2.png"), tileWidth, tileHeight, false, false);
        forkLift = new Forklift(gc, "/img/forklift.png", tileWidth, tileHeight, 0, 0);

        create_shelves();
        create_packages("/img/paczka12.png");
        create_packages("/img/paczka22.png");
        create_packages("/img/paczka32.png");
    }

    private void create_shelves() {
        for (int k = 0; k < MAP_SIZE_X; k = k + 3) {
            for (int j = 5; j < MAP_SIZE_Y; j++) {
                shelfList.add(new Shelf(gc, "/img/polkidwie.png", tileWidth, tileHeight, j, k));
            }
        }
    }

    private void create_packages(String packageType) {
        for (int k = 0; k < PACKAGE_COUNT; k++) {
            int posX;
            int posY;

            Boolean test;

            // Tosia: co ten kod robi dokładnie?
            do {
                test = false;
                Random r = new Random();
                int low = 0;
                int high = 15;
                posX = r.nextInt(15);
                posY = r.nextInt(15);

                for (Shelf shelf : shelfList) {
                    if ((shelf.getXPos() == posX) && (shelf.getYPos() == posY)) {
                        test = true;
                        break;
                    }
                }
            } while (test);

            packageList.add(new Package(gc, packageType, tileWidth, tileHeight, posX, posY));
        }
    }

    private void drawFloor(GraphicsContext gc, Image image, float width, float height) {
        for (int i = 0; i < MAP_SIZE_X; i++) {
            for (int j = 0; j < MAP_SIZE_Y; j++) {
                gc.drawImage(image, i * width, j * height);
            }
        }
    }


    private void handleInput(HashSet currentKeys, Forklift forklift) {
        if (currentKeys.contains("LEFT")) {
            if (!collisionFound(forklift.getXPos() - 1, forklift.getYPos())) {
                forklift.moveLeft();
            }
        } else if (currentKeys.contains("RIGHT")) {
            if (!collisionFound(forklift.getXPos() + 1, forklift.getYPos())) {
                forklift.moveRight();
            }
        } else if (currentKeys.contains("UP")) {
            if (!collisionFound(forklift.getXPos(), forklift.getYPos() - 1)) {
                forklift.moveUp();
            }
        } else if (currentKeys.contains("DOWN")) {
            if (!collisionFound(forklift.getXPos(), forklift.getYPos() + 1)) {
                forklift.moveDown();
            }
        }
    }

    public boolean collisionFound(int nextXPos, int nextYPos) {
        // collision with shelf
        for (Shelf shelf : shelfList) {
            if ((shelf.getXPos() == nextXPos) && (shelf.getYPos() == nextYPos)) {
                return true;
            }
        }
        return false;
    }

    private class MainGameLoop extends AnimationTimer {

        @Override
        public void handle(long now) {
            gc.clearRect(0, 0, screenWidthResolution, screenHeightResolution);

            handleInput(currentlyActiveKeys, forkLift);

            drawFloor(gc, floorImg, tileWidth, tileHeight);

            // skrócona forma
            shelfList.forEach(GameObject::render);

            packageList.forEach(GameObject::render);

            forkLift.render();

            currentlyActiveKeys.clear();
        }
    }
}
