package com.example.belegen;

import java.util.List;

public class updateEmployee {
    private String firstName, lastName, cnic, address;
    private List<String> number;

    updateEmployee()
    {

    }

    public updateEmployee(String firstName, String lastName, String cnic, String address, List<String> number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnic = cnic;
        this.address = address;
        this.number = number;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCnic() {
        return cnic;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getNumber() {
        return number;
    }
}
