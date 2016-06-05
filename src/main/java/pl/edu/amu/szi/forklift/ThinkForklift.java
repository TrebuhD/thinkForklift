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
import pl.edu.amu.szi.forklift.utils.Distance;

import java.util.*;

import static pl.edu.amu.szi.forklift.utils.Constants.*;

public class ThinkForklift extends Application {

    private GraphicsContext gc;
    private HashSet<String> currentlyActiveKeys;
    private HashMap<String, Double> currentMouseClick;

    private double screenWidth;
    private double screenHeight;

    private Map map;

    private ForkliftController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage)
	{
        map = Map.getInstance();

        prepareScene(theStage);
		Properties p = new Properties();
		//InputStream i = null;
		int count = 0;
        String metric = "";
		try
		{

			p.load(getClass().getResourceAsStream(PROPERTIES));
			count = Integer.parseInt(p.getProperty("zbierz.paczek"));
            metric = p.getProperty("metryka");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
        Distance.setMetric(metric);
        Forklift forklift = map.getForklift();
		forklift.setNoTasks(count);

        new RenderUiTimer().start();

        theStage.show();

        // Controller thread
		controller = new ForkliftController(forklift);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
				controller = new ForkliftController(forklift);
                controller.handleInput(currentlyActiveKeys, currentMouseClick);
                currentlyActiveKeys.clear();
                currentMouseClick.clear();
            }
        }, 0, 50);
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

        currentlyActiveKeys = new HashSet<>();
        currentMouseClick = new HashMap<>();
        StackPane holder = new StackPane();
        holder.setPrefSize(screenWidth, screenHeight);

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setCache(true);
        canvas.setCacheHint(CacheHint.SPEED);

        holder.getChildren().add(canvas);
        holder.setCache(true);
        holder.setCacheHint(CacheHint.SPEED);

//         add Pane to start scene
        root.getChildren().add(holder);

        gc = canvas.getGraphicsContext2D();

        map.prepareObjects(gc);

        theStage.setWidth(screenWidth);
        theStage.setHeight(screenHeight);

//         handle keyboard input
        theScene.setOnKeyPressed(event -> currentlyActiveKeys.add(event.getCode().toString()));

        theScene.setOnMouseClicked(event -> {currentMouseClick.put("X", event.getSceneX()); currentMouseClick.put("Y", event.getSceneY());});
    }

    private class RenderUiTimer extends AnimationTimer {

        @Override
        public void handle(long now) {
            gc.clearRect(0, 0, screenWidth, screenHeight);
            map.renderMap();
        }
    }
}
