package pl.edu.amu.szi.forklift;

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
import pl.edu.amu.szi.forklift.objects.Forklift;
import pl.edu.amu.szi.forklift.objects.GameObject;
import pl.edu.amu.szi.forklift.objects.Shelf;
import pl.edu.amu.szi.forklift.objects.Package;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static pl.edu.amu.szi.forklift.utils.Constants.*;

public class ThinkForklift extends Application {

    private GraphicsContext gc;
    private HashSet<String> currentlyActiveKeys;

    private Image floorImg;
    public static Forklift forkLift;

    private float tileWidth;
    private float tileHeight;
    private double screenWidthResolution;
    private double screenHeightResolution;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        prepareScene(theStage);
        prepareObjects();

        new MainGameLoop().start();

        theStage.show();
    }

    private void prepareScene(Stage theStage) {
        theStage.setTitle(APP_TITLE);

        tileWidth = CANVAS_WIDTH / MAP_SIZE_X;
        tileHeight = CANVAS_HEIGHT / MAP_SIZE_Y;
        screenWidthResolution = Screen.getPrimary().getVisualBounds().getWidth();
        screenHeightResolution = Screen.getPrimary().getVisualBounds().getHeight();

        Group root = new Group();
        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);

        Scene theScene = new Scene(root);
        theScene.setFill(Color.BLACK);

        theStage.setScene(theScene);

//         input key hashmap
        currentlyActiveKeys = new HashSet<>();
        StackPane holder = new StackPane();
        holder.setPrefSize(screenWidthResolution, screenHeightResolution);

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setCache(true);
        canvas.setCacheHint(CacheHint.SPEED);

        holder.getChildren().add(canvas);
        holder.setCache(true);
        holder.setCacheHint(CacheHint.SPEED);

//         add Pane to main scene
        root.getChildren().add(holder);

        gc = canvas.getGraphicsContext2D();

        theStage.setWidth(screenWidthResolution);
        theStage.setHeight(screenHeightResolution);

//         handle keyboard input
        theScene.setOnKeyPressed(event -> currentlyActiveKeys.add(event.getCode().toString()));
    }

    private void prepareObjects() {
        Map.shelfList = new ArrayList<>();
        Map.packageList = new ArrayList<>();

        System.out.println(getClass().getResource("").getPath());
        floorImg = new Image(getClass().getResourceAsStream(IMG_PODLOGA), tileWidth, tileHeight, false, false);
        forkLift = new Forklift(gc, IMG_FORKLIFT, tileWidth, tileHeight, 0, 0);

        create_shelves();
        create_packages(IMG_PACZKA12);
        create_packages(IMG_PACZKA22);
        create_packages(IMG_PACZKA32);
    }

    private void create_shelves() {
        for (int k = 0; k < MAP_SIZE_X; k = k + 3) {
            for (int j = 5; j < MAP_SIZE_Y; j++) {
                Map.shelfList.add(new Shelf(gc, IMG_POLKIDWIE, tileWidth, tileHeight, j, k));
            }
        }
    }

    private void create_packages(String packageType) {
        for (int k = 0; k < PACKAGE_COUNT; k++) {
            int posX;
            int posY;

            Boolean test;

            do {
                test = false;
                Random r = new Random();
                posX = r.nextInt(MAP_SIZE_X);
                posY = r.nextInt(MAP_SIZE_Y);

                for (Shelf shelf : Map.shelfList) {
                    if ((shelf.getXPos() == posX) && (shelf.getYPos() == posY)) {
                        test = true;
                        break;
                    }
                }
            } while (test);

            Map.packageList.add(new Package(gc, packageType, tileWidth, tileHeight, posX, posY));
        }
    }

    private class MainGameLoop extends AnimationTimer {

        @Override
        public void handle(long now) {
            gc.clearRect(0, 0, screenWidthResolution, screenHeightResolution);

            ForkliftController.handleInput(currentlyActiveKeys);

            Map.drawFloor(gc, floorImg, tileWidth, tileHeight);

            // skrócona forma
            Map.shelfList.forEach(GameObject::render);

            Map.packageList.forEach(GameObject::render);

            forkLift.render();

            currentlyActiveKeys.clear();
        }
    }
}
