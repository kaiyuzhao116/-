package com.example.demo.factory.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test {
    public static void main(String[] args) {
        CoffeStore store = new CoffeStore(new AmericanCoffeeFactory());


        store.orderCoffee();

        System.out.println("????????????????????????");
        log.info("------______________________________");

        CoffeStore coffeStore = new CoffeStore(new LatteCoffeeFactory());
        coffeStore.orderCoffee();

        new CoffeStore(new TEaCoffeFoctory()).orderCoffee();


    }
}
