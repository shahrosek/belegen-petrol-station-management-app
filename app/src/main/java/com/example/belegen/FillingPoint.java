package com.example.belegen;

public class FillingPoint {
    private int number;
    private String name;

    public FillingPoint(){
        // Empty constructor for Firestore
    }

    public FillingPoint(int fillingPoint, String name) {
        this.number = fillingPoint;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
