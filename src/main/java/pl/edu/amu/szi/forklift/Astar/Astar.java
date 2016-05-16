package pl.edu.amu.szi.forklift.Astar;

import pl.edu.amu.szi.forklift.Map;
import pl.edu.amu.szi.forklift.objects.Forklift;
import pl.edu.amu.szi.forklift.utils.Constants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Astar {

    private Forklift forklift;
    private Node[][] passablePath;

    public Astar(Forklift forklift) {
        this.forklift = forklift;
        this.passablePath = preparePassablePath();
    }

    public List<Node> findPath(int startX, int startY, int destinationX, int destinationY) {

        List<Node> finalResult = new ArrayList<>();

        // compare F values
        Comparator<Node> nodeComparator = (o1, o2) -> o1.getF() - o2.getF();

        ArrayList<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();

        Node destinationNode = passablePath[destinationX][destinationY];

        Node currentNode = passablePath[startX][startY];

        // if destination node is not passable, walk next to destination node
        if (!destinationNode.isPassable()) {

            List<Node> adjecencies = new ArrayList<>();
            Node tempNode;
            if (destinationNode.getPosX() - 1 >= 0) {
                tempNode = passablePath[destinationNode.getPosX() - 1][destinationNode.getPosY()];
                if (tempNode.isPassable()) {
                    adjecencies.add(tempNode);
                }
            }
            if (destinationNode.getPosX() + 1 <= Constants.MAP_SIZE_X - 1) {
                tempNode = passablePath[destinationNode.getPosX() + 1][destinationNode.getPosY()];
                if (tempNode.isPassable()) {
                    adjecencies.add(tempNode);
                }
            }
            if (destinationNode.getPosY() - 1 >= 0) {
                tempNode = passablePath[destinationNode.getPosX()][destinationNode.getPosY() - 1];
                if (tempNode.isPassable()) {
                    adjecencies.add(tempNode);
                }
            }
            if (destinationNode.getPosY() + 1 <= Constants.MAP_SIZE_Y + 1) {
                tempNode = passablePath[destinationNode.getPosX()][destinationNode.getPosY() + 1];
                if (tempNode.isPassable()) {
                    adjecencies.add(tempNode);
                }
            }
            if (adjecencies.isEmpty()) {
                System.out.println("A* = Punkt docelowy nieosiągalny");
                return null;
            }

            // compute H-value (distance from end node)
            for (Node n : adjecencies) {
                n.setH(manhattanDistance(currentNode, n));
            }

            // find node with lowest H-value
            destinationNode = adjecencies.get(0);
            for (Node n : adjecencies) {
                if (n.getH() < currentNode.getH()) {
                    destinationNode = n;
                }
            }
        }

        currentNode.setH(manhattanDistance(currentNode, destinationNode));

        openList.add(currentNode);

        Node newNode;

        while(!openList.isEmpty() && !closedList.contains(destinationNode)) {
            // left
            if (currentNode.getPosX() - 1 >= 0) {
                newNode = passablePath[currentNode.getPosX() - 1][currentNode.getPosY()];
                applyHeuristic(openList, closedList, newNode, currentNode, destinationNode);
            }
            if (currentNode.getPosX() + 1 <= Constants.MAP_SIZE_X - 1) {
                newNode = passablePath[currentNode.getPosX() + 1][currentNode.getPosY()];
                applyHeuristic(openList, closedList, newNode, currentNode, destinationNode);
            }
            if (currentNode.getPosY() - 1 >= 0) {
                newNode = passablePath[currentNode.getPosX()][currentNode.getPosY() - 1];
                applyHeuristic(openList, closedList, newNode, currentNode, destinationNode);
            }
            if (currentNode.getPosY() + 1 <= Constants.MAP_SIZE_Y - 1) {
                newNode = passablePath[currentNode.getPosX()][currentNode.getPosY() + 1];
                applyHeuristic(openList, closedList, newNode, currentNode, destinationNode);
            }

            // don't pass the same node twice
            openList.remove(currentNode);
            closedList.add(currentNode);

            openList.sort(nodeComparator);

            currentNode = openList.get(0);

            System.out.println(closedList);
        }

        System.out.println(closedList);

        Node lastNode = closedList.get(closedList.size() - 1);
        // traverse back to find path
        while (lastNode.getPreviousNode() != null) {
            finalResult.add(lastNode);
            System.out.println("path: ");
            System.out.println(lastNode);
            lastNode = lastNode.getPreviousNode();
        }

        finalResult.add(lastNode);
        return finalResult;
    }

    private void applyHeuristic(ArrayList<Node> openList, List<Node> closedList, Node newNode, Node currentNode, Node destinationNode) {
        if (newNode.isPassable() && !closedList.contains(newNode)) {
            if (openList.contains(newNode)) {
                if (newNode.getG() > currentNode.getG() + newNode.getCost()) {
                    newNode.setG(currentNode.getG() + newNode.getCost()).setPreviousNode(currentNode);
                }
            } else {
                openList.add(newNode.setPreviousNode(currentNode).setG(currentNode.getG()
                        + newNode.getCost()).setH(manhattanDistance(newNode, destinationNode)));
            }
        }
    }

    private static int manhattanDistance(Node begin, Node end) {
        int distX = Math.abs(end.getPosX() - begin.getPosX());
        int distY = Math.abs(end.getPosY() - begin.getPosY());
        return distX + distY;
    }

    private static Node[][] preparePassablePath() {
        Map map = Map.getInstance();

        int sizeX = Constants.MAP_SIZE_X;
        int sizeY = Constants.MAP_SIZE_Y;

        Node[][] path = new Node[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {

                path[i][j] = new Node(i, j, map.isPassable(i, j));
            }
        }

        return path;
    }
}
