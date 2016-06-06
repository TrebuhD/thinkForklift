package pl.edu.amu.szi.forklift.objects;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class Package extends GameObject {
    private Integer weight;
    private Integer value;
    private String type;
    private boolean isHidden;

    public Package(GraphicsContext gc, String imageSrc, float tileWidth, float tileHeight, int initX, int initY, int weight, String type) {
        super(gc, imageSrc, tileWidth, tileHeight, initX, initY);
        this.weight = weight;
        this.type = type;
        this.isHidden = false;
        Random ran = new Random();
        if(((String)"Heavy").equals(type)){
            if(weight > 15)
            {
                this.value = weight * 1 + ran.nextInt(30);
            } else {
                this.value = weight * 1 + ran.nextInt(50);
            }
        } else { // type light
            if(weight > 7)
            {
                this.value = weight * 9 + ran.nextInt(40);
            } else {
                this.value = weight * 4 + ran.nextInt(20);
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public int getValue() { return value; }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }
}
