package com.example.calculatorhide.Model;

import android.graphics.drawable.Drawable;

public class HomeModel {
    int image;
    String name;
    String subname;
    int progrssimage;
    int color;

    public HomeModel(int image, String name, String subname, int progrssimage,int color) {
        this.image = image;
        this.name = name;
        this.subname = subname;
        this.progrssimage = progrssimage;
        this.color = color;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public int getProgrssimage() {
        return progrssimage;
    }

    public void setProgrssimage(int progrssimage) {
        this.progrssimage = progrssimage;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
