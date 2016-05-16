package pl.edu.amu.szi.forklift.Astar;

public class Node implements Comparable<Node>{

    private int posX;
    private int posY;
    private int G;
    private int H;
    private int cost;

    private boolean isPassable;
    private Node previousNode;

    public Node(int x, int y, boolean isPassable) {
        this.posX = x;
        this.posY = y;
        this.isPassable = isPassable;
        this.H = 0;
        this.G = 0;
        this.cost = 1;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public Node setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
        return this;
    }

    public int getG() {
        return G;
    }

    public int getH() {
        return H;
    }

    public int getCost() {
        return cost;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Node setG(int g) {
        this.G = g;
        return this;
    }

    public Node setH(int h) {
        this.H = h;
        return this;
    }

    public int getF() {
        return this.getG() + this.getH();
    }

    @Override
    public String toString() {
        return "[Node x:" + getPosX() + ", y: " + getPosY() + " G:"+G+" H:"+H+"]";
    }

    @Override
    public int compareTo(Node o) {
        return this.getF() - o.getF();
    }

}
