package com.example.belegen;

public class Fuel {
    private String name, type;
    private Float stock;
    private Float price;

    public Fuel() {
        // Constructor required for Firestore
    }

    public Fuel(String name, String type, Float price, Float stock) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }
    public String getType() { return type; }
    public Float getPrice() {
        return price;
    }
    public Float getStock() {
        return stock;
    }
}
