package com.example.demo.factory.test;

public class TEaCoffeFoctory implements CoffeFactory {
    @Override
    public Coffee createCoffee() {
        return new TeaCoffe();
    }
}
