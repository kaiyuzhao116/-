package com.example.demo.factory.before;

public class CoffeeStore {
    public Coffee orderCoffee(String type) {
        Coffee coffee = createCoffee(type);
        coffee.addSugar();
        coffee.addMilk();
        return coffee;
    }
    private Coffee createCoffee(String type) {
        if ("american".equals(type)) {
            return new AmericanCoffee();
        } else if ("latte".equals(type)) {
            return new LatteCoffee();
        } else {
            throw new RuntimeException("Unknown type: " + type);
        }
    }

}
