package com.example2.demo1;

public class Pudding extends Topping{
    public Pudding(Drink drink) {
        super(drink, 3, "布丁");
    }
    public float cost() {
        return getPrice() + getDrink().cost();
    }
    public String getDesc() {
        return super.getDesc() + getDrink().getDesc();
    }

}
