package com.example.belegen;

public class Sale {
    private String name, month;
    private int day, year, fillingPoint;
    private Double quantity, price;

    Sale(){
        // Required for Firestore
    }

    public Sale(String name, int fillingPoint, int day, String month, int year, Double quantity, Double price) {
        this.name = name;
        this.month = month;
        this.day = day;
        this.year = year;
        this.fillingPoint = fillingPoint;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getFillingPoint() {
        return fillingPoint;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setFillingPoint(int fillingPoint) {
        this.fillingPoint = fillingPoint;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
