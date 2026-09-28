package com.example.demo.factory.method;

public class test {
    public static void main(String[] args) {
        CoffeeStore coffeeStore = new CoffeeStore();
        coffeeStore.setCoffeeFactory(new AmericanCofferfactory());
        Coffee coffee = coffeeStore.orderCoffee();
        System.out.println(coffee.getName());
    }
}
