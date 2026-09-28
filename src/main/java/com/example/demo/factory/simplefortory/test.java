package com.example.demo.factory.simplefortory;

public class test {
    public static void main(String[] args) {
        CoffeeStore coffeeStore = new CoffeeStore();
        Coffee latte = coffeeStore.orderCoffee("latte");
        System.out.println(latte.getName());
        Coffee american = coffeeStore.orderCoffee("american");
        System.out.println(american.getName());
        
    }
}
