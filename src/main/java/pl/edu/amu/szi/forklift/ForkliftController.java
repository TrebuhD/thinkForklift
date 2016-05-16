package pl.edu.amu.szi.forklift;

import pl.edu.amu.szi.forklift.Astar.Astar;
import pl.edu.amu.szi.forklift.Astar.DestinationUnreachableException;
import pl.edu.amu.szi.forklift.Astar.Node;
import pl.edu.amu.szi.forklift.objects.Forklift;

import java.util.ArrayList;
import java.util.HashSet;

public class ForkliftController {

    private Forklift forklift;

    public ForkliftController(Forklift forklift) {
        this.forklift = forklift;
    }

    public void handleInput(HashSet currentKeys) {
        Map map = Map.getInstance();

        if (currentKeys.contains("LEFT")) {
            if (map.isPassable(forklift.getXPos() - 1, forklift.getYPos())) {
                forklift.moveLeft();
            }
        } else if (currentKeys.contains("RIGHT")) {
            if (map.isPassable(forklift.getXPos() + 1, forklift.getYPos())) {
                forklift.moveRight();
            }
        } else if (currentKeys.contains("UP")) {
            if (map.isPassable(forklift.getXPos(), forklift.getYPos() - 1)) {
                forklift.moveUp();
            }
        } else if (currentKeys.contains("DOWN")) {
            if (map.isPassable(forklift.getXPos(), forklift.getYPos() + 1)) {
                forklift.moveDown();
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
        } else if (currentKeys.contains("A")) {
            try {
                moveForklift(13,13);
            } catch (DestinationUnreachableException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveForklift(int destinationX, int destinationY) throws DestinationUnreachableException {
        Astar astar = new Astar(forklift);
        ArrayList<Node> list = (ArrayList<Node>) astar.findPath(forklift.getXPos(), forklift.getYPos(),
                destinationX, destinationY);
        if (list == null) {
            System.out.println("Punkt docelowy niedostępny");
            throw new DestinationUnreachableException();
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int newPosX = list.get(i).getPosX();
            int newPosY = list.get(i).getPosY();
            System.out.println("Forklift moving: [" + forklift.getXPos() + "," + forklift.getYPos()
                    + "] --> [" + newPosX + "," + newPosY + "]");
            forklift.setPosition(newPosX, newPosY);
        }
        System.out.println("Wózek dotarł do celu.");
    }

}
