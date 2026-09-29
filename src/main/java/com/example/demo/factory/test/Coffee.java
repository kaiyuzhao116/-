package com.example.demo.factory.test;

public abstract class Coffee {

    public void addMilk() {
        System.out.println("Adding milk to the coffee");
    }

    public void addSugar() {
        System.out.println("Adding sugar to the coffee");
    }

    public abstract String getName();

}
