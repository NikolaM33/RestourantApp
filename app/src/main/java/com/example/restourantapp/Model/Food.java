package com.example.restourantapp.Model;

import java.util.List;

public class Food {

    private String Name, description, price, image;
    private List<Addon> Addon;

    public Food(String name, String description, String price, String image, List<Addon> Addon) {
        this.Name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.Addon = Addon;

    }

    public List<com.example.restourantapp.Model.Addon> getAddon() {
        return Addon;
    }

    public void setAddon(List<com.example.restourantapp.Model.Addon> addon) {
        Addon = addon;
    }

    public Food() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


