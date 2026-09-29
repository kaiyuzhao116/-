package com.example.demo.factory.test;

public class CoffeStore {

    private CoffeFactory factory;

    public CoffeStore(CoffeFactory factory) {
        this.factory = factory;
    }

    public Coffee orderCoffee() {
        Coffee coffee = factory.createCoffee();
        coffee.addMilk();
        coffee.addSugar();
        return coffee;
    }
}
