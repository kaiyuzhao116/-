package com.example.demo.factory.method;

public class CoffeeStore {

    private CoffeeFactory coffeeFactory;

    public void setCoffeeFactory(CoffeeFactory coffeeFactory) {
        this.coffeeFactory = coffeeFactory;
    }


    // 创建咖啡
    public Coffee orderCoffee() {
        Coffee coffee = coffeeFactory.createCoffee();
        coffee.addMilk();
        return coffee;
    }

}
