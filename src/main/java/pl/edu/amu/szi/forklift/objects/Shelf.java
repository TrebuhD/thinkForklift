package pl.edu.amu.szi.forklift.objects;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Objects;

public class Shelf extends GameObject {
    private ArrayList<Package> packageList = new ArrayList<>();;
    private int shelfMaxWeight; // max weight of pkg on shelf

    public Shelf(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY, int shelfMaxWeight) {
        super(gc, imageSrc, tileWidth, tileHeight, initX, initY);
        this.shelfMaxWeight = shelfMaxWeight;
    }

    public boolean addPkg(Package pkg){ // returns 1 if added, otherwise 0
        if(checkShelf(pkg)){
            packageList.add(pkg);
            return true;
        } else {
            return false;
        }
    }

    public int getWeightOnShelf()
    {
        int weight=0;
        for(int i=0; i<packageList.size(); i++)
        {
            weight = weight + packageList.get(i).getWeight();
        }
        return weight;
    }

    private boolean checkShelf(Package pkg){
        int size = packageList.size();

        if(getWeightOnShelf()+pkg.getWeight()>shelfMaxWeight)
        {
            System.out.println("PKG's on shelf are too heavy!");
            return false;
        }
        if(size==3)
        {
            System.out.println("Error: Shelf already have 3 packages!");
            return false;
        } else if(size > 0)
        {
            if (Objects.equals(pkg.getType(),packageList.get(size-1).getType())
                || Objects.equals(packageList.get(size-1).getType(), new String("Light")))
            {
                System.out.println("PKG Added to shelf.");
                return true;
            }
            else
            {
                System.out.println("Error: PKG are in wrong order! Light should be on heavy!");
                return false;
            }
        } else {
            System.out.println("PKG added to shelf.");
            return true;
        }
    }
}
