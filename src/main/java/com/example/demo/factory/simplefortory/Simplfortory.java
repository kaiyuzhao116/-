package com.example.demo.factory.simplefortory;

public class Simplfortory {
    public Coffee createCoffee(String type) {
        if ("american".equals(type)) {
            return new AmericanCoffee();
        } else if ("latte".equals(type)) {
            return new LatteCoffee();
        } else {
            throw new RuntimeException("Unknown type: " + type);
        }
    }

}
