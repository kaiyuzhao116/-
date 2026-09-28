package com.example.demo.factory.simplefortory;

public class CoffeeStore {
    public Coffee orderCoffee(String type) {
        Simplfortory factory = new Simplfortory();
        return factory.createCoffee(type);

    }


}
