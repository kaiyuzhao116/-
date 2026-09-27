package com.example.demo.factory.before;

public abstract class Coffee {

    public abstract String getName();

    public void addSugar() {
        System.out.println("Adding sugar to the coffee");
    }
    public void addMilk() {
        System.out.println("Adding milk to the coffee");
    }

}
