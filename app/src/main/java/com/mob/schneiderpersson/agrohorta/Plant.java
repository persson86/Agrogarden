package com.mob.schneiderpersson.agrohorta;

public class Plant {
    String name;
    int imageResource;

    public Plant(){}

    public Plant(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource){
        this.imageResource = imageResource;
    }
}
