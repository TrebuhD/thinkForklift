package pl.edu.amu.szi.forklift;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import javafx.application.Platform;
import pl.edu.amu.szi.forklift.Astar.Astar;
import pl.edu.amu.szi.forklift.Astar.DestinationUnreachableException;
import pl.edu.amu.szi.forklift.c45.start;
import pl.edu.amu.szi.forklift.objects.Package;
import pl.edu.amu.szi.forklift.objects.Shelf;
import pl.edu.amu.szi.forklift.utils.Node;
import pl.edu.amu.szi.forklift.IDAstar.IDAstar;
import pl.edu.amu.szi.forklift.objects.Forklift;

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
        int marginL = (int) Math.round(map.getGc().getCanvas().localToScene
                (map.getGc().getCanvas().getBoundsInLocal()).getMinX());
        int marginT = (int) Math.round(map.getGc().getCanvas().localToScene(
                map.getGc().getCanvas().getBoundsInLocal()).getMinY());

        Double tileWidth = (double) map.getTileWidth();
        Double tileHeight = (double) map.getTileHeight();
        if(!currentMouseClick.isEmpty()) {
            Double xCoord = Double.parseDouble(currentMouseClick.get("X").toString());
            Double yCoord = Double.parseDouble(currentMouseClick.get("Y").toString());
            xDestination = (xCoord.intValue() - marginL) / tileWidth.intValue();
            yDestination = (yCoord.intValue() - marginT) / tileHeight.intValue();
        }
        if (currentKeys.contains("LEFT")) {
            if (map.isPassable(forklift.getXPos() - 1, forklift.getYPos())) {
                forklift.moveLeft();
                System.out.println("X:"+Integer.toString(forklift.getXPos())+
                        " Y:"+Integer.toString(forklift.getYPos()));
            }
        } else if (currentKeys.contains("RIGHT")) {
            if (map.isPassable(forklift.getXPos() + 1, forklift.getYPos())) {
                forklift.moveRight();
                System.out.println("X:"+Integer.toString(forklift.getXPos())+
                        " Y:"+Integer.toString(forklift.getYPos()));
            }
        } else if (currentKeys.contains("UP")) {
            if (map.isPassable(forklift.getXPos(), forklift.getYPos() - 1)) {
                forklift.moveUp();
                System.out.println("X:"+Integer.toString(forklift.getXPos())+
                        " Y:"+Integer.toString(forklift.getYPos()));
            }
        } else if (currentKeys.contains("DOWN")) {
            if (map.isPassable(forklift.getXPos(), forklift.getYPos() + 1)) {
                forklift.moveDown();
                System.out.println("X:"+Integer.toString(forklift.getXPos())+
                        " Y:"+Integer.toString(forklift.getYPos()));
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
        } else if (currentKeys.contains("P")){ // put on shelf
            putOnShelf();
        } else if (currentKeys.contains("T")){ // take from shelf
            takeFromShelf();
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
        else if (currentKeys.contains("D")) {
            deliverPackagesToShelves(map.getPackageList(), map.getShelfList());
        }
    }

    public void putOnShelf()
    {
        Map map = Map.getInstance();
        if (map.packageFound(forklift.getXPos(), forklift.getYPos())) {
            if(!map.isPassable(forklift.getXPos(),forklift.getYPos()-1)){ // check if shelf is above
                if (forklift.hasCargo() &&
                        map.getShelfAtPos(forklift.getXPos(),forklift.getYPos()-1).addPkg(forklift.getCargo()))
                {
                    forklift.dropCargo();
                    System.out.println("PKG added to shelf.");
                    map.hidePackageAtPos(forklift.getXPos(),forklift.getYPos());
                }
                else
                {
                    System.out.println("Error: Cant put a pkg there! Or there is a map edge above!");
                }
            }
            else
            {
                System.out.println("Error: There is no shelf above forklift!");
            }
        }
        else
        {
            System.out.println("Error: Forklift does not have any pkg!\n");
        }
    }

    public void takeFromShelf()
    {
        Map map = Map.getInstance();
        if(!map.isPassable(forklift.getXPos(),forklift.getYPos()-1)) { // check if shelf is above
            if (!forklift.hasCargo() &&
                    map.getShelfAtPos(forklift.getXPos(), forklift.getYPos() - 1).notEmpty())
            {
                System.out.print("Taking pkg!");
                map.spawnPackageAtPos(forklift.getXPos(), forklift.getYPos(),
                        map.getShelfAtPos(forklift.getXPos(), forklift.getYPos() - 1).getTopPackageFromShelf());
                forklift.pickUpCargo(map.getPackageAtPos(forklift.getXPos(), forklift.getYPos()));
            }
            else
            {
                System.out.println("Error: There are no pkg's on the shelf!");
            }
        }
        else
        {
            System.out.println("Error: There is no shelf above me!");
        }
    }

    public void deliverPackagesToShelves(ArrayList<Package> packages, ArrayList<Shelf> shelves) {
        int currShelfIndex = 0;
        int iterator = 0;
        int currentRow = 0;
        Map map = Map.getInstance();
        ArrayList<String> classified = map.getClassification();
        ArrayList<Integer> currentShelfRow = new ArrayList<>();
        currentShelfRow.add(0,0);
        currentShelfRow.add(1,11);
        currentShelfRow.add(2,21);
        currentShelfRow.add(3,31);
        currentShelfRow.add(4,41);
        currentShelfRow.add(5,51);

        getPkgClassification(packages);

        for (Package pkg : packages) {
            try {
                switch(classified.get(iterator)){
                    case "C1":
                        currentRow = 0;
                        break;
                    case "C2":
                        currentRow = 1;
                        break;
                    case "C3":
                        currentRow = 2;
                        break;
                    case "C4":
                        currentRow = 3;
                        break;
                }
                System.out.println("Pkg category is:" + classified.get(iterator) +" shelf row:" + currentRow);
                Shelf currShelf = shelves.get(currentShelfRow.get(currentRow));
                if (currShelf.checkShelf(pkg)) {
                    deliverPackage(pkg, currShelf);
                } else {
                    //currShelfIndex += 1;
                    currentShelfRow.set(currentRow,currentShelfRow.get(currentRow)+1);
                    //currShelf = shelves.get(currShelfIndex)
                    currShelf = shelves.get(currentShelfRow.get(currentRow));
                    deliverPackage(pkg, currShelf);
                }
            } catch (DestinationUnreachableException e) {
                System.out.println("Destination Unreachable");
                e.printStackTrace();
            }
        iterator ++;
        }
    }

    private void getPkgClassification(ArrayList<Package> packages)
    {
        String input = new String();
        String line = new String();
        for (Package pkg : packages)
        {
            line = pkg.getType() + "," + pkg.getWeight() + "," + pkg.getValue() + ",C1";
            if(input.length()>1)
            {
                input = input + ":" + line;
            } else {
             input = line;
            }
        }
        start.generate(input);
    }

    public void deliverPackage(Package pkg, Shelf shelf) throws DestinationUnreachableException {
        int pkgX = pkg.getXPos();
        int pkgY = pkg.getYPos();
        int shelfX = shelf.getXPos();
        int shelfY = shelf.getYPos();
        moveForkliftAstar(pkgX, pkgY);
        forklift.pickUpCargo(Map.getInstance().getPackageAtPos(forklift.getXPos(), forklift.getYPos()));
        moveForkliftAstar(shelfX, shelfY + 1);
        putOnShelf();
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
                TimeUnit.MILLISECONDS.sleep(150);
                Platform.runLater(() -> Map.getInstance().renderMap());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            int newPosX = list.get(i).getPosX();
            int newPosY = list.get(i).getPosY();
//            System.out.println("Forklift moving: [" + forklift.getXPos() + "," + forklift.getYPos() + "] --> [" + newPosX + "," + newPosY + "]");
            forklift.setPosition(newPosX, newPosY);
            if (forklift.hasCargo()) {
                forklift.getCargo().setPosition(newPosX, newPosY);
            }
        }
        System.out.println("Wózek dotarł do celu.");
    }

}
