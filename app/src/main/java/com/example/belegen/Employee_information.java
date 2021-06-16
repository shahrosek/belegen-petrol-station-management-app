package com.example.belegen;

public class Employee_information implements java.io.Serializable{
    String name;
    String id;
    String Password;
    String cnic;
    String address;
    String number;
 public Employee_information()
 {
        this.name=null;
        this.id=null;
        this.number=null;
        this.address=null;
        this.cnic=null;
        this.Password=null;
 }
    public Employee_information(String name, String id, String password, String cnic, String address, String number) {
        this.name = name;
        this.id = id;
        Password = password;
        this.cnic = cnic;
        this.address = address;
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return Password;
    }

    public String getCnic() {
        return cnic;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }
}
