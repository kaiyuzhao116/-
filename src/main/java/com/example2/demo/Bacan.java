package com.example2.demo;

public class Bacan extends Garnish{
    public Bacan(FastFood fastFood) {
        super(fastFood, 2, " Bacon");
    }

    @Override
    public float cost() {
        return getPrice() + getFastFood().cost();
    }
    @Override
    public String getDesc() {
        return super.getDesc() + getFastFood().getDesc();
    }

}
