package com.example.demo.factory.test;

public class LatteCoffeeFactory implements CoffeFactory {
    @Override
    public Coffee createCoffee() {
        return new LatteCoffee();
    }
}
