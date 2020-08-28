package com.example.cherry.exampledb;

public class MyModel {

   public MyModel(){

   }

    String name,roll,number;

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getNumber() {
        return number;
    }

    public MyModel(String name, String roll, String number) {
        this.name = name;
        this.roll = roll;
        this.number = number;
    }
}
