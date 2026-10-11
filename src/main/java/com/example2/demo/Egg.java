package com.example2.demo;

public class Egg extends Garnish {
    public Egg(FastFood fastFood) {
        super(fastFood, 1, "鸡蛋");
    }

    @Override
    public float cost() {

        //鸡蛋价格+ 原来主食的总价
        return getPrice() + getFastFood().cost();

    }

    @Override
    public String getDesc() {
        return super.getDesc() + getFastFood().getDesc();
    }

}
