package pl.edu.amu.szi.forklift;

import javafx.application.Platform;
import pl.edu.amu.szi.forklift.Astar.Astar;
import pl.edu.amu.szi.forklift.Astar.DestinationUnreachableException;
import pl.edu.amu.szi.forklift.utils.Node;
import pl.edu.amu.szi.forklift.IDAstar.IDAstar;
import pl.edu.amu.szi.forklift.objects.Forklift;
import pl.edu.amu.szi.forklift.objects.Package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class ForkliftController {

    private Forklift forklift;


    private static int xDestination, yDestination;
    public ForkliftController(Forklift forklift) {
        this.forklift = forklift;
    }

    public void handleInput(HashSet currentKeys, HashMap currentMouseClick) {
        Map map = Map.getInstance();
        int marginL = Integer.valueOf((int)Math.round(map.getGc().getCanvas().localToScene(map.getGc().getCanvas().getBoundsInLocal()).getMinX()));
        int marginT = Integer.valueOf((int)Math.round(map.getGc().getCanvas().localToScene(map.getGc().getCanvas().getBoundsInLocal()).getMinY()));

        Double tileWidth = Double.valueOf(map.getTileWidth());
        Double tileHeight = Double.valueOf(map.getTileHeight());
        if(!currentMouseClick.isEmpty()) {
            Double xCoord = Double.parseDouble(currentMouseClick.get("X").toString());
            Double yCoord = Double.parseDouble(currentMouseClick.get("Y").toString());
            xDestination = (xCoord.intValue() - marginL) / tileWidth.intValue();
            yDestination = (yCoord.intValue() - marginT) / tileHeight.intValue();
        }
        if (currentKeys.contains("LEFT")) {
            if (map.isPassable(forklift.getXPos() - 1, forklift.getYPos())) {
                forklift.moveLeft();
                System.out.println("X:"+Integer.toString(forklift.getXPos())+ " Y:"+Integer.toString(forklift.getYPos()));
            }
        } else if (currentKeys.contains("RIGHT")) {
            if (map.isPassable(forklift.getXPos() + 1, forklift.getYPos())) {
                forklift.moveRight();
                System.out.println("X:"+Integer.toString(forklift.getXPos())+ " Y:"+Integer.toString(forklift.getYPos()));
            }
        } else if (currentKeys.contains("UP")) {
            if (map.isPassable(forklift.getXPos(), forklift.getYPos() - 1)) {
                forklift.moveUp();
                System.out.println("X:"+Integer.toString(forklift.getXPos())+ " Y:"+Integer.toString(forklift.getYPos()));
            }
        } else if (currentKeys.contains("DOWN")) {
            if (map.isPassable(forklift.getXPos(), forklift.getYPos() + 1)) {
                forklift.moveDown();
                System.out.println("X:"+Integer.toString(forklift.getXPos())+ " Y:"+Integer.toString(forklift.getYPos()));
            }
//      pick up package
        } else if (currentKeys.contains("SPACE")) {
            if (map.packageFound(forklift.getXPos(), forklift.getYPos())) {
                if (forklift.hasCargo()) {
                    forklift.dropCargo();
                } else {
                    forklift.pickUpCargo(map.getPackageAtPos(forklift.getXPos(), forklift.getYPos()));
                }
            }
        } else if (currentKeys.contains("P")){
            if (map.packageFound(forklift.getXPos(), forklift.getYPos())) {
                if(!map.isPassable(forklift.getXPos(),forklift.getYPos()-1)){ // check if shelf is above
                    if (forklift.hasCargo() &&
                        map.getShelfAtPos(forklift.getXPos(),forklift.getYPos()-1).addPkg(forklift.getCargo())) {
                        forklift.dropCargo();
                        map.removePackageAtPos(forklift.getXPos(),forklift.getYPos());
                    }
                }
            }
        } else if (currentKeys.contains("A")) {
            try {
                moveForkliftAstar(xDestination, yDestination);
            } catch (DestinationUnreachableException e) {
                e.printStackTrace();
            }
        }
        else if(currentKeys.contains("I"))
        {
            try
            {
                moveForkliftIDAstar(xDestination, yDestination);
            }
            catch(DestinationUnreachableException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void moveForkliftIDAstar(int destinationX, int destinationY) throws DestinationUnreachableException {
        IDAstar idastar = new IDAstar();
        System.out.println(forklift.getXPos() + " " + forklift.getYPos());
        System.out.println(destinationX + " " + destinationY);
        ArrayList<Node> list = (ArrayList<Node>) idastar.findPath(forklift.getXPos(), forklift.getYPos(),
                destinationX, destinationY);
        if (list == null) {
            System.out.println("Punkt docelowy niedostępny");
            throw new DestinationUnreachableException();
        }

        moveForklift(list);
    }

    public void moveForkliftAstar(int destinationX, int destinationY) throws DestinationUnreachableException {
        Astar astar = new Astar();
        ArrayList<Node> list = (ArrayList<Node>) astar.findPath(forklift.getXPos(), forklift.getYPos(),
                destinationX, destinationY);
        if (list == null) {
            System.out.println("Punkt docelowy niedostępny");
            throw new DestinationUnreachableException();
        }

        moveForklift(list);
    }

    public void moveForklift(ArrayList<Node> list)
    {
        for(int i = list.size() - 1; i >= 0; i--)
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(500);
                Platform.runLater(() -> Map.getInstance().renderMap());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            int newPosX = list.get(i).getPosX();
            int newPosY = list.get(i).getPosY();
            System.out.println("Forklift moving: [" + forklift.getXPos() + "," + forklift.getYPos() + "] --> [" + newPosX + "," + newPosY + "]");
            forklift.setPosition(newPosX, newPosY);
        }
        System.out.println("Wózek dotarł do celu.");
    }

}
