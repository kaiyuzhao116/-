package com.example.demo.factory.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AmericanCoffeeFactory implements CoffeFactory {
    @Override
    public Coffee createCoffee() {

        log.info("AmericanCoffeeFactory createCoffee_________________");
        return new AmericanCoffe();
    }
}
