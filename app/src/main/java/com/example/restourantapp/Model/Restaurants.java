package com.example.restourantapp.Model;

public class Restaurants {

    private String Name, address, image, description, phone, dressCode, parking;

    public Restaurants() {
    }

    public Restaurants(String name, String address, String image, String description, String phone, String dressCode, String parking) {
        Name = name;
        this.address = address;
        this.image = image;
        this.description = description;
        this.phone = phone;
        this.dressCode = dressCode;
        this.parking = parking;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDressCode() {
        return dressCode;
    }

    public void setDressCode(String dressCode) {
        this.dressCode = dressCode;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }
}
