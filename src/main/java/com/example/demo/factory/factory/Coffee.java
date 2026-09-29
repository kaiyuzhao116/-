package com.example.demo.factory.factory;

public abstract class Coffee {

    public void addSugar() {
        System.out.println("Adding sugar to the coffee");
    }
    public void addMilk() {
        System.out.println("Adding milk to the coffee");
    }

    public abstract String getName();


}
