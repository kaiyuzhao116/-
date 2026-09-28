package com.example.demo.factory.method;

public class AmericanCofferfactory implements CoffeeFactory{
    @Override
    public Coffee createCoffee() {
        return new AmericanCoffee();
    }

}
