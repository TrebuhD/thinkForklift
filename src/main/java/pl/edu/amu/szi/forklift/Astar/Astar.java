package pl.edu.amu.szi.forklift.Astar;

import pl.edu.amu.szi.forklift.Map;
import pl.edu.amu.szi.forklift.utils.Constants;

public class Astar {

    private static int manhattanDistance(Node begin, Node end) {
        int distX = Math.abs(end.getPosX() - begin.getPosX());
        int distY = Math.abs(end.getPosY() - begin.getPosY());
        return distX + distY;
    }

    private static Node[][] preparePassablePath() {
        int sizeX = Constants.MAP_SIZE_X;
        int sizeY = Constants.MAP_SIZE_Y;

        Node[][] path = new Node[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {

                path[i][j] = new Node(i, j, Map.isPassable(i, j));
            }
        }

        return path;
    }
}
