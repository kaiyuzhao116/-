package com.example2.demo1;

public class Topping extends Drink{
    private Drink drink;

    public Drink getDrink() {
        return drink;
    }
    public Topping(Drink drink,float price,String desc) {
        super(price,desc);
        this.drink = drink;
    }

    @Override
    public float cost() {
        return getPrice()+drink.cost();
    }
}
