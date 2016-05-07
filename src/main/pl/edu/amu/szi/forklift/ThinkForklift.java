package main.pl.edu.amu.szi.forklift;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.pl.edu.amu.szi.forklift.object.Forklift;
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

    ArrayList<Shelf> shelfList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        float tileWidth = CANVAS_WIDTH / MAP_SIZE_X;
        float tileHeight = CANVAS_HEIGHT / MAP_SIZE_Y;

        double screenWidthResolution = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeightResolution = Screen.getPrimary().getVisualBounds().getHeight();

        theStage.setTitle("Think Forklift");

        Group root = new Group();
        Scene theScene = new Scene(root);

        theStage.setScene(theScene);

        HashSet<String> currentlyActiveKeys = new HashSet<>();
        StackPane holder = new StackPane();
        holder.setPrefSize(screenWidthResolution, screenHeightResolution);

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        holder.getChildren().add(canvas);
        root.getChildren().add(holder);

        holder.setStyle("-fx-background-color: black");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image floorImage = new Image(getClass().
                getResourceAsStream("/img/podloga2.png"), tileWidth, tileHeight, false, false);

        Forklift forkLift = new Forklift(gc, "/img/forklift.png", tileWidth, tileHeight, 0, 0);

        shelfList = new ArrayList<>();

        for (int k = 0; k < 15; k = k + 3) {
            for (int j = 5; j < 15; j++) {
                shelfList.add(new Shelf(gc, "/img/polkidwie.png", tileWidth, tileHeight, j, k));
            }
        }

        ArrayList<Package> packageList = new ArrayList();

        for (int k = 0; k < PACKAGE_COUNT; k++) {
            int posX = 0;
            int posY = 0;

            Boolean test;

            do {
                test = false;
                Random r = new Random();
                int low = 0;
                int high = 15;
                posX = r.nextInt(15);
                posY = r.nextInt(15);

                for (int i = 0; i < shelfList.size(); i++) {
                    if ((shelfList.get(i).getXPos() == posX) && (shelfList.get(i).getYPos() == posY)) {
                        test = true;
                        break;
                    }
                }
            } while (test);

            packageList.add(new Package(gc, "/img/paczka12.png", tileWidth, tileHeight, posX, posY));
        }

        for (int k = 0; k < PACKAGE_COUNT; k++) {
            int posX = 0;
            int posY = 0;

            Boolean test;

            do {
                test = false;
                Random r = new Random();
                int low = 0;
                int high = 15;
                posX = r.nextInt(15);
                posY = r.nextInt(15);

                for (int i = 0; i < shelfList.size(); i++) {
                    if ((shelfList.get(i).getXPos() == posX) && (shelfList.get(i).getYPos() == posY)) {
                        test = true;
                        break;
                    }
                }
            } while (test);

            packageList.add(new Package(gc, "/img/paczka22.png", tileWidth, tileHeight, posX, posY));
        }

        for (int k = 0; k < PACKAGE_COUNT; k++) {
            int posX = 0;
            int posY = 0;

            Boolean test;

            do {
                test = false;
                Random r = new Random();
                int low = 0;
                int high = 15;
                posX = r.nextInt(15);
                posY = r.nextInt(15);

                for (int i = 0; i < shelfList.size(); i++) {
                    if ((shelfList.get(i).getXPos() == posX) && (shelfList.get(i).getYPos() == posY)) {
                        test = true;
                        break;
                    }
                }
            } while (test);

            packageList.add(new Package(gc, "/img/paczka32.png", tileWidth, tileHeight, posX, posY));
        }

        theScene.setOnKeyPressed(event -> currentlyActiveKeys.add(event.getCode().toString()));

        theStage.setWidth(screenWidthResolution);
        theStage.setHeight(screenHeightResolution);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                gc.clearRect(0, 0, screenWidthResolution, screenHeightResolution);

                handleInput(currentlyActiveKeys, forkLift);

                drawFloor(gc, floorImage, tileWidth, tileHeight);

                shelfList.forEach((shelf -> shelf.render()));

                packageList.forEach((packageOb -> packageOb.render()));

                forkLift.render();

                currentlyActiveKeys.clear();
            }
        }.start();

        theStage.show();
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
            if ((shelf.getXPos() == nextXPos)
                    && (shelf.getYPos() == nextYPos)) {
                return true;
            }
        }
        return false;
    }

    private void drawFloor(GraphicsContext gc, Image image, float width, float height) {
        for (int i = 0; i < MAP_SIZE_X; i++) {
            for (int j = 0; j < MAP_SIZE_Y; j++) {
                gc.drawImage(image, i * width, j * height);
            }
        }

    }
}
