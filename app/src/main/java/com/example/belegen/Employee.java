package com.example.belegen;

import java.util.List;

public class Employee {
    private String firstName, lastName, cnic, address;
    private List<String> number;

    Employee()
    {

    }

    public Employee(String firstName, String lastName, String cnic, String address, List<String> number) {
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
