package pl.edu.amu.szi.forklift;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pl.edu.amu.szi.forklift.objects.Forklift;

import java.util.HashSet;

import static pl.edu.amu.szi.forklift.utils.Constants.*;

public class ThinkForklift extends Application {

    private GraphicsContext gc;
    private HashSet<String> currentlyActiveKeys;

    private double screenWidth;
    private double screenHeight;

    private Map map;

    private Forklift forklift;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        map = Map.getInstance();

        prepareScene(theStage);

        forklift = map.getForklift();

        new MainGameLoop().start();

        theStage.show();
    }

    private void prepareScene(Stage theStage) {
        theStage.setTitle(APP_TITLE);

        screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        Group root = new Group();
        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);

        Scene theScene = new Scene(root);
        theScene.setFill(Color.BLACK);

        theStage.setScene(theScene);

//         input key hashmap
        currentlyActiveKeys = new HashSet<>();
        StackPane holder = new StackPane();
        holder.setPrefSize(screenWidth, screenHeight);

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setCache(true);
        canvas.setCacheHint(CacheHint.SPEED);

        holder.getChildren().add(canvas);
        holder.setCache(true);
        holder.setCacheHint(CacheHint.SPEED);

//         add Pane to main scene
        root.getChildren().add(holder);

        gc = canvas.getGraphicsContext2D();

        map.prepareObjects(gc);

        theStage.setWidth(screenWidth);
        theStage.setHeight(screenHeight);

//         handle keyboard input
        theScene.setOnKeyPressed(event -> currentlyActiveKeys.add(event.getCode().toString()));
    }


    private class MainGameLoop extends AnimationTimer {

        @Override
        public void handle(long now) {

            gc.clearRect(0, 0, screenWidth, screenHeight);

            ForkliftController controller = new ForkliftController(map.getForklift());

            controller.handleInput(currentlyActiveKeys);

            map.renderMap();

            currentlyActiveKeys.clear();
        }
    }
}
