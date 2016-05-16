package pl.edu.amu.szi.forklift;

import pl.edu.amu.szi.forklift.objects.Forklift;

import java.util.HashSet;

public class ForkliftController {

    public static void handleInput(HashSet currentKeys, Forklift forklift) {
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
        }
    }

}
