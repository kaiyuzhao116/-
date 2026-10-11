package com.example2.demo1;

public class Coffee extends Drink{
    public Coffee() {
        super(15,"咖啡");
    }

    @Override
    public float cost() {
        return getPrice();
    }

}
