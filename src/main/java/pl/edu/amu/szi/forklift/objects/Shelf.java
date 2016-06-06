package pl.edu.amu.szi.forklift.objects;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Objects;

public class Shelf extends GameObject {
    private ArrayList<Package> packageList = new ArrayList<>();;
    public static final int SHELF_MAX_WEIGHT = 25; // max weight of pkg on shelf
    public static final int MAX_PACKAGES = 3;
    private int shelfMaxWeight; // max weight of pkg on shelf

    public Shelf(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY) {
        super(gc, imageSrc, tileWidth, tileHeight, initX, initY);
    }

    public boolean addPkg(Package pkg){ // returns 1 if added, otherwise 0
        if(checkShelf(pkg)){
            packageList.add(pkg);
            return true;
        } else {
            return false;
        }
    }

    public Package getTopPackageFromShelf(){
        if(notEmpty())
        {
            Package pkg = packageList.get(packageList.size()-1);
            packageList.remove(pkg);
            return pkg;
        } else {
            System.out.println("Error: There are no packages to take!");
            return null;
        }
    }

    public int getWeightOnShelf()
    {
        int weight=0;
        for (Package aPackageList : packageList) {
            weight = weight + aPackageList.getWeight();
        }
        return weight;
    }

    public boolean checkShelf(Package pkg){
        int size = packageList.size();

        if(getWeightOnShelf()+pkg.getWeight()>SHELF_MAX_WEIGHT)
        {
            System.out.println("PKG's on shelf are too heavy!");
            return false;
        }
        if(size==MAX_PACKAGES)
        {
            System.out.println("Error: Shelf already have 3 packages!");
            return false;
        } else if(size > 0)
        {
            if (Objects.equals(pkg.getType(),packageList.get(size-1).getType())
                    || Objects.equals(packageList.get(size-1).getType(), "Light"))
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
            return true;
        }
    }

    public boolean notEmpty() {
        return packageList.size() > 0;
    }
}
