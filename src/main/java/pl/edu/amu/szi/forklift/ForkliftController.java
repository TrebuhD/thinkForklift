package pl.edu.amu.szi.forklift;

import static pl.edu.amu.szi.forklift.ThinkForklift.forkLift;
import static pl.edu.amu.szi.forklift.Map.shelfList;

import java.util.HashSet;

public class ForkliftController {

    public static void handleInput(HashSet currentKeys) {
        if (currentKeys.contains("LEFT")) {
            if (!Map.collisionFound(forkLift.getXPos() - 1, forkLift.getYPos(), shelfList)) {
                forkLift.moveLeft();
            }
        } else if (currentKeys.contains("RIGHT")) {
            if (!Map.collisionFound(forkLift.getXPos() + 1, forkLift.getYPos(), shelfList)) {
                forkLift.moveRight();
            }
        } else if (currentKeys.contains("UP")) {
            if (!Map.collisionFound(forkLift.getXPos(), forkLift.getYPos() - 1, shelfList)) {
                forkLift.moveUp();
            }
        } else if (currentKeys.contains("DOWN")) {
            if (!Map.collisionFound(forkLift.getXPos(), forkLift.getYPos() + 1, shelfList)) {
                forkLift.moveDown();
            }
//      pick up package
        } else if (currentKeys.contains("SPACE")) {
            if (Map.packageFound(forkLift.getXPos(), forkLift.getYPos())) {
                if (forkLift.hasCargo()) {
                    forkLift.dropCargo();
                } else {
                    forkLift.pickUpCargo(Map.getPackageAtPos(forkLift.getXPos(), forkLift.getYPos()));
                }
            }
        }
    }

}
