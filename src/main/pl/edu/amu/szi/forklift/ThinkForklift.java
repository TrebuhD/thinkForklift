package main.pl.edu.amu.szi.forklift;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.event.EventHandler;
import java.util.HashSet;
import javafx.animation.AnimationTimer;
import main.pl.edu.amu.szi.forklift.object.Forklift;
import main.pl.edu.amu.szi.forklift.object.Package;
import main.pl.edu.amu.szi.forklift.object.Shelf;

import java.util.ArrayList;
import java.util.Random;

public class ThinkForklift extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {

        int x = 15;
        int y = 15;

        int canvasWidth = 600;
        int canvasHeight = 600;

        float tileWidth = canvasWidth / x;
        float tileHeight = canvasHeight / y;
        
        int shelfAmount = 20;
        int packageAmount = 15;

        double screenWidthResolution = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeightResolution = Screen.getPrimary().getVisualBounds().getHeight();

        theStage.setTitle("Replace this text");

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);

        HashSet<String> currentlyActiveKeys = new HashSet<>();

        StackPane holder = new StackPane();
        holder.setPrefSize(screenWidthResolution, screenHeightResolution);

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);

        holder.getChildren().add(canvas);
        root.getChildren().add(holder);

        holder.setStyle("-fx-background-color: black");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image tileImg = new Image(getClass().getResourceAsStream("/img/podloga2.png"), tileWidth, tileHeight, false, false);

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                gc.drawImage(tileImg, i * tileWidth, j * tileHeight);
            }
        }

        Forklift forkLift = new Forklift(gc, "/img/forklift.png", tileWidth, tileHeight, 0, 0);
        
        ArrayList<Shelf> shelfList = new ArrayList();
        
        for(int k=0; k<15; k=k+3) {
            for(int j=5; j<15; j++) {
                shelfList.add(new Shelf(gc, "/img/polkidwie.png", tileWidth, tileHeight, j, k));
            }
        }
        
        ArrayList<Package> packageList = new ArrayList();
        
        for(int k=0; k<packageAmount; k++) {
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
                
                for(int i=0; i < shelfList.size(); i++) {
                    if((shelfList.get(i).getXPos() == posX) && (shelfList.get(i).getYPos() == posY)){
                        test = true;
                        break;
                    }
                }
            } while (test);
           
            packageList.add(new Package(gc, "/img/paczka12.png", tileWidth, tileHeight, posX, posY));
        }
        
        for(int k=0; k<packageAmount; k++) {
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
                
                for(int i=0; i < shelfList.size(); i++) {
                    if((shelfList.get(i).getXPos() == posX) && (shelfList.get(i).getYPos() == posY)){
                        test = true;
                        break;
                    }
                }
            } while (test);
           
            packageList.add(new Package(gc, "/img/paczka22.png", tileWidth, tileHeight, posX, posY));
        }
        
        for(int k=0; k<packageAmount; k++) {
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
                
                for(int i=0; i < shelfList.size(); i++) {
                    if((shelfList.get(i).getXPos() == posX) && (shelfList.get(i).getYPos() == posY)){
                        test = true;
                        break;
                    }
                }
            } while (test);
           
            packageList.add(new Package(gc, "/img/paczka32.png", tileWidth, tileHeight, posX, posY));
        }
       
        theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                currentlyActiveKeys.add(event.getCode().toString());
            }
        });

        theStage.setWidth(screenWidthResolution);
        theStage.setHeight(screenHeightResolution);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                gc.clearRect(0, 0, screenWidthResolution, screenHeightResolution);
                
                int nextXpos = 0;
                int nextYpos = 0;

                if (currentlyActiveKeys.contains("LEFT")) {
                    nextXpos = -1;
                    //forkLift.moveLeft()
                } else if (currentlyActiveKeys.contains("RIGHT")) {
                    nextXpos = 1;
                    //forkLift.moveRight();
                } else if (currentlyActiveKeys.contains("UP")) {
                    nextYpos = -1;
                    //forkLift.moveUp();
                } else if (currentlyActiveKeys.contains("DOWN")) {
                    nextYpos = 1;
                    //forkLift.moveDown();
                }
                
                int nextXPos = forkLift.getXPos() + nextXpos;
                int nextYPos = forkLift.getYPos() + nextYpos;
                
                if(!checkIfCollision(shelfList, nextXPos, nextYPos)) {
                    forkLift.setPosition(nextXPos, nextYPos);
                } 
               
                for (int i = 0; i < x; i++) {
                    for (int j = 0; j < y; j++) {
                        gc.drawImage(tileImg, i * tileWidth, j * tileHeight);
                    }
                }
                
                shelfList.forEach((shelf -> shelf.render()));
                
                packageList.forEach((packageOb -> packageOb.render()));

                forkLift.render();
                
                currentlyActiveKeys.clear();
            }
        }.start();

        theStage.show();
    }
    
    public static boolean checkIfCollision(ArrayList<Shelf> shelfList, int nextXPos,int nextYPos) {
        for (Shelf aShelfList : shelfList) {
            if ((aShelfList.getXPos() == nextXPos) && (aShelfList.getYPos() == nextYPos)
                    || (nextXPos < 0)
                    || (nextXPos > 14)
                    || (nextYPos < 0)
                    || (nextYPos > 14)) {
                return true;
            }
        }
       return false;
    }
}
