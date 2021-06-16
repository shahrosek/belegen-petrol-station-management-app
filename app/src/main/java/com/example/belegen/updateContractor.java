package com.example.belegen;

import java.util.List;

public class updateContractor {
    private String firstName, lastName, address, cnic;
    private List<String> number;
    private double amount;
    private boolean status;

    updateContractor(){
        // Required for Firestore
    }

    public updateContractor(String firstName, String lastName, String address, String cnic, List<String> number, double amount, boolean status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.cnic = cnic;
        this.number = number;
        this.amount = amount;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCnic() {
        return cnic;
    }

    public List<String> getNumber() {
        return number;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isStatus() {
        return status;
    }
}
