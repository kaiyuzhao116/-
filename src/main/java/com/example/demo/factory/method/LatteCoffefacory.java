package com.example.demo.factory.method;

public class LatteCoffefacory implements CoffeeFactory {
    @Override
    public Coffee createCoffee() {
        return new LatteCoffee();
    }
}
