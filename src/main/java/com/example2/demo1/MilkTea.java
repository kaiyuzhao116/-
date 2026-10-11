package com.example2.demo1;

public class MilkTea extends Drink {

    public MilkTea() {
        super(12,"珍珠奶茶");
    }

    @Override
    public float cost() {
        return getPrice();
    }
}
