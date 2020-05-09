package com.example.restourantapp.Model;

public class Food {

    private String Name, description, price, image;

    public Food(String name, String description, String price, String image) {
        this.Name = name;
        this.description = description;
        this.price = price;
        this.image = image;
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
